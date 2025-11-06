import { Routes, Route } from 'react-router-dom'
import LandingPage from '../pages/Landing/LandingPage'
import Dashboard from '@/pages/Dashboard/Dashboard'
import Products from '@/pages/Products/Products'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/products" element={<Products />} />
    </Routes>
  )
}