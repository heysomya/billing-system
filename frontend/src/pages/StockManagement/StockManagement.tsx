import { useState } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import UpdateStockDialog from "./UpdateStockDialog";
import InventoryLogsDialog from "./InventoryLogsDialog";


type Product = {
  id: string;
  name: string;
  sku: string;
  quantityOnHand: number;
};

const StockManagement = () => {
  const [products, setProducts] = useState<Product[] | null>(null);
  const [searchType, setSearchType] = useState<"name" | "SKU">("name");
  const [searchQuery, setSearchQuery] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const [updateStockProduct, setUpdateStockProduct] = useState<Product | null>(
    null
  );
  const [selectedStockProduct, setSelectedStockProduct] = useState<Product | null>(null);
  const [isLogsOpen, setIsLogsOpen] = useState(false);

  const API_BASE = "http://localhost:8090/api/products";

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      setProducts(null); // reset to initial state
      return;
    }

    setIsSearching(true);
    try {
      const response = await fetch(
        `${API_BASE}/search/${searchType}/${encodeURIComponent(searchQuery)}`
      );

      if (!response.ok) throw new Error("Search failed");

      const result = await response.json();

      if (Array.isArray(result)) {
        setProducts(result.length > 0 ? result : []);
      } else if (result && Object.keys(result).length > 0) {
        setProducts([result]);
      } else {
        setProducts([]); // No results
      }
    } catch (error) {
      console.error("Search error:", error);
      setProducts([]);
    } finally {
      setIsSearching(false);
    }
  };

  const handleClearSearch = () => {
    setSearchQuery("");
    setProducts(null); // reset to initial state
  };

  const handleUpdateStock = (product: Product) => {
    setUpdateStockProduct(product);
  };

  const handleSubmitUpdateStock = async (
    updatedQuantity: number,
    reason: string
  ) => {
    if (!updateStockProduct) return;

    try {
      const res = await fetch(
        `http://localhost:8091/stock/update/${
          updateStockProduct.id
        }?updatedQuantity=${updatedQuantity}&reason=${encodeURIComponent(
          reason
        )}`,
        {
          method: "POST",
        }
      );
      if (!res.ok) throw new Error("Failed to update stock");

      setUpdateStockProduct(null);
      alert("Stock updated successfully!");
      // Optionally refresh products
      await handleSearch();
    } catch (err) {
      console.error(err);
      alert("Error updating stock");
    }
  };

  const handleCheckInventory = (product: Product) => {
    setSelectedStockProduct(product);
    setIsLogsOpen(true);
  };

  return (
    <MainLayout>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-semibold">Stock Management</h1>
      </div>

      {/* Search Section */}
      <div className="flex flex-wrap items-center justify-between mb-6 gap-3">
        <div className="flex items-center flex-wrap gap-2">
          <select
            value={searchType}
            onChange={(e) => setSearchType(e.target.value as "name" | "SKU")}
            className="border border-gray-300 rounded-md p-2"
          >
            <option value="name">Search by Name</option>
            <option value="SKU">Search by SKU</option>
          </select>

          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            placeholder={`Enter ${searchType}`}
            className="border border-gray-300 rounded-md p-2 w-48 sm:w-64"
          />

          <Button onClick={handleSearch} disabled={isSearching}>
            {isSearching ? "Searching..." : "Search"}
          </Button>

          {searchQuery && (
            <Button variant="outline" onClick={handleClearSearch}>
              Clear
            </Button>
          )}
        </div>
      </div>

      {/* Product Stock Grid */}
      {products === null ? (
        <p className="text-gray-500 text-center mt-10">
          Please search for a product to see stock.
        </p>
      ) : products.length === 0 ? (
        <p className="text-gray-500 text-center mt-10">No products found.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {products.map((product) => (
            <Card
              key={product.id}
              className="hover:shadow-lg cursor-pointer overflow-hidden"
            >
              {/* Placeholder Image */}
              <div className="h-40 w-full overflow-hidden">
                <img
                  src={"/images/placeholder.png"}
                  alt={product.name}
                  className="w-full h-full object-contain"
                />
              </div>
              <CardContent className="p-4 flex flex-col justify-between">
                <div className="flex justify-between items-center mb-2">
                  <h3 className="text-lg font-semibold">{product.name}</h3>
                  <span className="text-gray-700 font-medium">
                    Stock: {product.quantityOnHand}
                  </span>
                </div>

                <div className="flex justify-between mt-2">
                  <Button
                    size="sm"
                    variant="default"
                    onClick={() => handleUpdateStock(product)}
                  >
                    Update Stock
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleCheckInventory(product)}
                  >
                    Check Inventory Log
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
      <UpdateStockDialog
        open={!!updateStockProduct}
        onOpenChange={setUpdateStockProduct as any}
        productId={updateStockProduct?.id || null}
        onSubmit={handleSubmitUpdateStock}
      />
      <InventoryLogsDialog
        product={selectedStockProduct}
        open={isLogsOpen}
        onClose={() => {
         setIsLogsOpen(false);

        }}
      />
    </MainLayout>
  );
};

export default StockManagement;
