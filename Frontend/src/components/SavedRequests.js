import React, { useState, useEffect } from 'react';
import { savedRequestsAPI, collectionsAPI } from '../services/api';
import { LoadingSpinner, ErrorAlert, SuccessAlert } from './common/Alerts';
import { FiEdit2, FiTrash2, FiCopy } from 'react-icons/fi';

export default function SavedRequests() {
  // State
  const [requests, setRequests] = useState([]);
  const [collections, setCollections] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  
  // Form state
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    method: 'GET',
    url: '',
    headers: '{}',
    body: '',
    collectionId: '',
  });

  // Fetch saved requests and collections
  useEffect(() => {
    fetchRequests();
    fetchCollections();
  }, []);

  const fetchCollections = async () => {
    try {
      const response = await collectionsAPI.getAll();
      setCollections(response.data || []);
    } catch (err) {
      console.log('Could not load collections');
    }
  };

  const fetchRequests = async () => {
    try {
      setLoading(true);
      setError('');
      const response = await savedRequestsAPI.getAll();
      setRequests(response.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load saved requests');
    } finally {
      setLoading(false);
    }
  };

  // Handle form change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Create or update request
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.name.trim() || !formData.url.trim()) {
      setError('Name and URL are required');
      return;
    }

    try {
      setLoading(true);
      setError('');
      
      // Parse headers from JSON string to object
      let parsedHeaders = {};
      try {
        parsedHeaders = JSON.parse(formData.headers || '{}');
      } catch (err) {
        setError('Invalid JSON in headers field');
        setLoading(false);
        return;
      }
      
      if (editingId) {
        // Update
        await savedRequestsAPI.update(
          editingId,
          formData.name,
          formData.description,
          formData.method,
          formData.url,
          parsedHeaders,
          formData.body,
          formData.collectionId ? parseInt(formData.collectionId) : null
        );
        setSuccess('Request updated successfully');
      } else {
        // Create
        await savedRequestsAPI.create(
          formData.name,
          formData.description,
          formData.method,
          formData.url,
          parsedHeaders,
          formData.body,
          formData.collectionId ? parseInt(formData.collectionId) : null
        );
        setSuccess('Request saved successfully');
      }

      resetForm();
      fetchRequests();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save request');
    } finally {
      setLoading(false);
    }
  };

  // Delete request
  const handleDelete = async (id) => {
    if (!window.confirm('Delete this saved request?')) return;

    try {
      setLoading(true);
      await savedRequestsAPI.delete(id);
      setSuccess('Request deleted');
      fetchRequests();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete request');
    } finally {
      setLoading(false);
    }
  };

  // Edit request
  const handleEdit = (request) => {
    setFormData({
      name: request.name,
      description: request.description || '',
      method: request.method,
      url: request.url,
      headers: request.headers || '{}',
      body: request.body || '',
      collectionId: request.collectionId || '',
    });
    setEditingId(request.id);
    setShowForm(true);
  };

  // Copy to clipboard
  const handleCopy = (request) => {
    const text = `${request.method} ${request.url}`;
    navigator.clipboard.writeText(text);
    setSuccess('Copied to clipboard!');
  };

  // Reset form
  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      method: 'GET',
      url: '',
      headers: '{}',
      body: '',
      collectionId: '',
    });
    setEditingId(null);
    setShowForm(false);
  };

  return (
    <div>
      {/* Header with Add Button */}
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5>Saved Requests</h5>
        <button
          className="btn btn-sm btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : '+ New Request'}
        </button>
      </div>

      {/* Error and Success alerts */}
      <ErrorAlert message={error} onClose={() => setError('')} />
      <SuccessAlert message={success} onClose={() => setSuccess('')} />

      {/* Add/Edit Form */}
      {showForm && (
        <div className="card mb-3 border-primary">
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="row mb-3">
                <div className="col-md-12">
                  <label className="form-label">Request Name</label>
                  <input
                    type="text"
                    className="form-control"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    placeholder="e.g., Get All Posts"
                    disabled={loading}
                  />
                </div>
              </div>

              <div className="row mb-3">
                <div className="col-md-12">
                  <label className="form-label">Description (Optional)</label>
                  <input
                    type="text"
                    className="form-control"
                    name="description"
                    value={formData.description}
                    onChange={handleInputChange}
                    placeholder="Brief description of this request"
                    disabled={loading}
                  />
                </div>
              </div>

              <div className="row mb-3">
                <div className="col-md-12">
                  <label className="form-label">Collection (Optional)</label>
                  <select
                    className="form-select"
                    name="collectionId"
                    value={formData.collectionId}
                    onChange={handleInputChange}
                    disabled={loading || collections.length === 0}
                  >
                    <option value="">-- No Collection --</option>
                    {collections.map(col => (
                      <option key={col.id} value={col.id}>{col.name}</option>
                    ))}
                  </select>
                  {collections.length === 0 && (
                    <small className="text-muted">Create a collection first to organize requests</small>
                  )}
                </div>
              </div>

              <div className="row mb-3">
                <div className="col-md-2">
                  <label className="form-label">Method</label>
                  <select
                    className="form-select"
                    name="method"
                    value={formData.method}
                    onChange={handleInputChange}
                    disabled={loading}
                  >
                    <option>GET</option>
                    <option>POST</option>
                    <option>PUT</option>
                    <option>DELETE</option>
                    <option>PATCH</option>
                  </select>
                </div>
                <div className="col-md-10">
                  <label className="form-label">URL</label>
                  <input
                    type="text"
                    className="form-control"
                    name="url"
                    value={formData.url}
                    onChange={handleInputChange}
                    placeholder="https://api.example.com/endpoint"
                    disabled={loading}
                  />
                </div>
              </div>

              {['POST', 'PUT', 'PATCH'].includes(formData.method) && (
                <div className="mb-3">
                  <label className="form-label">Body (JSON)</label>
                  <textarea
                    className="form-control"
                    name="body"
                    value={formData.body}
                    onChange={handleInputChange}
                    rows="3"
                    disabled={loading}
                    style={{ fontFamily: 'monospace' }}
                  />
                </div>
              )}

              <div className="mb-3">
                <label className="form-label">Headers (JSON)</label>
                <textarea
                  className="form-control"
                  name="headers"
                  value={formData.headers}
                  onChange={handleInputChange}
                  rows="2"
                  disabled={loading}
                  placeholder='{"Authorization": "Bearer token", "Content-Type": "application/json"}'
                  style={{ fontFamily: 'monospace' }}
                />
              </div>

              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary" disabled={loading}>
                  {loading ? 'Saving...' : editingId ? 'Update' : 'Save Request'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={resetForm} disabled={loading}>
                  Clear
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Saved Requests List */}
      {loading && requests.length === 0 && <LoadingSpinner />}

      {requests.length === 0 && !loading && (
        <p className="text-muted text-center py-4">No saved requests yet. Create one to get started.</p>
      )}

      {requests.length > 0 && (
        <div>
          {requests.map((request) => (
            <div key={request.id} className="card mb-2">
              <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                  <div className="flex-grow-1">
                    <h6 className="card-title mb-1">{request.name}</h6>
                    <div className="mb-2">
                      <span className="badge bg-info me-2">{request.method}</span>
                      {request.collectionId && (
                        <span className="badge bg-success me-2">
                          {collections.find(c => c.id === request.collectionId)?.name || 'Collection'}
                        </span>
                      )}
                      <span className="small text-muted text-break">{request.url}</span>
                    </div>
                  </div>
                  <div className="d-flex gap-2">
                    <button
                      className="btn btn-sm btn-outline-primary"
                      onClick={() => handleCopy(request)}
                      title="Copy"
                    >
                      <FiCopy />
                    </button>
                    <button
                      className="btn btn-sm btn-outline-warning"
                      onClick={() => handleEdit(request)}
                      title="Edit"
                    >
                      <FiEdit2 />
                    </button>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDelete(request.id)}
                      title="Delete"
                    >
                      <FiTrash2 />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {loading && <LoadingSpinner />}
    </div>
  );
}
