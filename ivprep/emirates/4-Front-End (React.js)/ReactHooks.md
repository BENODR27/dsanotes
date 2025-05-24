Here's a **complete professional guide to React Hooks** in **TypeScript**, covering all major built-in hooks with clean, real-world examples.

---

## 🧠 What Are Hooks?

Hooks let you **use state and lifecycle features** in functional components.

---

## ✅ 1. `useState`

Manages **local state** in a component.

```tsx
import React, { useState } from "react";

const Counter: React.FC = () => {
  const [count, setCount] = useState<number>(0);

  return (
    <>
      <p>Count: {count}</p>
      <button onClick={() => setCount((prev) => prev + 1)}>Increment</button>
    </>
  );
};
```

---

## ✅ 2. `useEffect`

Performs **side effects** (e.g., fetching data, subscriptions).

```tsx
import React, { useEffect, useState } from "react";

const DataFetcher: React.FC = () => {
  const [data, setData] = useState<string | null>(null);

  useEffect(() => {
    fetch("https://api.example.com/data")
      .then((res) => res.text())
      .then(setData);
  }, []); // [] = run only on mount

  return <div>{data || "Loading..."}</div>;
};
```

---

## ✅ 3. `useContext`

Consumes a **React context**.

```tsx
import React, { useContext } from "react";
import { ThemeContext } from "./ThemeProvider";

const ThemeToggle: React.FC = () => {
  const theme = useContext(ThemeContext);
  return <div>Theme is: {theme}</div>;
};
```

---

## ✅ 4. `useRef`

Persists a **mutable reference** across renders (e.g., for DOM access or timers).

```tsx
import React, { useRef, useEffect } from "react";

const InputFocus: React.FC = () => {
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    inputRef.current?.focus();
  }, []);

  return <input ref={inputRef} placeholder="Focus on mount" />;
};
```

---

## ✅ 5. `useReducer`

A more **predictable state management** pattern than `useState`, great for complex state.

```tsx
import React, { useReducer } from "react";

type State = { count: number };
type Action = { type: "increment" } | { type: "decrement" };

function reducer(state: State, action: Action): State {
  switch (action.type) {
    case "increment":
      return { count: state.count + 1 };
    case "decrement":
      return { count: state.count - 1 };
  }
}

const CounterWithReducer: React.FC = () => {
  const [state, dispatch] = useReducer(reducer, { count: 0 });

  return (
    <>
      <p>{state.count}</p>
      <button onClick={() => dispatch({ type: "increment" })}>+</button>
      <button onClick={() => dispatch({ type: "decrement" })}>-</button>
    </>
  );
};
```

---

## ✅ 6. `useMemo`

Memoizes a **computed value**, preventing expensive recalculations.

```tsx
import React, { useMemo, useState } from "react";

const ExpensiveCalc: React.FC = () => {
  const [num, setNum] = useState(5);

  const factorial = useMemo(() => {
    const fact = (n: number): number => (n <= 1 ? 1 : n * fact(n - 1));
    return fact(num);
  }, [num]);

  return (
    <div>
      <p>
        Factorial of {num}: {factorial}
      </p>
      <button onClick={() => setNum(num + 1)}>+</button>
    </div>
  );
};
```

---

## ✅ 7. `useCallback`

Memoizes a **function** to prevent unnecessary re-renders.

```tsx
import React, { useCallback, useState } from "react";

type Props = {
  onClick: () => void;
};

const Child = React.memo(({ onClick }: Props) => {
  return <button onClick={onClick}>Click me</button>;
});

const Parent: React.FC = () => {
  const [count, setCount] = useState(0);

  const handleClick = useCallback(() => {
    setCount((prev) => prev + 1);
  }, []);

  return (
    <>
      <p>Count: {count}</p>
      <Child onClick={handleClick} />
    </>
  );
};
```

---

## ✅ 8. `useLayoutEffect`

Like `useEffect`, but fires **synchronously after all DOM mutations**.

```tsx
import React, { useLayoutEffect, useRef } from "react";

const LayoutLogger: React.FC = () => {
  const ref = useRef<HTMLDivElement>(null);

  useLayoutEffect(() => {
    console.log("Width:", ref.current?.offsetWidth);
  }, []);

  return <div ref={ref}>Measure me</div>;
};
```

---

## ✅ 9. `useImperativeHandle` + `forwardRef`

Exposes **imperative methods** to parent components.

```tsx
import React, { useImperativeHandle, useRef, forwardRef } from "react";

type InputHandle = {
  focus: () => void;
};

const CustomInput = forwardRef<InputHandle, {}>((_, ref) => {
  const inputRef = useRef<HTMLInputElement>(null);

  useImperativeHandle(ref, () => ({
    focus: () => inputRef.current?.focus(),
  }));

  return <input ref={inputRef} />;
});

const Parent: React.FC = () => {
  const ref = useRef<InputHandle>(null);
  return (
    <>
      <CustomInput ref={ref} />
      <button onClick={() => ref.current?.focus()}>Focus Input</button>
    </>
  );
};
```

---

## 🧑‍💼 Professional Tips

| Hook          | Common Use Case                           | Avoid Misuse                                  |
| ------------- | ----------------------------------------- | --------------------------------------------- |
| `useEffect`   | Fetching, subscriptions                   | Don't forget to cleanup!                      |
| `useMemo`     | Expensive calculations                    | Don’t overuse, memoizing too much is wasteful |
| `useCallback` | Memoizing functions                       | Only use when passing callbacks to children   |
| `useReducer`  | Complex state logic (like Redux-lite)     | Don’t use for trivial state                   |
| `useRef`      | DOM or value persistence (not re-renders) | Not for storing reactive state                |

---

## 🚀 Want a Downloadable Project?

I can create a complete starter project with:

- All hooks above
- Demo components
- React + TypeScript setup
- Ready-to-run with `npm install && npm start`

Would you like a **GitHub repo** or **ZIP download**?
