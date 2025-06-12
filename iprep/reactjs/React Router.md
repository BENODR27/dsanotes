Here's your **professional-grade React Router guide** — clean, complete, and structured for production-ready apps.

---

## ✅ 1. **Install React Router DOM**

```bash
npm install react-router-dom
```

---

## ✅ 2. **Basic Routing Setup**

```tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import About from "./pages/About";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
      </Routes>
    </BrowserRouter>
  );
}
```

---

## ✅ 3. **Nested Routes (Layout Pattern)**

```tsx
// Layout.jsx
import { Outlet, Link } from "react-router-dom";

export default function Layout() {
  return (
    <>
      <nav>
        <Link to="/">Home</Link> | <Link to="/dashboard">Dashboard</Link>
      </nav>
      <Outlet />
    </>
  );
}
```

```tsx
// App.jsx
<BrowserRouter>
  <Routes>
    <Route path="/" element={<Layout />}>
      <Route index element={<Home />} />
      <Route path="dashboard" element={<Dashboard />} />
    </Route>
  </Routes>
</BrowserRouter>
```

---

## ✅ 4. **Dynamic Routes with Params**

```tsx
<Route path="user/:id" element={<UserProfile />} />
```

Inside `UserProfile.jsx`:

```tsx
import { useParams } from "react-router-dom";
const { id } = useParams();
```

---

## ✅ 5. **Navigate Programmatically**

```tsx
import { useNavigate } from "react-router-dom";

const navigate = useNavigate();
navigate("/dashboard");
```

---

## ✅ 6. **Redirects (Navigate Component)**

```tsx
import { Navigate } from "react-router-dom";

<Route path="/old" element={<Navigate to="/new" replace />} />;
```

---

## ✅ 7. **404 Not Found Route**

```tsx
<Route path="*" element={<NotFound />} />
```

---

## ✅ 8. **Private Routes (Auth Guard)**

```tsx
// RequireAuth.jsx
import { Navigate, Outlet } from "react-router-dom";

const RequireAuth = ({ isAuthenticated }) => {
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};

// Usage
<Route element={<RequireAuth isAuthenticated={userLoggedIn} />}>
  <Route path="/dashboard" element={<Dashboard />} />
</Route>;
```

---

## ✅ 9. **Route Loaders (React Router 6.4+)**

For data fetching before render:

```tsx
// router.js
import { createBrowserRouter } from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/user/:id",
    element: <User />,
    loader: async ({ params }) =>
      fetch(`/api/user/${params.id}`).then((res) => res.json()),
  },
]);
```

Access in component:

```tsx
import { useLoaderData } from "react-router-dom";
const user = useLoaderData();
```

---

## ✅ 10. **Route-Based Code Splitting**

```tsx
const LazyPage = React.lazy(() => import("./pages/LazyPage"));

<Route
  path="/lazy"
  element={
    <React.Suspense fallback={<div>Loading...</div>}>
      <LazyPage />
    </React.Suspense>
  }
/>;
```

---

## ⚙️ Folder Structure Example

```
src/
├── routes/
│   ├── AppRouter.jsx
│   ├── RequireAuth.jsx
│   └── loaders.js
├── pages/
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Dashboard.jsx
│   └── NotFound.jsx
```

---

## 🧠 Bonus Tips

| Pro Practice                          | Why                                   |
| ------------------------------------- | ------------------------------------- |
| Use `<Outlet />`                      | Enables nested routes/layouts cleanly |
| Prefer `<Navigate />` for redirects   | Declarative and safe                  |
| Wrap private sections                 | Use `RequireAuth` + `Outlet` pattern  |
| Add wildcard route                    | Always include `<Route path="*">`     |
| Use `createBrowserRouter` for loaders | Cleaner data handling                 |

---

## Want Example Project?

I can generate a full React app with:

- 🧭 Protected routes
- 👥 User-based access
- 🚀 Lazy loading
- 🌐 Route loaders for data

