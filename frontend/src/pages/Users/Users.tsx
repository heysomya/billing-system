import { useEffect, useState } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardContent } from "@/components/ui/card";

type User = {
  id: string;
  username: string;
  role: string;
  createdAt: string;
  updatedAt: string;
};

type Customer = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  createdAt: string;
  updatedAt: string;
};

const Users = () => {
  const [cashiers, setCashiers] = useState<User[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);

  const BASE_URL = "http://localhost:8095/user-management";

  const getRandomCashierImage = () => {
    const imgs = ["/images/cashier-1.png", "/images/cashier-2.png"];
    return imgs[Math.floor(Math.random() * imgs.length)];
  };

  const getRandomCustomerImage = () => {
    const imgs = [
      "/images/customer-1.png",
      "/images/customer-2.png",
      "/images/customer-3.png",
      "/images/customer-4.png",
    ];
    return imgs[Math.floor(Math.random() * imgs.length)];
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersRes, customersRes] = await Promise.all([
          fetch(`${BASE_URL}/users`),
          fetch(`${BASE_URL}/customers`),
        ]);

        const usersData = await usersRes.json();
        const customersData = await customersRes.json();

        const cashierList = usersData.filter(
          (user: User) => user.role.toUpperCase() === "CASHIER"
        );

        setCashiers(cashierList);
        setCustomers(customersData);
      } catch (error) {
        console.error("Error fetching users or customers:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading)
    return (
      <MainLayout>
        <p className="text-center mt-10">Loading...</p>
      </MainLayout>
    );

  return (
    <MainLayout>
      <div className="space-y-10">
        {/* Cashiers Section */}
        <section>
          <h1 className="text-3xl font-semibold mb-4">Cashiers</h1>
          {cashiers.length === 0 ? (
            <p className="text-gray-500">No cashiers found.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {cashiers.map((cashier) => (
                <Card
                  key={cashier.id}
                  className="p-0 hover:shadow-md overflow-hidden"
                >
                  {/* Random Image */}
                  <div className="h-40 w-full overflow-hidden">
                    <img
                      src={getRandomCashierImage()}
                      alt={cashier.username}
                      className="w-full h-full pt-2 object-contain"
                    />
                  </div>

                  <CardContent className="p-4 space-y-2">
                    <p>
                      <strong>Username:</strong> {cashier.username}
                    </p>
                    <p>
                      <strong>Registered:</strong>{" "}
                      {new Date(cashier.createdAt).toLocaleString()}
                    </p>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </section>

        {/* Customers Section */}
        <section>
          <h1 className="text-3xl font-semibold mb-4">Customers</h1>
          {customers.length === 0 ? (
            <p className="text-gray-500">No customers found.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {customers.map((cust) => (
                <Card
                  key={cust.id}
                  className="p-0 hover:shadow-md overflow-hidden"
                >
                  {/* Random Image */}
                  <div className="h-40 w-full overflow-hidden">
                    <img
                      src={getRandomCustomerImage()}
                      alt={`${cust.firstName} ${cust.lastName}`}
                      className="w-full h-full pt-2 object-contain"
                    />
                  </div>

                  <CardContent className="p-4 space-y-2">
                    <p>
                      <strong>Name:</strong> {cust.firstName} {cust.lastName}
                    </p>
                    <p>
                      <strong>Email:</strong> {cust.email}
                    </p>
                    <p>
                      <strong>Phone:</strong> {cust.phone}
                    </p>
                    <p>
                      <strong>Registered:</strong>{" "}
                      {new Date(cust.createdAt).toLocaleString()}
                    </p>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </section>
      </div>
    </MainLayout>
  );
};

export default Users;
