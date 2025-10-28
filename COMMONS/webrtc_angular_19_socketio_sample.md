# WebRTC Video Call — Backend (Node + Socket.io) + Angular 19 Frontend

This repository shows a minimal **WebRTC 1:1 video call** implementation using a Node.js signaling server (Socket.io) and an **Angular 19** frontend. It includes all the main files you'll need and instructions to run locally. It's intentionally simple so you can extend it (multi-room, auth, TURN servers, recordings, etc.).

---

## Project structure

```
webrtc-sample/
├─ server/
│  ├─ package.json
│  ├─ index.js
│  └─ README.md
├─ angular-client/
│  ├─ package.json
│  ├─ angular.json
│  ├─ tsconfig.json
│  ├─ src/
│  │  ├─ main.ts
│  │  ├─ app/
│  │  │  ├─ app.module.ts
│  │  │  ├─ app.component.ts
│  │  │  ├─ app.component.html
│  │  │  ├─ call/
│  │  │  │  ├─ call.component.ts
│  │  │  │  ├─ call.component.html
│  │  │  │  └─ call.component.css
│  │  │  └─ services/
│  │  │     └─ signaling.service.ts
│  │  └─ styles.css
└─ README.md
```

---

## Backend (server/index.js)

A simple socket.io-based signaling server. It only relays signaling messages (offer/answer/ice) between two peers in a room.

**server/package.json**

```json
{
  "name": "webrtc-signaling-server",
  "version": "1.0.0",
  "main": "index.js",
  "scripts": {
    "start": "node index.js"
  },
  "dependencies": {
    "express": "^4.18.2",
    "http": "^0.0.1-security",
    "socket.io": "^4.8.0",
    "cors": "^2.8.5"
  }
}
```

**server/index.js**

```js
const express = require('express');
const http = require('http');
const cors = require('cors');
const { Server } = require('socket.io');

const app = express();
app.use(cors());

const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: '*', // for demo only. In production, lock this down.
    methods: ['GET', 'POST']
  }
});

// Simple health
app.get('/', (req, res) => res.send('WebRTC signaling server running'));

// Room -> sockets mapping is implicit: socket.join(room)
io.on('connection', (socket) => {
  console.log('socket connected:', socket.id);

  socket.on('join', (roomId) => {
    console.log(`${socket.id} join ${roomId}`);
    const room = io.sockets.adapter.rooms.get(roomId);
    const numClients = room ? room.size : 0;

    if (numClients === 0) {
      socket.join(roomId);
      socket.emit('created', roomId);
    } else if (numClients === 1) {
      socket.join(roomId);
      socket.emit('joined', roomId);
      socket.to(roomId).emit('peer-joined');
    } else {
      // simple 2-person room cap for demo
      socket.emit('full', roomId);
    }
  });

  socket.on('signal', ({ roomId, data }) => {
    // relay signal to the other peer(s) in the room
    socket.to(roomId).emit('signal', data);
  });

  socket.on('leave', (roomId) => {
    socket.leave(roomId);
    socket.to(roomId).emit('peer-left');
  });

  socket.on('disconnect', () => {
    console.log('socket disconnected:', socket.id);
    // Note: you could broadcast peer-left to rooms here if needed
  });
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => console.log(`Signaling server listening on ${PORT}`));
```

> **Note:** For production, use HTTPS/WSS and configure TURN servers. The signaling server must be reachable by both peers.

---

## Angular 19 Frontend

We'll implement a single `CallComponent` and a `SignalingService` which connects to the socket.io server.

**angular-client/package.json** (essential parts)

```json
{
  "name": "webrtc-angular-client",
  "version": "1.0.0",
  "scripts": {
    "start": "ng serve --host 0.0.0.0 --port 4200"
  },
  "dependencies": {
    "@angular/core": "^19.0.0",
    "@angular/common": "^19.0.0",
    "socket.io-client": "^4.8.0"
  }
}
```

### Signaling service: `src/app/services/signaling.service.ts`

