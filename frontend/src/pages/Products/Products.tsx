import { useEffect, useState } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Plus, Edit, Trash } from "lucide-react";
import { v4 as uuidv4 } from "uuid";

type Product = {
  name: string;
  sku: string;
  minStockLevel: number;
  description: string;
  costPrice: number;
  sellingPrice: number;
  quantityOnHand: number;
};

const Products = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isDetailOpen, setIsDetailOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    name: "",
    sku: "",
    description: "",
    costPrice: "",
    sellingPrice: "",
    quantityOnHand: "",
    minStockLevel: "",
  });

  const API_BASE = "http://localhost:8090/api/products";

  // Fetch all products
  const fetchProducts = async () => {
    try {
      const res = await fetch(`${API_BASE}/get/all`);
      if (!res.ok) throw new Error("Failed to fetch products");
      const data = await res.json();

      // Map API response to only needed fields
      const simplified = data.map((p: any) => ({
        name: p.name,
        sku: p.sku,
        minStockLevel: p.minStockLevel,
        description: p.description,
        costPrice: p.costPrice,
        sellingPrice: p.sellingPrice,
        quantityOnHand: p.quantityOnHand,
      }));

      setProducts(simplified);
    } catch (err) {
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // Handle form input change
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Handle add product
  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();

    const newProduct = {
      name: formData.name,
      sku: formData.sku,
      minStockLevel: Number(formData.minStockLevel),
      description: formData.description,
      costPrice: parseFloat(formData.costPrice),
      sellingPrice: parseFloat(formData.sellingPrice),
      quantityOnHand: Number(formData.quantityOnHand),
    };

    try {
      const res = await fetch(`${API_BASE}/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newProduct),
      });

      if (!res.ok) throw new Error("Failed to create product");

      // Refresh products
      await fetchProducts();

      // Reset form and close modal
      setIsAddOpen(false);
      setFormData({
        name: "",
        sku: "",
        description: "",
        costPrice: "",
        sellingPrice: "",
        quantityOnHand: "",
        minStockLevel: "",
      });
    } catch (err) {
      console.error("Error creating product:", err);
    }
  };

  // Open product details modal
  const openProductDetails = (product: Product) => {
    setSelectedProduct(product);
    setIsDetailOpen(true);
  };

  return (
    <MainLayout>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-semibold">Products</h1>
        <Button
          onClick={() => setIsAddOpen(true)}
          className="flex items-center space-x-2"
        >
          <Plus className="w-4 h-4" />
          <span>Add Product</span>
        </Button>
      </div>

      {loading ? (
        <p className="text-gray-500 text-center mt-10">Loading products...</p>
      ) : products.length === 0 ? (
        <p className="text-gray-500 text-center mt-10">No products found.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {products.map((product, index) => (
            <Card
              key={index}
              className="hover:shadow-lg cursor-pointer"
              onClick={() => openProductDetails(product)}
            >
              <CardHeader className="text-center">
                <CardTitle className="text-lg font-semibold">
                  {product.name}
                </CardTitle>
              </CardHeader>
              <CardContent className="text-sm text-gray-600">
                <p>SKU: {product.sku}</p>
                <p>Cost Price: ${product.costPrice}</p>
                <p>Selling Price: ${product.sellingPrice}</p>
                <p>Stock: {product.quantityOnHand}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Add Product Dialog */}
      <Dialog open={isAddOpen} onOpenChange={setIsAddOpen}>
        <DialogContent className="sm:max-w-lg">
          <DialogTitle>Add New Product</DialogTitle>
          <DialogDescription>
            Fill in the product details below.
          </DialogDescription>

          <form
            onSubmit={handleAddProduct}
            className="mt-4 flex flex-col space-y-4"
          >
            <input
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Name"
              required
              className="border p-2 rounded"
            />
            <input
              name="sku"
              value={formData.sku}
              onChange={handleChange}
              placeholder="SKU"
              required
              className="border p-2 rounded"
            />
            <input
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Description"
              className="border p-2 rounded"
            />
            <input
              name="costPrice"
              value={formData.costPrice}
              onChange={handleChange}
              type="number"
              step="0.01"
              placeholder="Cost Price"
              required
              className="border p-2 rounded"
            />
            <input
              name="sellingPrice"
              value={formData.sellingPrice}
              onChange={handleChange}
              type="number"
              step="0.01"
              placeholder="Selling Price"
              required
              className="border p-2 rounded"
            />
            <input
              name="quantityOnHand"
              value={formData.quantityOnHand}
              onChange={handleChange}
              type="number"
              placeholder="Quantity"
              required
              className="border p-2 rounded"
            />
            <input
              name="minStockLevel"
              value={formData.minStockLevel}
              onChange={handleChange}
              type="number"
              placeholder="Minimum Stock Level"
              required
              className="border p-2 rounded"
            />

            <div className="flex justify-end space-x-2 mt-2">
              <Button
                variant="outline"
                type="button"
                onClick={() => setIsAddOpen(false)}
              >
                Cancel
              </Button>
              <Button type="submit">Add</Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>

      {/* Product Details Dialog */}
      {selectedProduct && (
        <Dialog open={isDetailOpen} onOpenChange={() => setIsDetailOpen(false)}>
          <DialogContent className="sm:max-w-lg">
            <DialogTitle>{selectedProduct.name}</DialogTitle>
            <DialogDescription>Product details and actions</DialogDescription>

            <div className="mt-4 space-y-2 text-gray-700">
              <p>
                <strong>SKU:</strong> {selectedProduct.sku}
              </p>
              <p>
                <strong>Description:</strong> {selectedProduct.description}
              </p>
              <p>
                <strong>Cost Price:</strong> ${selectedProduct.costPrice}
              </p>
              <p>
                <strong>Selling Price:</strong> ${selectedProduct.sellingPrice}
              </p>
              <p>
                <strong>Stock:</strong> {selectedProduct.quantityOnHand}
              </p>
              <p>
                <strong>Min Stock Level:</strong>{" "}
                {selectedProduct.minStockLevel}
              </p>
            </div>

            <div className="flex justify-end space-x-2 mt-4">
              <Button
                variant="destructive"
                className="flex items-center space-x-2"
              >
                <Trash className="w-4 h-4" />
                <span>Delete</span>
              </Button>
              <Button variant="default" className="flex items-center space-x-2">
                <Edit className="w-4 h-4" />
                <span>Edit</span>
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      )}
    </MainLayout>
  );
};

export default Products;
