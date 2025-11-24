import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import ReportForm from './components/ReportForm';
import ReportList from './components/ReportList';
import MyReports from './components/MyReports';
import Search from './components/Search';
import { AuthProvider, useAuth } from './context/AuthContext';
import './App.css';

function PrivateRoute({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" />;
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/reports" element={<ReportList />} />
      <Route path="/search" element={<Search />} />
      <Route 
        path="/submit-report" 
        element={
          <PrivateRoute>
            <ReportForm />
          </PrivateRoute>
        } 
      />
      <Route 
        path="/my-reports" 
        element={
          <PrivateRoute>
            <MyReports />
          </PrivateRoute>
        } 
      />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Navbar />
          <div className="container-fluid mt-4">
            <AppRoutes />
          </div>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;

