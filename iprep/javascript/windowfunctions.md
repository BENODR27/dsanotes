In JavaScript, the `window` object represents the global object in the browser environment. It contains all global variables, functions, and browser-related APIs. Here's a comprehensive list of **commonly used window functions and methods** grouped by category:

---

### 📌 **Core Window Functions**

| Function                 | Description                                                            |
| ------------------------ | ---------------------------------------------------------------------- |
| `window.alert()`         | Displays an alert dialog with a message.                               |
| `window.confirm()`       | Displays a dialog with OK/Cancel and returns `true` or `false`.        |
| `window.prompt()`        | Displays a dialog with an input field and returns user input.          |
| `window.print()`         | Opens the print dialog for the current page.                           |
| `window.open()`          | Opens a new browser window or tab.                                     |
| `window.close()`         | Closes the current window (only if it was opened via `window.open()`). |
| `window.setTimeout()`    | Calls a function after a specified delay.                              |
| `window.setInterval()`   | Calls a function repeatedly with a fixed time delay.                   |
| `window.clearTimeout()`  | Cancels a timeout set with `setTimeout()`.                             |
| `window.clearInterval()` | Cancels a repeating interval set with `setInterval()`.                 |

---

### 🌐 **Window Navigation & Location**

| Function/Property           | Description                                                        |
| --------------------------- | ------------------------------------------------------------------ |
| `window.location`           | Provides info about the current URL and allows redirection.        |
| `window.location.href`      | Gets or sets the current URL.                                      |
| `window.location.reload()`  | Reloads the current page.                                          |
| `window.location.assign()`  | Loads a new document.                                              |
| `window.location.replace()` | Replaces the current document with a new one (no back navigation). |
| `window.history`            | Allows manipulation of the browser's history.                      |
| `window.history.back()`     | Goes to the previous page.                                         |
| `window.history.forward()`  | Goes to the next page.                                             |
| `window.history.go(n)`      | Loads a specific page from the session history.                    |

---

### 🧭 **Window Size and Position**

| Function                | Description                                     |
| ----------------------- | ----------------------------------------------- |
| `window.innerWidth`     | Width of the window's content area.             |
| `window.innerHeight`    | Height of the window's content area.            |
| `window.outerWidth`     | Entire width of the browser window.             |
| `window.outerHeight`    | Entire height of the browser window.            |
| `window.moveTo(x, y)`   | Moves the window to the specified coordinates.  |
| `window.resizeTo(w, h)` | Resizes the window to the specified dimensions. |

---

### 📦 **Storage and State**

| Function/Property       | Description                                          |
| ----------------------- | ---------------------------------------------------- |
| `window.localStorage`   | Persistent storage for key-value pairs.              |
| `window.sessionStorage` | Temporary storage (per session).                     |
| `window.cookies`        | Read/write document cookies (via `document.cookie`). |

---

### 📋 **Clipboard & Focus**

| Function                                 | Description                            |
| ---------------------------------------- | -------------------------------------- |
| `window.focus()`                         | Sets focus to the current window.      |
| `window.blur()`                          | Removes focus from the current window. |
| `window.getSelection()`                  | Returns selected text on the page.     |
| `window.navigator.clipboard.writeText()` | Writes to the clipboard (async).       |
| `window.navigator.clipboard.readText()`  | Reads from the clipboard (async).      |

---

### 📡 **Event Handling & Animation**

| Function                         | Description                         |
| -------------------------------- | ----------------------------------- |
| `window.addEventListener()`      | Adds an event listener.             |
| `window.removeEventListener()`   | Removes an event listener.          |
| `window.dispatchEvent()`         | Dispatches an event.                |
| `window.requestAnimationFrame()` | Requests animation frame callback.  |
| `window.cancelAnimationFrame()`  | Cancels an animation frame request. |

---

### 📜 **Dialog and Popups**

| Function           | Description          |
| ------------------ | -------------------- |
| `window.alert()`   | Basic alert message. |
| `window.confirm()` | Confirmation box.    |
| `window.prompt()`  | Input prompt box.    |

---

### 📱 **Device & Media Info**

| Function/Property            | Description                                        |
| ---------------------------- | -------------------------------------------------- |
| `window.navigator`           | Provides information about the browser and device. |
| `window.navigator.userAgent` | Returns user-agent string.                         |
| `window.screen`              | Contains screen properties like `width`, `height`. |

---

### 🧪 **Miscellaneous**

| Function                   | Description                         |
| -------------------------- | ----------------------------------- |
| `window.btoa()`            | Encodes a string in Base64.         |
| `window.atob()`            | Decodes a Base64 string.            |
| `window.scrollTo()`        | Scrolls to a specific position.     |
| `window.scrollBy()`        | Scrolls by a relative amount.       |
| `window.scrollX / scrollY` | Current horizontal/vertical scroll. |

---

Would you like a downloadable cheatsheet for these functions?
