export default function LandingNavbar() {
  return (
    <nav className="fixed top-0 left-0 w-full z-50 ">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center text-white">
        <h1 className="text-2xl font-bold tracking-wide">BillFast</h1>
        <div className="space-x-8 font-medium">
          <a href="#features" className="hover:text-gray-300 transition">Login</a>
        </div>
      </div>
    </nav>
  );
}