```ts
import { Injectable } from '@angular/core';
import { io, Socket } from 'socket.io-client';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SignalingService {
  private socket!: Socket;

  connect(url: string) {
    this.socket = io(url, { transports: ['websocket'] });
  }

  join(roomId: string) {
    this.socket.emit('join', roomId);
  }

  on(event: string): Observable<any> {
    return new Observable((observer) => {
      this.socket.on(event, (data: any) => observer.next(data));
      return () => this.socket.off(event);
    });
  }

  sendSignal(roomId: string, data: any) {
    this.socket.emit('signal', { roomId, data });
  }

  leave(roomId: string) {
    this.socket.emit('leave', roomId);
    this.socket.disconnect();
  }
}
```

### Call component

**src/app/call/call.component.ts**

```ts
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SignalingService } from '../services/signaling.service';

const ICE_CONFIG: RTCConfiguration = {
  iceServers: [
    { urls: 'stun:stun.l.google.com:19302' }
    // Add TURN servers here for production
    // { urls: 'turn:your.turn.server:3478', username: 'user', credential: 'pass' }
  ]
};

@Component({
  selector: 'app-call',
  templateUrl: './call.component.html',
  styleUrls: ['./call.component.css']
})
export class CallComponent implements OnInit, OnDestroy {
  localStream!: MediaStream;
  remoteStream!: MediaStream;
  pc!: RTCPeerConnection;
  roomId = '';
  isCaller = false;
  signalingUrl = 'http://localhost:3000'; // update if different

  constructor(private signaling: SignalingService) {}

  async ngOnInit() {
    this.signaling.connect(this.signalingUrl);

    // Subscribe to server events
    this.signaling.on('created').subscribe(() => {
      console.log('created');
      this.isCaller = true; // first person becomes caller
    });

    this.signaling.on('joined').subscribe(() => {
      console.log('joined');
    });

    this.signaling.on('peer-joined').subscribe(() => {
      console.log('peer joined');
      if (this.isCaller) this.createOffer();
    });

    this.signaling.on('signal').subscribe(async (data: any) => {
      console.log('signal received', data.type || data);
      if (!this.pc) await this.createPeerConnection();

      if (data.type === 'offer') {
        await this.pc.setRemoteDescription(new RTCSessionDescription(data));
        const answer = await this.pc.createAnswer();
        await this.pc.setLocalDescription(answer);
        this.signaling.sendSignal(this.roomId, answer);
      } else if (data.type === 'answer') {
        await this.pc.setRemoteDescription(new RTCSessionDescription(data));
      } else if (data.candidate) {
        try {
          await this.pc.addIceCandidate(data);
        } catch (err) {
          console.error('Error adding ICE candidate', err);
        }
      }
    });

    this.signaling.on('peer-left').subscribe(() => {
      console.log('peer left');
      this.closeConnection();
    });
  }

  async startLocalStream() {
    this.localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    const localVideo = document.getElementById('localVideo') as HTMLVideoElement;
    localVideo.srcObject = this.localStream;
    await localVideo.play().catch(() => {});
  }

  async joinRoom(id: string) {
    this.roomId = id;
    await this.startLocalStream();
    this.signaling.join(this.roomId);
    // If second peer joins, server emits 'peer-joined' to other peer
  }

  async createPeerConnection() {
    this.pc = new RTCPeerConnection(ICE_CONFIG);

    // attach local tracks
    this.localStream.getTracks().forEach((t) => this.pc.addTrack(t, this.localStream));

    // remote stream
    this.remoteStream = new MediaStream();
    const remoteVideo = document.getElementById('remoteVideo') as HTMLVideoElement;
    remoteVideo.srcObject = this.remoteStream;

    this.pc.ontrack = (event) => {
      event.streams[0].getTracks().forEach((t) => this.remoteStream.addTrack(t));
    };

    this.pc.onicecandidate = (event) => {
      if (event.candidate) {
        this.signaling.sendSignal(this.roomId, event.candidate);
      }
    };

    this.pc.onconnectionstatechange = () => {
      console.log('pc state', this.pc.connectionState);
      if (this.pc.connectionState === 'disconnected' || this.pc.connectionState === 'failed') {
        this.closeConnection();
      }
    };
  }

  async createOffer() {
    if (!this.pc) await this.createPeerConnection();
    const offer = await this.pc.createOffer();
    await this.pc.setLocalDescription(offer);
    this.signaling.sendSignal(this.roomId, offer);
  }

  hangup() {
    this.signaling.leave(this.roomId);
    this.closeConnection();
  }

  closeConnection() {
    if (this.pc) {
      try { this.pc.getSenders().forEach(s => s.track?.stop()); } catch(e){}
      this.pc.close();
    }
    const localVideo = document.getElementById('localVideo') as HTMLVideoElement;
    const remoteVideo = document.getElementById('remoteVideo') as HTMLVideoElement;
    if (localVideo?.srcObject) { (localVideo.srcObject as MediaStream).getTracks().forEach(t=>t.stop()); localVideo.srcObject = null; }
    if (remoteVideo?.srcObject) { (remoteVideo.srcObject as MediaStream).getTracks().forEach(t=>t.stop()); remoteVideo.srcObject = null; }
    this.pc = null as any;
    this.localStream = null as any;
    this.remoteStream = null as any;
  }

  ngOnDestroy() {
    this.hangup();
  }
}
```

