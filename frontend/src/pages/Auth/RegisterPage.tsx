import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "", role: "CASHIER" });
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await fetch("http://localhost:8093/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      if (!response.ok) throw new Error("Registration failed");

      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);

      navigate("/dashboard");
    } catch (err: any) {
      setError(err.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className="relative min-h-screen flex items-center justify-center bg-cover bg-center bg-no-repeat text-white"
      style={{ backgroundImage: "url('/images/landing-bg.jpg')" }}
    >
      {/* Dark overlay */}
      <div className="absolute inset-0 bg-black/50 z-0" />

      <div className="relative z-10 w-full max-w-md px-6">
        <Card className="w-full bg-white/10 backdrop-blur-md text-white border border-white/20 shadow-xl">
          <CardHeader>
            <CardTitle className="text-2xl font-semibold text-center">Register</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input
                type="text"
                name="username"
                placeholder="Username"
                value={form.username}
                onChange={handleChange}
                required
                className="bg-white/20 text-white placeholder:text-white/70 border-white/30"
              />
              <Input
                type="password"
                name="password"
                placeholder="Password"
                value={form.password}
                onChange={handleChange}
                required
                className="bg-white/20 text-white placeholder:text-white/70 border-white/30"
              />

              {/* Role Dropdown */}
              <select
                name="role"
                value={form.role}
                onChange={handleChange}
                className="w-full bg-white/20 text-white border border-white/30 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
              >
                <option value="ADMIN" className="text-black">Admin</option>
                <option value="CASHIER" className="text-black">Cashier</option>
              </select>

              {error && <p className="text-red-400 text-sm text-center">{error}</p>}

              <Button
                type="submit"
                className="w-full bg-blue-600 hover:bg-blue-700 transition"
                disabled={loading}
              >
                {loading ? "Registering..." : "Register"}
              </Button>
            </form>

            <p className="mt-4 text-center text-sm text-gray-200">
              Already have an account?{" "}
              <a href="/login" className="text-blue-400 hover:underline">
                Login
              </a>
            </p>

            <div className="mt-6 text-center">
              <button
                onClick={() => navigate("/")}
                className="text-sm text-gray-300 hover:text-white transition"
              >
                ← Back to Homepage
              </button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
