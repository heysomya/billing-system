import { useEffect, useState } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Plus, Edit, Trash } from "lucide-react";
import ProductFormDialog, { type FormData } from "./ProductFormDialog";
import { Dialog, DialogContent, DialogTitle, DialogDescription } from "@/components/ui/dialog";

type Product = {
  id: string;
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
  const [detailsProduct, setDetailsProduct] = useState<Product | null>(null);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [editingProductId, setEditingProductId] = useState<string | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [formMode, setFormMode] = useState<"add" | "edit">("add");

  const API_BASE = "http://localhost:8090/api/products";

  // Fetch products
  const fetchProducts = async () => {
    try {
      const res = await fetch(`${API_BASE}/get/all`);
      if (!res.ok) throw new Error("Failed to fetch products");
      const data = await res.json();
      const simplified = data.map((p: any) => ({
        id: p.id,
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
      console.error(err);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // Open Add Product Modal
  const handleAdd = () => {
    setFormMode("add");
    setSelectedProduct(null);
    setEditingProductId(null);
    setIsFormOpen(true);
  };

  // Open Edit Product Modal
  const handleEdit = (product: Product) => {
    setFormMode("edit");
    setSelectedProduct(product);
    setEditingProductId(product.id);
    setDetailsProduct(null);
    setIsFormOpen(true);
  };

  // Submit form (add or edit)
  const handleFormSubmit = async (data: FormData) => {
    const body = {
      ...data,
      costPrice: parseFloat(data.costPrice),
      sellingPrice: parseFloat(data.sellingPrice),
      quantityOnHand: Number(data.quantityOnHand),
      minStockLevel: Number(data.minStockLevel),
    };

    try {
      if (formMode === "add") {
        await fetch(`${API_BASE}/create`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body),
        });
      } else if (formMode === "edit" && editingProductId) {
        await fetch(`${API_BASE}/update/${editingProductId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body),
        });
      }

      setIsFormOpen(false);
      setSelectedProduct(null);
      setEditingProductId(null);
      await fetchProducts();
    } catch (err) {
      console.error("Error submitting product:", err);
    }
  };

  // Delete product
  const handleDelete = async (productId: string) => {
    if (!confirm("Are you sure you want to delete this product?")) return;

    try {
      const res = await fetch(`${API_BASE}/delete/${productId}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to delete product");

      setDetailsProduct(null); // Close details dialog
      await fetchProducts(); // Refresh list
    } catch (err) {
      console.error("Error deleting product:", err);
    }
  };

  const openProductDetails = (product: Product) => {
    setDetailsProduct(product);
  };

  return (
    <MainLayout>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-semibold">Products</h1>
        <Button onClick={handleAdd} className="flex items-center space-x-2">
          <Plus className="w-4 h-4" />
          <span>Add Product</span>
        </Button>
      </div>

      {products.length === 0 ? (
        <p className="text-gray-500 text-center mt-10">No products found.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {products.map((product) => (
            <Card
              key={product.id}
              className="hover:shadow-lg cursor-pointer overflow-hidden"
              onClick={() => openProductDetails(product)}
            >
              {/* Placeholder Image */}
              <div className="h-40 w-full overflow-hidden">
                <img
                  src={"/images/placeholder.png"}
                  alt={product.name}
                  className="w-full h-full object-contain"
                />
              </div>

              {/* Product Info */}
              <CardContent className="p-4">
                <div className="flex justify-between items-center">
                  <h3 className="text-lg font-semibold">{product.name}</h3>
                  <span className="text-gray-700 font-medium">${product.sellingPrice}</span>
                </div>
              </CardContent>
            </Card>
          ))}
      </div>

      )}

      {/* Product Details Dialog */}
      {detailsProduct && (
        <Dialog open={!!detailsProduct} onOpenChange={() => setDetailsProduct(null)}>
          <DialogContent className="sm:max-w-lg">
            {/* Product Image */}
            <div className="h-48 w-full overflow-hidden rounded-md mb-4">
              <img
                src={"/images/placeholder.png"}
                alt={detailsProduct.name}
                className="w-full h-full object-cover"
              />
            </div>

            <DialogTitle>{detailsProduct.name}</DialogTitle>
            <DialogDescription>{detailsProduct.description}</DialogDescription>

            <div className="mt-4 space-y-2 text-gray-700">
              <p><strong>SKU:</strong> {detailsProduct.sku}</p>
              <p><strong>Cost Price:</strong> ${detailsProduct.costPrice}</p>
              <p><strong>Selling Price:</strong> ${detailsProduct.sellingPrice}</p>
              <p><strong>Stock:</strong> {detailsProduct.quantityOnHand}</p>
              <p><strong>Min Stock Level:</strong> {detailsProduct.minStockLevel}</p>
            </div>

            <div className="flex justify-end space-x-2 mt-4">
              <Button
                variant="destructive"
                className="flex items-center space-x-2"
                onClick={() => detailsProduct && handleDelete(detailsProduct.id)}
              >
                <Trash className="w-4 h-4" />
                <span>Delete</span>
              </Button>

              <Button
                variant="default"
                className="flex items-center space-x-2"
                onClick={() => detailsProduct && handleEdit(detailsProduct)}
              >
                <Edit className="w-4 h-4" />
                <span>Edit</span>
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      )}

      {/* Add/Edit Product Form Dialog */}
      <ProductFormDialog
        mode={formMode}
        open={isFormOpen}
        onOpenChange={setIsFormOpen}
        onSubmit={handleFormSubmit}
        initialData={
          formMode === "edit" && selectedProduct
            ? {
                name: selectedProduct.name,
                sku: selectedProduct.sku,
                description: selectedProduct.description,
                costPrice: selectedProduct.costPrice.toString(),
                sellingPrice: selectedProduct.sellingPrice.toString(),
                quantityOnHand: selectedProduct.quantityOnHand.toString(),
                minStockLevel: selectedProduct.minStockLevel.toString(),
              }
            : undefined
        }
      />
    </MainLayout>
  );
};

export default Products;
