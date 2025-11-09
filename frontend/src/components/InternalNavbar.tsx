import { useNavigate, useLocation } from "react-router-dom";
import { LogOut, User } from "lucide-react";
import { useEffect, useState } from "react";

const InternalNavbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [userRole, setUserRole] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    // Fetch user data from localStorage when component mounts
    const role = localStorage.getItem("role");
    const name = localStorage.getItem("username");
    setUserRole(role);
    setUsername(name);
  }, []);

  // Helper function to check if the link is active
  const isActive = (path: string) => location.pathname === path;

  // Common button style for all nav items
  const navButtonStyle = (path: string) =>
    `px-3 py-2 rounded-md transition ${
      isActive(path)
        ? "text-blue-600 font-semibold bg-blue-100"
        : "text-gray-700 hover:text-blue-600 hover:bg-blue-50"
    }`;

  // Logout function
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");

    navigate("/login", { replace: true });
  };

  return (
    <nav className="w-full bg-white dark:bg-gray-900 shadow-md border-b border-gray-200 dark:border-gray-700 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-3 flex items-center justify-between">
        {/* Left side: Logo / App Name */}
        <div
          className="text-2xl font-bold text-blue-600 cursor-pointer"
          onClick={() => navigate("/dashboard")}
        >
          BillFast
        </div>

        {/* Middle: Nav Links */}
        <div className="hidden md:flex space-x-4 text-gray-700 dark:text-gray-200 font-medium">
          <button
            onClick={() => navigate("/dashboard")}
            className={navButtonStyle("/dashboard")}
          >
            Dashboard
          </button>

          <button
            onClick={() => navigate("/products")}
            className={navButtonStyle("/products")}
          >
            Products
          </button>

          <button
            onClick={() => navigate("/stock")}
            className={navButtonStyle("/stock")}
          >
            Stock
          </button>

          <button
            onClick={() => navigate("/sales")}
            className={navButtonStyle("/sales")}
          >
            Sales
          </button>

          <button
            onClick={() => navigate("/reports")}
            className={navButtonStyle("/reports")}
          >
            Reports
          </button>

          <button
            onClick={() => navigate("/customers")}
            className={navButtonStyle("/customers")}
          >
            Customers
          </button>
        </div>

        {/* Right side: User + Logout */}
        <div className="flex items-center space-x-4">
          <div className="flex items-center space-x-2 text-gray-700 dark:text-gray-200">
            <User className="w-5 h-5" />
            <span className="text-sm font-medium">
              {username
                ? `${username} (${userRole === "ADMIN" ? "Admin" : "Cashier"})`
                : "User"}
            </span>
          </div>

          <button
            onClick={handleLogout}
            className="flex items-center space-x-2 bg-red-500 hover:bg-red-600 text-white px-3 py-2 rounded-lg text-sm font-medium transition"
          >
            <LogOut className="w-4 h-4" />
            <span>Logout</span>
          </button>
        </div>
      </div>
    </nav>
  );
};

export default InternalNavbar;
