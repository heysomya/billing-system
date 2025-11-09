import { useEffect, useState, useRef } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Trash, Plus, CheckCircle } from "lucide-react";

type Product = {
  id: string;
  name: string;
  sku: string;
  sellingPrice: number;
  quantityOnHand: number;
};

type Customer = {
  id: string;
  name: string;
};

type SaleItem = {
  product: Product;
  quantity: number;
};

type NewCustomer = {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
};

const Sales = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [selectedCustomer, setSelectedCustomer] = useState<string>("");
  const [paymentMethod, setPaymentMethod] = useState<string>("CASH");
  const [searchQuery, setSearchQuery] = useState("");
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [cart, setCart] = useState<SaleItem[]>([]);
  const [isConfirmDialogOpen, setIsConfirmDialogOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [cashierId, setCashierId] = useState<string>("");

  const [isCustomerDialogOpen, setIsCustomerDialogOpen] = useState(false);
  const [newCustomer, setNewCustomer] = useState<NewCustomer>({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
  });
  const [isCustomerSubmitting, setIsCustomerSubmitting] = useState(false);

  const [customerSearch, setCustomerSearch] = useState("");
  const [showCustomerSuggestions, setShowCustomerSuggestions] = useState(false);

  const [isSuccessDialogOpen, setIsSuccessDialogOpen] = useState(false);
  const [recentSaleId, setRecentSaleId] = useState<string | null>(null);

  const PRODUCT_API = "http://localhost:8090/api/products/get/all";
  const CUSTOMERS_API = "http://localhost:8095/user-management/customers";
  const GET_USER_API = "http://localhost:8095/user-management/getUser";
  const SALE_API = "http://localhost:8092/sales";
  const REGISTER_CUSTOMER_API = "http://localhost:8093/auth/registerCustomer";

  const allCustomersRef = useRef<Customer[]>([]);

  // Fetch products, customers, and cashier info
  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const username = localStorage.getItem("username");
        if (username) {
          const userRes = await fetch(`${GET_USER_API}/${username}`);
          if (userRes.ok) {
            const userData = await userRes.json();
            setCashierId(userData.id);
          }
        }

        const [productRes, customerRes] = await Promise.all([
          fetch(PRODUCT_API),
          fetch(CUSTOMERS_API),
        ]);

        const productsData = await productRes.json();
        setProducts(productsData);
        setFilteredProducts(productsData);

        const customersData = await customerRes.json();
        const formattedCustomers = customersData.map((c: any) => ({
          id: c.id,
          name: `${c.firstName} ${c.lastName}`,
        }));

        setCustomers(formattedCustomers);
        allCustomersRef.current = formattedCustomers;
      } catch (error) {
        console.error("Error loading data:", error);
      }
    };

    fetchInitialData();
  }, []);

  // Product search
  const handleSearch = (query: string) => {
    setSearchQuery(query);
    if (!query.trim()) setFilteredProducts(products);
    else {
      const lower = query.toLowerCase();
      setFilteredProducts(
        products.filter(
          (p) =>
            p.name.toLowerCase().includes(lower) ||
            p.sku.toLowerCase().includes(lower)
        )
      );
    }
  };

  // Cart management
  const addToCart = (product: Product) => {
    const existing = cart.find((item) => item.product.id === product.id);
    if (existing) {
      setCart(
        cart.map((item) =>
          item.product.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      setCart([...cart, { product, quantity: 1 }]);
    }
  };

  const updateQuantity = (productId: string, quantity: number) => {
    if (quantity <= 0) return;
    setCart(
      cart.map((item) =>
        item.product.id === productId ? { ...item, quantity } : item
      )
    );
  };

  const removeFromCart = (productId: string) => {
    setCart(cart.filter((item) => item.product.id !== productId));
  };

  const clearCart = () => setCart([]);

  // Calculations
  const totalAmount = cart.reduce(
    (sum, item) => sum + item.product.sellingPrice * item.quantity,
    0
  );
  const discount = totalAmount >= 100 ? totalAmount * 0.05 : 0; // 5% if total ≥ 100
  const tax = (totalAmount - discount) * 0.07; // 7%
  const finalTotal = totalAmount - discount + tax;

  // Sale submission
  const handleConfirmSale = async () => {
    if (!selectedCustomer || cart.length === 0) {
      alert("Please select a customer and add at least one product.");
      return;
    }

    if (!cashierId) {
      alert("Cashier ID not found. Please re-login or refresh.");
      return;
    }

    const salePayload = {
      customerId: selectedCustomer,
      userId: cashierId,
      paymentMethod,
      items: cart.map((item) => ({
        productId: item.product.id,
        quantity: item.quantity,
        unitPrice: item.product.sellingPrice,
      })),
    };

    setIsSubmitting(true);
    try {
      const res = await fetch(SALE_API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(salePayload),
      });

      if (!res.ok) throw new Error("Failed to create sale");

      const data = await res.json();
      setRecentSaleId(data.id);
      setIsSuccessDialogOpen(true);

      clearCart();
      setSelectedCustomer("");
      setCustomerSearch("");
      setPaymentMethod("CASH");
      setIsConfirmDialogOpen(false);
    } catch (err) {
      console.error("Error submitting sale:", err);
      alert("Failed to create sale. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  // Receipt download
  const handleDownloadReceipt = async () => {
    if (!recentSaleId) return;

    try {
      const pdfRes = await fetch(
        `http://localhost:8092/sales/${recentSaleId}/receipt/pdf`
      );

      if (!pdfRes.ok) throw new Error("Failed to download receipt");

      const blob = await pdfRes.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `Sale_Receipt_${recentSaleId}.pdf`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading receipt:", error);
      alert("Failed to download receipt. Please try again.");
    }
  };

  // Add new customer
  const handleCustomerInputChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setNewCustomer({ ...newCustomer, [e.target.name]: e.target.value });
  };

  const handleAddCustomer = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsCustomerSubmitting(true);

    try {
      const res = await fetch(REGISTER_CUSTOMER_API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newCustomer),
      });

      if (!res.ok) throw new Error("Failed to register customer");

      const data = await res.json();

      const added: Customer = {
        id: data.id || `TEMP-${Date.now()}`,
        name: `${newCustomer.firstName} ${newCustomer.lastName}`,
      };
      setCustomers((prev) => [...prev, added]);
      allCustomersRef.current = [...allCustomersRef.current, added];
      setSelectedCustomer(added.id);
      setCustomerSearch(added.name);

      alert("Customer added successfully!");
      setIsCustomerDialogOpen(false);
      setNewCustomer({ firstName: "", lastName: "", email: "", phone: "" });
    } catch (err) {
      console.error("Error adding customer:", err);
      alert("Failed to add customer. Please try again.");
    } finally {
      setIsCustomerSubmitting(false);
    }
  };

  // Customer filter
  const handleCustomerFilter = (query: string) => {
    setCustomerSearch(query);
    if (!query.trim()) {
      setCustomers(allCustomersRef.current);
      setShowCustomerSuggestions(false);
      return;
    }

    const lower = query.toLowerCase();
    const filtered = allCustomersRef.current.filter((c) =>
      c.name.toLowerCase().includes(lower)
    );
    setCustomers(filtered);
    setShowCustomerSuggestions(true);
  };

  return (
    <MainLayout>
      <div className="flex flex-col h-[85vh]">
        <h1 className="text-3xl font-semibold mb-4">Create Sale</h1>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 flex-1 overflow-hidden">
          {/* LEFT PANEL */}
          <div className="flex flex-col space-y-4 overflow-hidden h-full pr-2">
            <Card className="p-4 relative">
              <div className="flex justify-between items-center mb-2">
                <label className="font-medium">Customer</label>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setIsCustomerDialogOpen(true)}
                >
                  <Plus className="w-4 h-4 mr-1" /> Add New
                </Button>
              </div>

              <div className="relative">
                <input
                  type="text"
                  placeholder="Search customer by name"
                  value={customerSearch}
                  onChange={(e) => handleCustomerFilter(e.target.value)}
                  onFocus={() => {
                    if (customerSearch.trim().length > 0) {
                      setShowCustomerSuggestions(true);
                    }
                  }}
                  className="border border-gray-300 rounded-md p-2 w-full"
                />

                {showCustomerSuggestions &&
                  customerSearch.trim().length > 0 &&
                  customers.length > 0 && (
                    <ul className="absolute left-0 top-full bg-white border border-gray-300 rounded-md w-full mt-1 max-h-40 overflow-y-auto z-20 shadow-md">
                      {customers.map((c) => (
                        <li
                          key={c.id}
                          className="p-2 hover:bg-gray-100 cursor-pointer"
                          onClick={() => {
                            setSelectedCustomer(c.id);
                            setCustomerSearch(c.name);
                            setShowCustomerSuggestions(false);
                          }}
                        >
                          {c.name}
                        </li>
                      ))}
                    </ul>
                  )}
              </div>
            </Card>

            <Card className="p-4">
              <label className="font-medium mb-2 block">Payment Method</label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                className="border border-gray-300 rounded-md p-2 w-full"
              >
                <option value="CASH">Cash</option>
                <option value="CARD">Card</option>
                <option value="UPI">UPI</option>
              </select>
            </Card>

            {/* PRODUCTS */}
            <Card className="p-4 flex flex-col flex-1 overflow-hidden">
              <label className="font-medium mb-2 block">Search Product</label>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => handleSearch(e.target.value)}
                placeholder="Search by name or SKU"
                className="border border-gray-300 rounded-md p-2 w-full mb-3"
              />

              <div className="flex-1 overflow-y-auto space-y-2">
                {filteredProducts.map((p) => (
                  <Card
                    key={p.id}
                    className="cursor-pointer hover:shadow-md p-3"
                    onClick={() => addToCart(p)}
                  >
                    <div className="flex justify-between items-center">
                      <div>
                        <h3 className="font-medium">{p.name}</h3>
                        <p className="text-xs text-gray-500">{p.sku}</p>
                      </div>
                      <span className="font-semibold">${p.sellingPrice}</span>
                    </div>
                  </Card>
                ))}
                {filteredProducts.length === 0 && (
                  <p className="text-gray-500 text-sm text-center">
                    No products found.
                  </p>
                )}
              </div>
            </Card>
          </div>

          {/* RIGHT PANEL */}
          <div className="flex flex-col space-y-4 overflow-hidden h-full pl-2">
            <Card className="p-4 h-full flex flex-col justify-between">
              <div className="flex-1 overflow-y-auto mb-4">
                <h2 className="text-2xl font-semibold mb-4">Cart</h2>

                {cart.length === 0 ? (
                  <p className="text-gray-500 text-center mt-6">
                    Cart is empty.
                  </p>
                ) : (
                  <div className="space-y-3">
                    {cart.map((item) => (
                      <Card key={item.product.id}>
                        <CardContent className="flex justify-between items-center p-3">
                          <div>
                            <h3 className="font-medium">
                              {item.product.name}
                            </h3>
                            <p className="text-xs text-gray-500">
                              ${item.product.sellingPrice} × {item.quantity}
                            </p>
                          </div>
                          <div className="flex items-center gap-2">
                            <input
                              type="number"
                              min="1"
                              value={item.quantity}
                              onChange={(e) =>
                                updateQuantity(
                                  item.product.id,
                                  +e.target.value
                                )
                              }
                              className="border w-14 text-center rounded"
                            />
                            <Button
                              variant="destructive"
                              size="sm"
                              onClick={() =>
                                removeFromCart(item.product.id)
                              }
                            >
                              <Trash className="w-4 h-4" />
                            </Button>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}
              </div>

              {/* Footer with totals */}
              <div className="border-t pt-4 bg-white sticky bottom-0 space-y-2">
                <div className="flex justify-between text-sm">
                  <span>Subtotal:</span>
                  <span>${totalAmount.toFixed(2)}</span>
                </div>

                {totalAmount >= 100 && (
                  <div className="flex justify-between text-sm text-green-600">
                    <span>Discount (5%):</span>
                    <span>- ${discount.toFixed(2)}</span>
                  </div>
                )}

                {totalAmount > 0 && (
                  <div className="flex justify-between text-sm text-blue-600">
                    <span>Tax (7%):</span>
                    <span>+ ${tax.toFixed(2)}</span>
                  </div>
                )}

                <div className="flex justify-between text-lg font-semibold border-t pt-2">
                  <span>Final Total:</span>
                  <span>${finalTotal.toFixed(2)}</span>
                </div>

                <div className="flex justify-between mt-3">
                  <Button variant="outline" onClick={clearCart} disabled={!cart.length}>
                    Clear Cart
                  </Button>
                  <Button
                    onClick={() => setIsConfirmDialogOpen(true)}
                    disabled={!cart.length}
                  >
                    Checkout
                  </Button>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>

      {/* Confirm Sale Dialog */}
      <Dialog open={isConfirmDialogOpen} onOpenChange={setIsConfirmDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogTitle>Confirm Sale</DialogTitle>
          <p>
            Proceed with sale for total amount ${finalTotal.toFixed(2)} using{" "}
            <strong>{paymentMethod}</strong>?
          </p>
          <div className="flex justify-end gap-2 mt-4">
            <Button
              variant="outline"
              onClick={() => setIsConfirmDialogOpen(false)}
            >
              Cancel
            </Button>
            <Button onClick={handleConfirmSale} disabled={isSubmitting}>
              {isSubmitting ? "Processing..." : "Confirm"}
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      {/* ✅ Success Dialog */}
      <Dialog open={isSuccessDialogOpen} onOpenChange={setIsSuccessDialogOpen}>
        <DialogContent className="sm:max-w-md text-center space-y-4">
          <CheckCircle className="w-12 h-12 text-green-500 mx-auto" />
          <DialogTitle className="text-green-600 text-xl font-semibold">
            Sale Created Successfully!
          </DialogTitle>
          <p>Your sale has been recorded successfully.</p>
          <div className="flex justify-center gap-4 mt-4">
            <Button variant="outline" onClick={() => setIsSuccessDialogOpen(false)}>
              Close
            </Button>
            <Button onClick={handleDownloadReceipt}>
              Download Receipt
            </Button>
          </div>
        </DialogContent>
      </Dialog>

      {/* Add Customer Dialog */}
      <Dialog open={isCustomerDialogOpen} onOpenChange={setIsCustomerDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogTitle>Add New Customer</DialogTitle>
          <DialogDescription>Enter customer details below.</DialogDescription>
          <form onSubmit={handleAddCustomer} className="flex flex-col space-y-3 mt-3">
            <input
              name="firstName"
              placeholder="First Name"
              value={newCustomer.firstName}
              onChange={handleCustomerInputChange}
              required
              className="border p-2 rounded"
            />
            <input
              name="lastName"
              placeholder="Last Name"
              value={newCustomer.lastName}
              onChange={handleCustomerInputChange}
              required
              className="border p-2 rounded"
            />
            <input
              name="email"
              placeholder="Email"
              value={newCustomer.email}
              onChange={handleCustomerInputChange}
              type="email"
              required
              className="border p-2 rounded"
            />
            <input
              name="phone"
              placeholder="Phone"
              value={newCustomer.phone}
              onChange={handleCustomerInputChange}
              required
              className="border p-2 rounded"
            />
            <div className="flex justify-end gap-2 mt-2">
              <Button variant="outline" type="button" onClick={() => setIsCustomerDialogOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" disabled={isCustomerSubmitting}>
                {isCustomerSubmitting ? "Adding..." : "Add Customer"}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </MainLayout>
  );
};

export default Sales;
