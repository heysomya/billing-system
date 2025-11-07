import { Card, CardContent } from "@/components/ui/card";

type Product = {
  id: string;
  name: string;
  sku: string;
  quantityOnHand: number;
  minStockLevel: number;
};

type Props = {
    lowStockProducts: Product[];
};

const LowStockCarousel = ({ lowStockProducts }: Props) => {
    if (lowStockProducts.length === 0) return null;
    
    return (
        <div className="mb-6">
      <h2 className="text-xl font-semibold mb-2 text-red-600">Low Stock Products</h2>
      <div className="flex space-x-4 overflow-x-auto py-2">
        {lowStockProducts.map(product => (
            <Card key={product.id} className="min-w-[200px] flex-shrink-0">
            <CardContent className="p-4 flex flex-col justify-between h-full">
              <div className="h-24 w-full mb-2">
                <img
                  src="/images/placeholder.png"
                  alt={product.name}
                  className="w-full h-full object-contain"
                  />
              </div>
              <h3 className="text-lg font-semibold">{product.name}</h3>
              <p className="text-sm text-gray-700">
                Stock: {product.quantityOnHand} / Min: {product.minStockLevel}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default LowStockCarousel;