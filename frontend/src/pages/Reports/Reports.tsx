import { useState, useMemo } from "react";
import MainLayout from "@/layouts/MainLayout";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { BarChart3, CalendarRange, PieChart as PieChartIcon } from "lucide-react";
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  Legend,
  PieChart,
  Pie,
  Cell,
} from "recharts";

// 🧩 Types
type Product = {
  productId: string;
  name: string;
  sku: string;
  sellingPrice: number;
  quantityOnHand: number;
};

type SaleProduct = {
  product: Product;
  saleQuantity: number;
};

type SaleRecord = {
  saleId: string;
  totalAmt: number;
  saleDate: string;
  saleProducts: SaleProduct[];
};

type Granularity = "daily" | "weekly" | "monthly";

type AggregatedPoint = {
  label: string;
  totalAmount: number;
  saleCount: number;
};

// 🎨 Colors for Pie Chart
const COLORS = ["#2563eb", "#22c55e", "#eab308", "#ef4444", "#8b5cf6"];

const Reports = () => {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [granularity, setGranularity] = useState<Granularity>("daily");
  const [sales, setSales] = useState<SaleRecord[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [hasFetched, setHasFetched] = useState(false);

  // 🧭 Helpers for date formatting
  const toDateOnly = (d: Date) =>
    `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(
      d.getDate()
    ).padStart(2, "0")}`;

  const getWeekStart = (d: Date) => {
    const date = new Date(d);
    const diffToMonday = (date.getDay() + 6) % 7;
    date.setDate(date.getDate() - diffToMonday);
    return toDateOnly(date);
  };

  const getMonthLabel = (d: Date) =>
    `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`;

  // 🧩 Fetch Reports (real API)
  const fetchReports = async () => {
    setError(null);

    if (!startDate || !endDate) {
      setError("Please select both start and end dates.");
      return;
    }

    if (startDate > endDate) {
      setError("Start date cannot be after end date.");
      return;
    }

    setIsLoading(true);

    try {
      const url = `http://localhost:8094/reports?startDate=${startDate}&endDate=${endDate}`;
      const res = await fetch(url);
      if (!res.ok) throw new Error(`Failed to fetch reports: ${res.status}`);
      const rawData = await res.json();

      // 🔧 Transform response to match simplified types
      const mapped: SaleRecord[] = rawData.map((sale: any) => ({
        saleId: sale.saleId,
        totalAmt: sale.totalAmt,
        saleDate: sale.saleDate,
        saleProducts: sale.saleProducts.map((sp: any) => ({
          saleQuantity: sp.saleQuantity,
          product: {
            productId: sp.product.id,
            name: sp.product.name,
            sku: sp.product.sku,
            sellingPrice: sp.product.sellingPrice,
            quantityOnHand: sp.product.quantityOnHand,
          },
        })),
      }));

      setSales(mapped);
      setHasFetched(true);
    } catch (err: any) {
      console.error("Error fetching reports:", err);
      setError("Failed to fetch report data.");
    } finally {
      setIsLoading(false);
    }
  };

  // 🧮 Aggregate data
  const aggregatedData: AggregatedPoint[] = useMemo(() => {
    if (!startDate || !endDate) return [];

    const bucket: Record<string, AggregatedPoint> = {};

    for (const sale of sales) {
      const dateObj = new Date(sale.saleDate);
      let label: string;
      if (granularity === "daily") label = toDateOnly(dateObj);
      else if (granularity === "weekly") label = getWeekStart(dateObj);
      else label = getMonthLabel(dateObj);

      if (!bucket[label])
        bucket[label] = { label, totalAmount: 0, saleCount: 0 };

      bucket[label].totalAmount += sale.totalAmt;
      bucket[label].saleCount += 1;
    }

    // 🧩 Fill missing days/weeks/months
    const filled: AggregatedPoint[] = [];
    const start = new Date(startDate);
    const end = new Date(endDate);

    if (granularity === "daily") {
      for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
        const label = toDateOnly(d);
        filled.push(bucket[label] || { label, totalAmount: 0, saleCount: 0 });
      }
    } else if (granularity === "weekly") {
      for (let d = new Date(getWeekStart(start)); d <= end; d.setDate(d.getDate() + 7)) {
        const label = getWeekStart(d);
        filled.push(bucket[label] || { label, totalAmount: 0, saleCount: 0 });
      }
    } else {
      const d = new Date(start.getFullYear(), start.getMonth(), 1);
      const endMonth = new Date(end.getFullYear(), end.getMonth(), 1);

      while (d <= endMonth) {
        const label = getMonthLabel(d);
        filled.push(bucket[label] || { label, totalAmount: 0, saleCount: 0 });
        d.setMonth(d.getMonth() + 1);
      }
    }

    return filled;
  }, [sales, granularity, startDate, endDate]);

  // 🥧 Top products aggregation
  const productData = useMemo(() => {
    const productTotals: Record<string, { qty: number; product: Product }> = {};

    for (const sale of sales) {
      for (const sp of sale.saleProducts) {
        const key = sp.product.name;
        if (!productTotals[key]) {
          productTotals[key] = { qty: 0, product: sp.product };
        }
        productTotals[key].qty += sp.saleQuantity;
      }
    }

    return Object.values(productTotals)
      .map((item) => ({
        name: item.product.name,
        qty: item.qty,
        product: item.product,
      }))
      .sort((a, b) => b.qty - a.qty)
      .slice(0, 5);
  }, [sales]);

  const totalRevenue = useMemo(
    () => sales.reduce((sum, s) => sum + s.totalAmt, 0),
    [sales]
  );

  return (
    <MainLayout>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-semibold flex items-center gap-2">
          <BarChart3 className="w-7 h-7" /> Reports
        </h1>
      </div>

      {/* Filters */}
      <Card className="mb-6">
        <CardContent className="py-4">
          <div className="flex flex-wrap items-end gap-4">
            <div className="flex flex-col">
              <label className="mb-1 font-medium flex items-center gap-1">
                <CalendarRange className="w-4 h-4" /> Start Date
              </label>
              <input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="border border-gray-300 rounded-md p-2 min-w-[180px]"
              />
            </div>

            <div className="flex flex-col">
              <label className="mb-1 font-medium flex items-center gap-1">
                <CalendarRange className="w-4 h-4" /> End Date
              </label>
              <input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="border border-gray-300 rounded-md p-2 min-w-[180px]"
              />
            </div>

            <Button onClick={fetchReports} disabled={isLoading}>
              {isLoading ? "Loading..." : "Generate Report"}
            </Button>
          </div>
          {error && <p className="text-red-500 text-sm mt-3">{error}</p>}
        </CardContent>
      </Card>

      {hasFetched && (
        <>
          {/* Granularity Toggle */}
          <div className="flex justify-between items-center mb-4">
            <div className="inline-flex rounded-md shadow-sm border overflow-hidden">
              {(["daily", "weekly", "monthly"] as Granularity[]).map((g) => (
                <button
                  key={g}
                  onClick={() => setGranularity(g)}
                  className={`px-4 py-2 text-sm font-medium capitalize ${
                    granularity === g
                      ? "bg-blue-600 text-white"
                      : "bg-white text-gray-700 hover:bg-gray-50"
                  } ${g !== "monthly" ? "border-r" : ""}`}
                >
                  {g}
                </button>
              ))}
            </div>

            <Card className="min-w-[160px]">
              <CardContent className="py-3">
                <p className="text-xs text-gray-500">Total Revenue</p>
                <p className="text-lg font-semibold">${totalRevenue.toFixed(2)}</p>
              </CardContent>
            </Card>
          </div>

          {/* Chart 1: Number of Sales */}
          <Card className="mb-6">
            <CardContent className="pt-6">
              <h2 className="text-lg font-semibold mb-4">
                Number of Sales ({granularity})
              </h2>
              <div className="w-full h-[360px]">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={aggregatedData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="label" />
                    <YAxis
                      allowDecimals={false}
                      tickFormatter={(v) => Math.floor(v).toString()}
                      domain={[0, "dataMax + 1"]}
                    />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="saleCount" name="Sale Count" />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </CardContent>
          </Card>

          {/* Chart 2: Total Revenue */}
          <Card className="mb-6">
            <CardContent className="pt-6">
              <h2 className="text-lg font-semibold mb-4">
                Total Revenue ({granularity})
              </h2>
              <div className="w-full h-[360px]">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={aggregatedData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="label" />
                    <YAxis />
                    <Tooltip formatter={(v: any) => `$${v}`} />
                    <Legend />
                    <Bar dataKey="totalAmount" name="Revenue" />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </CardContent>
          </Card>

          {/* Chart 3: Top Products */}
          <Card>
            <CardContent className="pt-6">
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                <PieChartIcon className="w-5 h-5" /> Top Products Sold
              </h2>
              {productData.length === 0 ? (
                <p className="text-gray-500 text-center">
                  No product data available.
                </p>
              ) : (
                <div className="w-full h-[360px] flex justify-center">
                  <ResponsiveContainer width="60%" height="100%">
                    <PieChart>
                      <Pie
                        data={productData}
                        dataKey="qty"
                        nameKey="name"
                        label={(entry) => entry.name}
                      >
                        {productData.map((_, index) => (
                          <Cell key={index} fill={COLORS[index % COLORS.length]} />
                        ))}
                      </Pie>
                      <Tooltip
                        content={({ active, payload }) => {
                          if (!active || !payload || !payload.length) return null;
                          const item = payload[0] as any;
                          const value = item.value;
                          const product = item.payload.product as Product;

                          return (
                            <div className="bg-white border rounded-md p-2 text-xs shadow-md">
                              <div className="font-semibold mb-1">{product.name}</div>
                              <div>Qty Sold: {value}</div>
                              <div>Price: ${product.sellingPrice}</div>
                              <div>In Stock: {product.quantityOnHand}</div>
                              <div>SKU: {product.sku}</div>
                            </div>
                          );
                        }}
                      />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </div>
              )}
            </CardContent>
          </Card>
        </>
      )}
    </MainLayout>
  );
};

export default Reports;
