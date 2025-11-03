import LandingNavbar from '../../components/LandingNavbar'

export default function LandingPage() {
  return (
    <div
      className="relative min-h-screen flex flex-col bg-cover bg-center bg-no-repeat text-white"
      style={{ backgroundImage: "url('/images/landing-bg.jpg')" }}
    >
      <div className="absolute inset-0 bg-black/50 z-0" />

      <div className="relative z-10 flex flex-col min-h-screen">
        <LandingNavbar />

        <main className="grow flex flex-col items-center justify-center text-center px-4">
          <h1 className="text-5xl font-bold mb-4 drop-shadow-lg">
            Welcome to BillFast
          </h1>
          <p className="text-lg mb-6 max-w-xl leading-relaxed">
            Simplify product tracking, manage sales, and generate reports effortlessly.
          </p>
          <button className="px-6 py-3 bg-blue-600 font-semibold rounded-lg hover:bg-blue-700 transition">
            Go to Dashboard
          </button>
        </main>

        <footer className="py-4 text-center text-gray-300 text-sm bg-transparent">
          © {new Date().getFullYear()} BillFast. All rights reserved.
        </footer>
      </div>
    </div>
  )
}
