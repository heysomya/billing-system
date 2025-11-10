import { useNavigate, useLocation } from "react-router-dom";
import { LogOut, User } from "lucide-react";
import { useEffect, useState } from "react";

const InternalNavbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [userRole, setUserRole] = useState<string | null>(null);
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    const role = localStorage.getItem("role");
    const name = localStorage.getItem("username");
    setUserRole(role);
    setUsername(name);
  }, []);

  const isActive = (path: string) => location.pathname === path;

  const navButtonStyle = (path: string) =>
    `px-3 py-2 rounded-md transition ${
      isActive(path)
        ? "text-blue-600 font-semibold bg-blue-100"
        : "text-gray-700 hover:text-blue-600 hover:bg-blue-50"
    }`;

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    navigate("/login", { replace: true });
  };

  // Define allowed links per role
  const navLinks = [
    { label: "Dashboard", path: "/dashboard", roles: ["ADMIN", "CASHIER"] },
    { label: "Products", path: "/products", roles: ["ADMIN", "CASHIER"] },
    { label: "Stock", path: "/stock", roles: ["ADMIN"] },
    { label: "Sales", path: "/sales", roles: ["CASHIER"] },
    { label: "Reports", path: "/reports", roles: ["ADMIN"] },
    { label: "Users", path: "/users", roles: ["ADMIN"] },
  ];

  return (
    <nav className="w-full bg-white dark:bg-gray-900 shadow-md border-b border-gray-200 dark:border-gray-700 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-3 flex items-center justify-between">
        {/* Logo */}
        <div
          className="text-2xl font-bold text-blue-600 cursor-pointer"
          onClick={() => navigate("/dashboard")}
        >
          BillFast
        </div>

        {/* Nav Links */}
        <div className="hidden md:flex space-x-4 text-gray-700 dark:text-gray-200 font-medium">
          {navLinks
            .filter((link) => userRole && link.roles.includes(userRole.toUpperCase()))
            .map((link) => (
              <button
                key={link.path}
                onClick={() => navigate(link.path)}
                className={navButtonStyle(link.path)}
              >
                {link.label}
              </button>
            ))}
        </div>

        {/* Right side */}
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
