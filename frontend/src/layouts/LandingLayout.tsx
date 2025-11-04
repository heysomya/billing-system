import type { ReactNode } from "react";
import LandingNavbar from "@/components/LandingNavbar";

interface LandingLayoutProps {
  children: ReactNode;
}

const LandingLayout = ({ children }: LandingLayoutProps) => {
  return (
    <div className="relative min-h-screen flex flex-col bg-cover bg-center bg-no-repeat text-white" style={{ backgroundImage: "url('/images/landing-bg.jpg')" }}>
      {/* Semi-transparent overlay */}
      <div className="absolute inset-0 bg-black/50 z-0" />

      {/* Main content */}
      <div className="relative z-10 flex flex-col min-h-screen">
        <LandingNavbar />

        <main className="grow flex flex-col items-center justify-center text-center px-6">
          {children}
        </main>

        <footer className="py-4 text-center text-gray-300 text-sm bg-transparent">
          © {new Date().getFullYear()} BillFast. All rights reserved.
        </footer>
      </div>
    </div>
  );
};

export default LandingLayout;
