import { Navigate } from "react-router-dom";

interface RoleProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles: string[]; // e.g. ["ADMIN", "CASHIER"]
}

function isTokenExpired(token: string): boolean {
  try {
    const [, payloadBase64] = token.split(".");
    const payload = JSON.parse(atob(payloadBase64));
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

export default function RoleProtectedRoute({
  children,
  allowedRoles,
}: RoleProtectedRouteProps) {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // 🔐 Basic token checks
  if (!token || isTokenExpired(token)) {
    localStorage.clear();
    return <Navigate to="/login" replace />;
  }

  // 🚫 Role check
  if (!role || !allowedRoles.includes(role)) {
    // If role not allowed, redirect to dashboard (or a 403 page)
    return <Navigate to="/dashboard" replace />;
  }

  // ✅ Authorized
  return <>{children}</>;
}
