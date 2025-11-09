import { useState, useEffect } from "react";
import { Dialog, DialogContent, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

type UpdateStockDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  productId: string | null;
  onSubmit: (updatedQuantity: number, reason: string) => void;
};

const UpdateStockDialog = ({
  open,
  onOpenChange,
  productId,
  onSubmit,
}: UpdateStockDialogProps) => {
  const [quantity, setQuantity] = useState("");
  const [reason, setReason] = useState("");

  useEffect(() => {
    if (!open) {
      setQuantity("");
      setReason("");
    }
  }, [open]);

  const handleSubmit = () => {
    if (!quantity || !reason) return;
    onSubmit(Number(quantity), reason);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogTitle>Update Stock</DialogTitle>

        <div className="flex flex-col space-y-4 mt-4">
          <input
            type="number"
            placeholder="Updated Quantity"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            className="border border-gray-300 rounded-md p-2"
          />
          <input
            type="text"
            placeholder="Reason for Update"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            className="border border-gray-300 rounded-md p-2"
          />
        </div>

        <div className="flex justify-end mt-6 space-x-2">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancel
          </Button>
          <Button onClick={handleSubmit}>Update</Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateStockDialog;
