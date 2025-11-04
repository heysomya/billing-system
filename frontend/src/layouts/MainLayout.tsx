import type { ReactNode } from "react";
import InternalNavbar from "@/components/InternalNavbar";

interface MainLayoutProps {
  children: ReactNode;
}

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <div className="flex flex-col min-h-screen bg-gray-50 dark:bg-gray-900">
      {/* Navbar */}
      <InternalNavbar />

      {/* Main content */}
      <main className="flex-grow max-w-7xl mx-auto px-6 py-8 w-full">
        {children}
      </main>

      {/* Footer */}
      <footer className="py-4 text-center text-gray-400 text-sm border-t border-gray-200 dark:border-gray-700">
        © {new Date().getFullYear()} BillFast. All rights reserved.
      </footer>
    </div>
  );
};

export default MainLayout;
