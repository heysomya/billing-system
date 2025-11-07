import { Routes, Route } from 'react-router-dom'
import LandingPage from '../pages/Landing/LandingPage'
import Dashboard from '@/pages/Dashboard/Dashboard'
import Products from '@/pages/Products/Products'
import StockManagement from '@/pages/StockManagement/StockManagement'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/products" element={<Products />} />
      <Route path="/stock" element={<StockManagement />} />
    </Routes>
  )
}