**src/app/call/call.component.html**

```html
<div class="call-container">
  <div class="controls">
    <input type="text" placeholder="Room ID" #roomInput />
    <button (click)="joinRoom(roomInput.value)">Join</button>
    <button (click)="hangup()">Hang up</button>
  </div>

  <div class="videos">
    <video id="localVideo" autoplay muted playsinline></video>
    <video id="remoteVideo" autoplay playsinline></video>
  </div>
</div>
```

**src/app/call/call.component.css**

```css
.call-container { display:flex; flex-direction:column; gap:12px; align-items:center; }
.videos { display:flex; gap:12px; }
video { width:320px; height:240px; background:#000; border-radius:8px; }
.controls { display:flex; gap:8px; }
```

### App module + component wiring

**src/app/app.module.ts**

```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { CallComponent } from './call/call.component';

@NgModule({
  declarations: [AppComponent, CallComponent],
  imports: [BrowserModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
```

**src/app/app.component.html**

```html
<h1>Angular 19 WebRTC Demo</h1>
<app-call></app-call>
```

---

## How it works (high-level)

1. Both peers open the Angular page and join the same `roomId` via the signaling server.
2. Server keeps rooms and notifies the first/second peer using `created`, `joined`, and `peer-joined` events.
3. The first peer (`isCaller`) creates an SDP offer and sends it to the server which relays to the other peer.
4. The receiver sets the offer as remote description, creates an answer, sends back via signaling server.
5. Both exchange ICE candidates via `signal` messages.
6. Once ICE and SDP are negotiated, a direct peer-to-peer connection is established and media flows.

---

## Important production notes

- Use secure transport: HTTPS on the web server and WSS for socket.io.
- TURN servers are required when peers are behind restrictive NATs. Add TURN entries to `ICE_CONFIG`. You may run coturn or use a cloud provider.
- Add authentication (tokenized room joins) and room management if needed.
- Scale signaling: For many rooms/connections, consider horizontal scaling (Redis adapter for socket.io).
- Consider integrate data channels for text/controls.

---

## Quick run instructions (local)

1. Start server:

```bash
cd server
npm install
node index.js
# server listens on http://localhost:3000
```

2. Start Angular client (new terminal):

```bash
cd angular-client
npm install
npm start
# open http://localhost:4200
```

3. Open two browser windows (or two devices), open the Angular app, and join the same room id (any string). Allow camera/mic.

---

## Extending

- Add text chat with `pc.createDataChannel`.
- Add screen sharing by using `getDisplayMedia()` and replacing/adding tracks.
- Add file transfer with data channels and chunking.

---

If you'd like, I can also produce:
- A TypeScript-based server with Express + socket.io using ES modules.
- A more advanced Angular UI (call list, room creation, user tokens).
- TURN server configuration snippets (coturn).

Tell me which of those you'd like next and I'll add them directly into this canvas.

