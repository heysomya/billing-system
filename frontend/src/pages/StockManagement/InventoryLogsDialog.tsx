import { Dialog, DialogContent, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { useEffect, useState } from "react";

export type Product = {
  id: string;
  name: string;
  sku: string;
};

export type InventoryLog = {
  id: string;
  quantityChange: number;
  reason: string;
  createdAt: string;
};

type Props = {
  product: Product | null;
  open: boolean;
  onClose: () => void;
};

const InventoryLogsDialog = ({ product, open, onClose }: Props) => {
  const [logs, setLogs] = useState<InventoryLog[]>([]);

  useEffect(() => {
    if (product && open) {
      setLogs([]);
      fetchLogs(product.id);
    }

  }, [product, open]);

  const fetchLogs = async (productId: string) => {
    try {
      const res = await fetch(`http://localhost:8091/stock/logs/${productId}`);
      if (!res.ok) throw new Error("Failed to fetch inventory logs");
      const data = await res.json();
      setLogs(data);
    } catch (err) {
      console.error(err);
      setLogs([]);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-2xl">
        <DialogTitle>Inventory Logs - {product?.name}</DialogTitle>
        <DialogDescription className="mb-4">
          Showing recent stock updates
        </DialogDescription>

        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-sm font-medium text-gray-500">Quantity Change</th>
                <th className="px-4 py-2 text-left text-sm font-medium text-gray-500">Reason</th>
                <th className="px-4 py-2 text-left text-sm font-medium text-gray-500">Date/Time</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {logs.length === 0 ? (
                <tr>
                  <td colSpan={3} className="text-center py-4 text-gray-500">
                    No logs found.
                  </td>
                </tr>
              ) : (
                logs.map((log) => (
                  <tr key={log.id}>
                    <td className="px-4 py-2">{log.quantityChange}</td>
                    <td className="px-4 py-2">{log.reason}</td>
                    <td className="px-4 py-2">{new Date(log.createdAt).toLocaleString()}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="flex justify-end mt-4">
          <Button onClick={onClose}>Close</Button>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default InventoryLogsDialog;