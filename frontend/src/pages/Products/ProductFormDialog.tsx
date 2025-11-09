import {
  Dialog,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { useState, useEffect } from "react";

export type FormData = {
  name: string;
  sku: string;
  description: string;
  costPrice: string;
  sellingPrice: string;
  quantityOnHand: string;
  minStockLevel: string;
};

type ProductFormDialogProps = {
  mode: "add" | "edit";
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSubmit: (data: FormData) => void;
  initialData?: FormData;
};

const emptyForm: FormData = {
  name: "",
  sku: "",
  description: "",
  costPrice: "",
  sellingPrice: "",
  quantityOnHand: "",
  minStockLevel: "",
};

const ProductFormDialog = ({
  mode,
  open,
  onOpenChange,
  onSubmit,
  initialData,
}: ProductFormDialogProps) => {
  const [formData, setFormData] = useState<FormData>(emptyForm);

  // Reset form when dialog opens/closes or mode changes
  useEffect(() => {
    if (open) {
      setFormData(initialData || emptyForm);
    } else {
      setFormData(emptyForm);
    }
  }, [open, initialData]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-lg">
        <DialogTitle>
          {mode === "add" ? "Add New Product" : "Edit Product"}
        </DialogTitle>
        <DialogDescription>
          {mode === "add"
            ? "Fill in the product details below."
            : "Update the product details below."}
        </DialogDescription>
        <form onSubmit={handleSubmit} className="mt-4 flex flex-col space-y-4">
          <div className="flex flex-col">
            <label className="mb-1 font-medium">Name</label>
            <input
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="border p-2 rounded"
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">SKU</label>
            <input
              name="sku"
              value={formData.sku}
              onChange={handleChange}
              required
              className="border p-2 rounded"
              disabled={mode === "edit"}
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">Description</label>
            <input
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="border p-2 rounded"
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">Cost Price</label>
            <input
              name="costPrice"
              value={formData.costPrice}
              onChange={handleChange}
              type="number"
              step="0.01"
              required
              className="border p-2 rounded"
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">Selling Price</label>
            <input
              name="sellingPrice"
              value={formData.sellingPrice}
              onChange={handleChange}
              type="number"
              step="0.01"
              required
              className="border p-2 rounded"
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">Quantity</label>
            <input
              name="quantityOnHand"
              value={formData.quantityOnHand}
              onChange={handleChange}
              type="number"
              required
              className="border p-2 rounded"
              disabled={mode === "edit"}
            />
          </div>

          <div className="flex flex-col">
            <label className="mb-1 font-medium">Minimum Stock Level</label>
            <input
              name="minStockLevel"
              value={formData.minStockLevel}
              onChange={handleChange}
              type="number"
              required
              className="border p-2 rounded"
            />
          </div>

          <div className="flex justify-end space-x-2 mt-2">
            <Button
              variant="outline"
              type="button"
              onClick={() => onOpenChange(false)}
            >
              Cancel
            </Button>
            <Button type="submit">{mode === "add" ? "Add" : "Save"}</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default ProductFormDialog;
