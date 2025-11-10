import { useNavigate } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Package, BarChart3, ShoppingCart, Users, FileText } from "lucide-react";
import MainLayout from "@/layouts/MainLayout";

export default function Dashboard() {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const allTiles = [
    {
      title: "Product Catalog",
      description: "View, add, edit, or delete products.",
      icon: <Package className="w-8 h-8 text-blue-500" />,
      route: "/products",
      allowed: ["ADMIN", "CASHIER"],
    },
    {
      title: "Stock Management",
      description: "Track stock levels and view low-stock alerts.",
      icon: <BarChart3 className="w-8 h-8 text-orange-500" />,
      route: "/stock",
      allowed: ["ADMIN"], // only admins
    },
    {
      title: "Sales / Make a Sale",
      description: "Record sales and generate receipts automatically.",
      icon: <ShoppingCart className="w-8 h-8 text-green-500" />,
      route: "/sales",
      allowed: ["CASHIER"],
    },
    {
      title: "Reports",
      description: "View daily, weekly, and monthly summaries.",
      icon: <FileText className="w-8 h-8 text-purple-500" />,
      route: "/reports",
      allowed: ["ADMIN"], // only admins
    },
    {
      title: "Users and Customers",
      description: "Manage user details and permissions.",
      icon: <Users className="w-8 h-8 text-teal-500" />,
      route: "/users",
      allowed: ["ADMIN"],
    }
  ];

  const visibleTiles = allTiles.filter((tile) => tile.allowed.includes(role ?? "CASHIER"));

  return (
    <MainLayout>
      <h1 className="text-3xl font-semibold mb-8 text-gray-800 dark:text-gray-100">
        Dashboard
      </h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {visibleTiles.map((tile, index) => (
          <Card
            key={index}
            className="hover:shadow-lg transition-all duration-300 cursor-pointer"
            onClick={() => navigate(tile.route)}
          >
            <CardHeader className="flex flex-col items-center justify-center text-center space-y-3">
              {tile.icon}
              <CardTitle className="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {tile.title}
              </CardTitle>
            </CardHeader>
            <CardContent className="text-sm text-gray-600 dark:text-gray-300 text-center px-4 pb-6">
              {tile.description}
            </CardContent>
          </Card>
        ))}
      </div>
    </MainLayout>
  );
}

