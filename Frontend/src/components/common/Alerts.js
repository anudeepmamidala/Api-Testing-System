import React from 'react';

// Loading spinner component
export function LoadingSpinner() {
  return (
    <div className="d-flex justify-content-center align-items-center" style={{ height: '200px' }}>
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Loading...</span>
      </div>
    </div>
  );
}

// Error alert component
export function ErrorAlert({ message, onClose }) {
  if (!message) return null;

  return (
    <div className="alert alert-danger alert-dismissible fade show" role="alert">
      <strong>Error:</strong> {message}
      <button
        type="button"
        className="btn-close"
        data-bs-dismiss="alert"
        onClick={onClose}
        aria-label="Close"
      ></button>
    </div>
  );
}

// Success alert component
export function SuccessAlert({ message, onClose }) {
  if (!message) return null;

  return (
    <div className="alert alert-success alert-dismissible fade show" role="alert">
      <strong>Success:</strong> {message}
      <button
        type="button"
        className="btn-close"
        data-bs-dismiss="alert"
        onClick={onClose}
        aria-label="Close"
      ></button>
    </div>
  );
}
