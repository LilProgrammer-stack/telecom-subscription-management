import { Route, Routes } from 'react-router'
import RegisterPage from './pages/RegisterPage'
import ConfirmInformationPage from './pages/ConfirmInformationPage'
import VerifyEmailPage from './pages/VerifyEmailPage'
function App() {
  return (
    <Routes>
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/register/confirm" element={<ConfirmInformationPage />} />
      <Route path="/register/verify-email" element={<VerifyEmailPage />} />
    </Routes>
  )
}

export default App