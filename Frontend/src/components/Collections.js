import React, { useState, useEffect } from 'react';
import { collectionsAPI } from '../services/api';
import { LoadingSpinner, ErrorAlert, SuccessAlert } from './common/Alerts';
import { FiEdit2, FiTrash2, FiFolder, FiChevronDown, FiChevronRight } from 'react-icons/fi';

export default function Collections() {
  // State
  const [collections, setCollections] = useState([]);
  const [expandedCollectionId, setExpandedCollectionId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  
  // Form state
  const [formData, setFormData] = useState({
    name: '',
    description: '',
  });

  // Fetch collections
  useEffect(() => {
    fetchCollections();
  }, []);

  const fetchCollections = async () => {
    try {
      setLoading(true);
      setError('');
      const response = await collectionsAPI.getAll();
      setCollections(response.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load collections');
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

  // Create or update collection
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.name.trim()) {
      setError('Collection name is required');
      return;
    }

    try {
      setLoading(true);
      setError('');
      
      if (editingId) {
        // Update
        await collectionsAPI.update(editingId, formData.name, formData.description);
        setSuccess('Collection updated successfully');
      } else {
        // Create
        await collectionsAPI.create(formData.name, formData.description);
        setSuccess('Collection created successfully');
      }

      resetForm();
      fetchCollections();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save collection');
    } finally {
      setLoading(false);
    }
  };

  // Delete collection
  const handleDelete = async (id) => {
    if (!window.confirm('Delete this collection? This cannot be undone.')) return;

    try {
      setLoading(true);
      await collectionsAPI.delete(id);
      setSuccess('Collection deleted');
      fetchCollections();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete collection');
    } finally {
      setLoading(false);
    }
  };

  // Edit collection
  const handleEdit = (collection) => {
    setFormData({
      name: collection.name,
      description: collection.description || '',
    });
    setEditingId(collection.id);
    setShowForm(true);
  };

  // Toggle expand
  const toggleExpand = (collectionId) => {
    setExpandedCollectionId(expandedCollectionId === collectionId ? null : collectionId);
  };

  // Get status badge color based on HTTP response code
  const getStatusBadgeClass = (responseCode) => {
    if (!responseCode) return 'bg-secondary';
    
    const code = parseInt(responseCode);
    if (code >= 200 && code < 300) {
      return 'bg-success'; // 2xx = green
    } else if (code >= 400 && code < 500) {
      return 'bg-warning'; // 4xx = orange
    } else if (code >= 500) {
      return 'bg-danger'; // 5xx = red
    }
    return 'bg-secondary';
  };

  // Reset form
  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
    });
    setEditingId(null);
    setShowForm(false);
  };

  return (
    <div>
      {/* Header with Add Button */}
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5>Collections</h5>
        <button
          className="btn btn-sm btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? 'Cancel' : '+ New Collection'}
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
              <div className="mb-3">
                <label className="form-label">Collection Name</label>
                <input
                  type="text"
                  className="form-control"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  placeholder="e.g., User API, Payment API"
                  disabled={loading}
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Description</label>
                <textarea
                  className="form-control"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  placeholder="Add a description (optional)"
                  rows="2"
                  disabled={loading}
                />
              </div>

              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary" disabled={loading}>
                  {loading ? 'Saving...' : editingId ? 'Update Collection' : 'Create Collection'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={resetForm} disabled={loading}>
                  Clear
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Collections Grid */}
      {loading && collections.length === 0 && <LoadingSpinner />}

      {collections.length === 0 && !loading && (
        <p className="text-muted text-center py-4">No collections yet. Create one to organize your requests.</p>
      )}

      {collections.length > 0 && (
        <div>
          {collections.map((collection) => (
            <div key={collection.id} className="card mb-3">
              <div className="card-body">
                {/* Collection header - clickable to expand */}
                <div 
                  className="d-flex justify-content-between align-items-center cursor-pointer"
                  onClick={() => toggleExpand(collection.id)}
                  style={{ cursor: 'pointer', userSelect: 'none' }}
                >
                  <div className="d-flex align-items-center gap-2 flex-grow-1">
                    {expandedCollectionId === collection.id ? <FiChevronDown /> : <FiChevronRight />}
                    <FiFolder size={20} className="text-primary" />
                    <div>
                      <h6 className="mb-0">{collection.name}</h6>
                      {collection.description && (
                        <p className="small text-muted mb-0">{collection.description}</p>
                      )}
                    </div>
                  </div>
                  
                  <div className="d-flex gap-2 align-items-center">
                    <span className="badge bg-info">{collection.requestCount || 0} requests</span>
                    <button
                      className="btn btn-sm btn-outline-warning"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleEdit(collection);
                      }}
                      title="Edit"
                    >
                      <FiEdit2 />
                    </button>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleDelete(collection.id);
                      }}
                      title="Delete"
                    >
                      <FiTrash2 />
                    </button>
                  </div>
                </div>

                {/* Expanded requests list */}
                {expandedCollectionId === collection.id && collection.requests && collection.requests.length > 0 && (
                  <div className="mt-3 pt-3 border-top">
                    <h6 className="mb-2">Requests</h6>
                    {collection.requests.map((request) => (
                      <div key={request.id} className="d-flex justify-content-between align-items-center py-2 px-3 bg-light rounded mb-2">
                        <div className="flex-grow-1">
                          <div className="d-flex align-items-center gap-2">
                            <span className={`badge ${getStatusBadgeClass(request.responseCode)}`}>
                              {request.responseCode || 'No Status'}
                            </span>
                            <span className="badge bg-secondary">{request.method}</span>
                            <span className="small text-truncate">{request.url}</span>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}

                {expandedCollectionId === collection.id && (!collection.requests || collection.requests.length === 0) && (
                  <div className="mt-3 pt-3 border-top">
                    <p className="text-muted mb-0 small">No requests in this collection yet</p>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {loading && <LoadingSpinner />}
    </div>
  );
}
