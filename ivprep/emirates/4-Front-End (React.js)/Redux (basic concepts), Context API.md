Here’s a **professional-level explanation and example** of:

---

# ✅ Redux (Basic Concepts) vs Context API in React (with TypeScript)

---

## 🧠 1. REDUX: Overview

Redux is a **predictable state container** for JavaScript apps, often used in **large applications**.

### 🔁 Core Redux Concepts:

| Concept      | Description                                   |
| ------------ | --------------------------------------------- |
| **Store**    | Single source of truth (global state)         |
| **Action**   | Plain object describing **what happened**     |
| **Reducer**  | Pure function to update state based on action |
| **Dispatch** | Sends an action to the store                  |
| **Selector** | Extracts state from store                     |

---

### 🧪 Redux Example (with TypeScript):

**1. Setup:**

```bash
npm install @reduxjs/toolkit react-redux
```

**2. store.ts**

```ts
import { configureStore, createSlice, PayloadAction } from "@reduxjs/toolkit";

type CounterState = { value: number };

const initialState: CounterState = { value: 0 };

const counterSlice = createSlice({
  name: "counter",
  initialState,
  reducers: {
    increment: (state) => {
      state.value += 1;
    },
    decrement: (state) => {
      state.value -= 1;
    },
    addBy: (state, action: PayloadAction<number>) => {
      state.value += action.payload;
    },
  },
});

export const { increment, decrement, addBy } = counterSlice.actions;

const store = configureStore({
  reducer: {
    counter: counterSlice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;
```

**3. index.tsx**

```tsx
import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import store from "./store";
import App from "./App";

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById("root")
);
```

**4. Counter.tsx**

```tsx
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { RootState, increment, decrement, addBy } from "./store";

const Counter: React.FC = () => {
  const count = useSelector((state: RootState) => state.counter.value);
  const dispatch = useDispatch();

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => dispatch(increment())}>+</button>
      <button onClick={() => dispatch(decrement())}>-</button>
      <button onClick={() => dispatch(addBy(5))}>Add 5</button>
    </div>
  );
};

export default Counter;
```

---

## 🌐 2. React Context API: Overview

Context API is a **lighter alternative to Redux**, best for **simpler or medium-complex state sharing**.

### 🌲 Core Concepts:

| Concept      | Description                                       |
| ------------ | ------------------------------------------------- |
| **Context**  | Created using `React.createContext()`             |
| **Provider** | Component that **supplies** context value         |
| **Consumer** | Component that **uses** context with `useContext` |

---

### ✅ Context API Example (TypeScript)

**1. ThemeContext.tsx**

```tsx
import React, { createContext, useState, ReactNode, useContext } from "react";

type Theme = "light" | "dark";

type ThemeContextType = {
  theme: Theme;
  toggleTheme: () => void;
};

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const ThemeProvider = ({ children }: { children: ReactNode }) => {
  const [theme, setTheme] = useState<Theme>("light");

  const toggleTheme = () =>
    setTheme((prev) => (prev === "light" ? "dark" : "light"));

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = (): ThemeContextType => {
  const context = useContext(ThemeContext);
  if (!context) throw new Error("useTheme must be used inside ThemeProvider");
  return context;
};
```

**2. App.tsx**

```tsx
import React from "react";
import { ThemeProvider, useTheme } from "./ThemeContext";

const ThemeToggle: React.FC = () => {
  const { theme, toggleTheme } = useTheme();
  return (
    <div>
      <h1>Theme: {theme}</h1>
      <button onClick={toggleTheme}>Toggle</button>
    </div>
  );
};

const App: React.FC = () => (
  <ThemeProvider>
    <ThemeToggle />
  </ThemeProvider>
);
```

---

## ⚔️ Redux vs Context API

| Feature          | Redux                         | Context API                              |
| ---------------- | ----------------------------- | ---------------------------------------- |
| Scalability      | Very High                     | Medium                                   |
| Boilerplate      | More (but cleaner with RTK)   | Minimal                                  |
| Dev Tools        | Excellent (Redux DevTools)    | Limited                                  |
| Async Middleware | Thunk, Saga                   | Custom implementation                    |
| Use Case         | Complex apps with large state | Simpler apps, theme/auth/global settings |

---

## 🚀 Need a Combined Demo Project?

Would you like me to create a **GitHub starter project** with:

- A Redux-powered counter
- A Context-powered theme toggle
- React + TypeScript setup
- README and folder structure?

Let me know if you want a **GitHub repo** or **ZIP file** download!
