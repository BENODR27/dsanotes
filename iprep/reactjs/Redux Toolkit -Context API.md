Great — you're going **pro-level** with state management:
You want **Redux Toolkit** *and* **Context API** — **complete**, **clean**, and **production-structured**.

---

## 🧠 1. When to Use What?

| Use Case                                | Use `Context API` | Use `Redux Toolkit`  |
| --------------------------------------- | ----------------- | -------------------- |
| Light global state (theme, auth)        | ✅ Yes             | ❌ Overkill           |
| Complex state (CRUD, async, pagination) | ❌ Not scalable    | ✅ Yes                |
| DevTools / Middleware                   | ❌ Limited         | ✅ Built-in           |
| Boilerplate                             | ✅ Minimal         | ✅ Low with Toolkit   |
| Async logic                             | ❌ Needs custom    | ✅ `createAsyncThunk` |

---

## ✅ 2. Context API (Global State)

### Example: `ThemeContext` with Toggle

```tsx
// ThemeContext.js
import { createContext, useContext, useState } from "react";

const ThemeContext = createContext();

export const ThemeProvider = ({ children }) => {
  const [dark, setDark] = useState(false);
  const toggleTheme = () => setDark((prev) => !prev);

  return (
    <ThemeContext.Provider value={{ dark, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => useContext(ThemeContext);
```

### Usage:

```tsx
const ThemeSwitcher = () => {
  const { dark, toggleTheme } = useTheme();
  return (
    <button onClick={toggleTheme}>
      {dark ? "Switch to Light" : "Switch to Dark"}
    </button>
  );
};
```

#### Wrap App:

```tsx
<ThemeProvider>
  <App />
</ThemeProvider>
```

---

## 🚀 3. Redux Toolkit (Advanced Global State)

### 💾 Installation:

```bash
npm install @reduxjs/toolkit react-redux
```

---

### 3.1 🔧 `store.js`

```ts
import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./features/userSlice";

export const store = configureStore({
  reducer: {
    user: userReducer,
  },
});
```

---

### 3.2 👤 `userSlice.js`

```ts
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const fetchUsers = createAsyncThunk("user/fetchUsers", async () => {
  const res = await axios.get("/api/users");
  return res.data;
});

const userSlice = createSlice({
  name: "user",
  initialState: { list: [], loading: false },
  reducers: {
    addUser: (state, action) => {
      state.list.push(action.payload);
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.list = action.payload;
        state.loading = false;
      });
  },
});

export const { addUser } = userSlice.actions;
export default userSlice.reducer;
```

---

### 3.3 🌐 Wrap App with Provider

```tsx
import { Provider } from "react-redux";
import { store } from "./store";

<Provider store={store}>
  <App />
</Provider>
```

---

### 3.4 🎯 Component Usage

```tsx
import { useDispatch, useSelector } from "react-redux";
import { fetchUsers, addUser } from "./features/userSlice";

const Users = () => {
  const dispatch = useDispatch();
  const users = useSelector((state) => state.user.list);
  const loading = useSelector((state) => state.user.loading);

  useEffect(() => {
    dispatch(fetchUsers());
  }, []);

  return (
    <>
      {loading ? "Loading..." : users.map((u) => <div key={u.id}>{u.name}</div>)}
      <button onClick={() => dispatch(addUser({ id: 4, name: "Alice" }))}>
        Add User
      </button>
    </>
  );
};
```

---

## 📦 Folder Structure Best Practice

```
src/
├── app/
│   └── store.js
├── features/
│   └── user/
│       ├── userSlice.js
│       └── UserComponent.jsx
├── context/
│   └── ThemeContext.js
├── App.js
└── index.js
```

---

## ⚠️ Gotchas to Avoid

| Problem                        | Fix                                                               |
| ------------------------------ | ----------------------------------------------------------------- |
| Overusing Context              | Use Redux for frequent updates or large state                     |
| No memoization                 | Wrap components with `React.memo` and use `useMemo`/`useCallback` |
| Select entire store            | Use `useSelector((state) => state.slice.key)`                     |
| Triggering too many re-renders | Split state and memoize selectors                                 |

---

## ✅ Want a Full App Example?

I can generate:

* 👤 Auth with Context
* 👥 Users + async fetch with Redux Toolkit
* 📦 Docker + GitHub-ready structure

Just say:
**“Build the full stack app with context + redux.”**

