Got it. You want **professional-level mastery** of **React Functional Components + Hooks** — **complete, no gaps**.

Here’s a **fully comprehensive guide** covering:

---

## ✅ 1. **Functional Components (Recap)**

**Stateless or Stateful** components using functions:

```jsx
function Greeting({ name }) {
  return <h1>Hello, {name}!</h1>;
}
```

---

## ✅ 2. **Essential React Hooks (Exhaustive List)**

| Hook                   | Purpose                                          |
| ---------------------- | ------------------------------------------------ |
| `useState`             | Local component state                            |
| `useEffect`            | Side effects (data fetch, subscriptions)         |
| `useContext`           | Access global state via context                  |
| `useReducer`           | Complex state logic (like Redux)                 |
| `useRef`               | Mutable value across renders; access DOM         |
| `useMemo`              | Cache expensive calculations                     |
| `useCallback`          | Cache function references                        |
| `useLayoutEffect`      | Like `useEffect` but runs **before paint**       |
| `useImperativeHandle`  | Expose custom methods to parent                  |
| `useDebugValue`        | Custom label for React DevTools                  |
| `useId` (React 18+)    | Stable unique ID for accessibility               |
| `useTransition`        | Mark state as low-priority (concurrent features) |
| `useDeferredValue`     | Defer value update for performance               |
| `useSyncExternalStore` | Subscribe to external stores (state libs)        |
| `useInsertionEffect`   | Inject styles before paint (for CSS-in-JS)       |

---

## 💡 3. **Real-World Use Cases**

---

### 🧠 `useState` – Local State

```jsx
const [count, setCount] = useState(0);
```

---

### ⚙️ `useEffect` – Side Effects (API calls, subscriptions)

```jsx
useEffect(() => {
  fetchData();
}, [dependency]); // Empty = run on mount only
```

---

### 🌍 `useContext` – Global State

```jsx
const ThemeContext = createContext();

function App() {
  return (
    <ThemeContext.Provider value="dark">
      <Child />
    </ThemeContext.Provider>
  );
}

function Child() {
  const theme = useContext(ThemeContext);
  return <div className={theme}>Theme Applied</div>;
}
```

---

### 🔄 `useReducer` – State Management Like Redux

```jsx
const reducer = (state, action) => {
  switch (action.type) {
    case "increment":
      return { count: state.count + 1 };
    default:
      return state;
  }
};

const [state, dispatch] = useReducer(reducer, { count: 0 });
```

---

### 📌 `useRef` – DOM Access or Mutable Value

```jsx
const inputRef = useRef(null);

useEffect(() => {
  inputRef.current.focus();
}, []);
```

---

### ⚡ `useMemo` – Expensive Computation Caching

```jsx
const filtered = useMemo(() => {
  return data.filter((item) => item.active);
}, [data]);
```

---

### 🔁 `useCallback` – Memoize Functions

```jsx
const handleClick = useCallback(() => {
  console.log("clicked");
}, []);
```

---

### 📐 `useLayoutEffect` – Measure DOM Before Paint

```jsx
useLayoutEffect(() => {
  const height = divRef.current.offsetHeight;
}, []);
```

---

### 🛠️ `useImperativeHandle` + `forwardRef` – Custom Methods

```jsx
const Input = forwardRef((props, ref) => {
  useImperativeHandle(ref, () => ({
    focus: () => inputRef.current.focus(),
  }));
  const inputRef = useRef();
  return <input ref={inputRef} />;
});
```

---

### 🧪 `useDebugValue` – Debug in DevTools

```jsx
useDebugValue(user ? "Logged In" : "Guest");
```

---

### 🆔 `useId` – Accessibility ID for SSR

```jsx
const id = useId();
<label htmlFor={id}>Name</label>
<input id={id} />
```

---

### ⏳ `useTransition` – Deferred UI (React 18+)

```jsx
const [isPending, startTransition] = useTransition();

startTransition(() => {
  setSearchQuery(value);
});
```

---

### 🐌 `useDeferredValue` – Delay Value Updates

```jsx
const deferredSearch = useDeferredValue(searchText);
```

---

### 📡 `useSyncExternalStore` – Sync with External Store

```jsx
const theme = useSyncExternalStore(subscribe, getSnapshot);
```

---

### 🎨 `useInsertionEffect` – CSS-in-JS Styling

```jsx
useInsertionEffect(() => {
  // Inject styles before paint
}, []);
```

---

## 🧠 Bonus: Pro Patterns

### 🧩 Combine Hooks into Custom Hook

```jsx
function useUserData(userId) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    fetchUser(userId).then(setUser);
  }, [userId]);

  return user;
}
```

---

### 🧼 Cleanups in `useEffect`

```jsx
useEffect(() => {
  const id = setInterval(tick, 1000);
  return () => clearInterval(id); // cleanup
}, []);
```

---

## ⚠️ Avoid These Mistakes

| Mistake                             | Fix                                     |
| ----------------------------------- | --------------------------------------- |
| Calling hooks conditionally         | Always call at top level                |
| Not providing dependencies          | Always list `useEffect`, `useMemo` deps |
| Recreating functions inside render  | Wrap with `useCallback`                 |
| Using `useState` for derived values | Use `useMemo` or compute on-the-fly     |

---
