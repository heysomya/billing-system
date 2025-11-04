import { Routes, Route } from 'react-router-dom'
import LandingPage from '../pages/Landing/LandingPage'
import Dashboard from '@/pages/Dashboard/Dashboard'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/dashboard" element={<Dashboard />} />
    </Routes>
  )
}