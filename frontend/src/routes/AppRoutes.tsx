import { Routes, Route } from 'react-router-dom'
import LandingPage from '../pages/Landing/LandingPage'
import Dashboard from '@/pages/Dashboard/Dashboard'
import Products from '@/pages/Products/Products'
import StockManagement from '@/pages/StockManagement/StockManagement'
import LoginPage from '@/pages/Auth/LoginPage'
import RegisterPage from '@/pages/Auth/RegisterPage'
import ProtectedRoute from '@/components/ProtectedRoute'
import PublicRoute from '@/components/PublicRoute'
import RoleProtectedRoute from '@/components/RoleProtectedRoute'
import Reports from '@/pages/Reports/Reports'
import Sales from '@/pages/Sales/Sales'
import Users from '@/pages/Users/Users'


export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/products" element={<ProtectedRoute><Products /></ProtectedRoute>} />
      <Route path="/stock" element={<RoleProtectedRoute allowedRoles={["ADMIN"]}><StockManagement /></RoleProtectedRoute>} />
      <Route path="/reports" element={<RoleProtectedRoute allowedRoles={["ADMIN"]}><Reports /></RoleProtectedRoute>} />
      <Route path="/sales" element={<RoleProtectedRoute allowedRoles={["CASHIER"]}><Sales /></RoleProtectedRoute>} />
      <Route path="/users" element={<RoleProtectedRoute allowedRoles={["ADMIN"]}><Users /></RoleProtectedRoute>} />
    </Routes>
  )
}