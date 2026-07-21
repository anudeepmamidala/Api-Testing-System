import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FiLogOut } from 'react-icons/fi';

export default function Navbar({ user }) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div className="container-fluid">
        {/* Brand */}
        <span className="navbar-brand mb-0 h1">🚀 ProbeAPI</span>

        {/* Spacer */}
        <span className="ms-auto"></span>

        {/* User info and logout */}
        <div className="d-flex align-items-center gap-3">
          <span className="text-white">👤 {user?.username}</span>
          <button
            className="btn btn-danger btn-sm"
            onClick={handleLogout}
            title="Logout"
          >
            <FiLogOut /> Logout
          </button>
        </div>
      </div>
    </nav>
  );
}
