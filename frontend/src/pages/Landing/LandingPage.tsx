import { useNavigate } from "react-router-dom";
import LandingLayout from "@/layouts/LandingLayout";

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <LandingLayout>
      <h1 className="text-5xl font-bold mb-4 drop-shadow-lg">Welcome to BillFast</h1>
      <p className="text-lg mb-6 max-w-xl leading-relaxed">
        Simplify product tracking, manage sales, and generate reports effortlessly.
      </p>
      <button
        className="px-6 py-3 bg-blue-600 font-semibold rounded-lg hover:bg-blue-700 transition"
        onClick={() => navigate("/dashboard")}
      >
        Go to Dashboard
      </button>
    </LandingLayout>
  );
}
