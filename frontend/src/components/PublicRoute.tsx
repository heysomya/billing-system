import { Navigate } from "react-router-dom";

interface PublicRouteProps {
  children: React.ReactNode;
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

export default function PublicRoute({ children }: PublicRouteProps) {
  const token = localStorage.getItem("token");

  if (token && !isTokenExpired(token)) {
    // valid token → redirect to dashboard
    return <Navigate to="/dashboard" replace />;
  } else if (token && isTokenExpired(token)) {
    // expired → cleanup
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  return <>{children}</>;
}
