import React, { useState, useEffect } from 'react';
import { requestAPI, collectionsAPI, savedRequestsAPI } from '../services/api';
import { ErrorAlert, SuccessAlert } from './common/Alerts';
import { LoadingSpinner } from './common/Alerts';

export default function RequestBuilder({ setResponse, onRequestExecuted }) {
  // State variables
  const [method, setMethod] = useState('GET');
  const [url, setUrl] = useState('');
  const [headers, setHeaders] = useState('{\n  "Content-Type": "application/json"\n}');
  const [body, setBody] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [collections, setCollections] = useState([]);
  const [selectedCollectionId, setSelectedCollectionId] = useState('');
  const [savingRequest, setSavingRequest] = useState(false);

  // Fetch collections on mount
  useEffect(() => {
    fetchCollections();
  }, []);

  const fetchCollections = async () => {
    try {
      const response = await collectionsAPI.getAll();
      setCollections(response.data || []);
      // Auto-select first collection if available
      if (response.data && response.data.length > 0 && !selectedCollectionId) {
        setSelectedCollectionId(response.data[0].id);
      }
    } catch (err) {
      console.log('Could not load collections');
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    // Validation
    if (!url.trim()) {
      setError('URL is required');
      return;
    }

    if (!selectedCollectionId) {
      setError('Please select a collection first');
      return;
    }

    try {
      setLoading(true);

      // Parse headers (try to parse as JSON)
      let headerObj = {};
      try {
        headerObj = JSON.parse(headers) || {};
      } catch (err) {
        setError('Headers must be valid JSON');
        setLoading(false);
        return;
      }

      // Make API call
      const response = await requestAPI.execute(method, url, headerObj, body || null);
      
      setResponse(response.data);
      setSuccess('Request executed successfully!');

      // Auto-save to collection with response code
      setSavingRequest(true);
      try {
        const requestName = `${method} ${url.substring(url.lastIndexOf('/') + 1) || 'request'}`;
        const responseCode = response.status || 200; // Use HTTP status code from axios response
        await savedRequestsAPI.create(
          requestName,
          `Auto-saved from dashboard execution`,
          method,
          url,
          headerObj,
          body || null,
          parseInt(selectedCollectionId),
          responseCode.toString(), // Store response code as status
          responseCode
        );
        console.log('Request auto-saved to collection with status code:', responseCode);
      } catch (saveErr) {
        console.log('Auto-save to collection failed:', saveErr);
        // Don't fail the main request execution if auto-save fails
      } finally {
        setSavingRequest(false);
      }

      onRequestExecuted?.();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to execute request');
      setResponse(null);

      // Auto-save failed request with error response code
      setSavingRequest(true);
      try {
        // Parse headers again for error case
        let headerObj = {};
        try {
          headerObj = JSON.parse(headers) || {};
        } catch (headerErr) {
          headerObj = {};
        }
        
        const requestName = `${method} ${url.substring(url.lastIndexOf('/') + 1) || 'request'}`;
        const responseCode = err.response?.status || 500; // Use error response status
        await savedRequestsAPI.create(
          requestName,
          `Auto-saved from dashboard execution (Failed)`,
          method,
          url,
          headerObj,
          body || null,
          parseInt(selectedCollectionId),
          responseCode.toString(), // Store response code as status
          responseCode
        );
        console.log('Failed request auto-saved to collection with status code:', responseCode);
      } catch (saveErr) {
        console.log('Auto-save failed request failed:', saveErr);
      } finally {
        setSavingRequest(false);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <div className="card-header bg-primary text-white">
        <h5 className="mb-0">Request Builder</h5>
      </div>
      <div className="card-body">
        {/* Error and Success alerts */}
        <ErrorAlert message={error} onClose={() => setError('')} />
        <SuccessAlert message={success} onClose={() => setSuccess('')} />

        <form onSubmit={handleSubmit}>
          {/* Method, Collection and URL row */}
          <div className="row mb-3">
            <div className="col-md-2">
              <label className="form-label">Method</label>
              <select
                className="form-select"
                value={method}
                onChange={(e) => setMethod(e.target.value)}
                disabled={loading}
              >
                <option value="GET">GET</option>
                <option value="POST">POST</option>
                <option value="PUT">PUT</option>
                <option value="DELETE">DELETE</option>
                <option value="PATCH">PATCH</option>
                <option value="HEAD">HEAD</option>
                <option value="OPTIONS">OPTIONS</option>
              </select>
            </div>

            <div className="col-md-3">
              <label className="form-label">Collection</label>
              <select
                className="form-select"
                value={selectedCollectionId}
                onChange={(e) => setSelectedCollectionId(e.target.value)}
                disabled={loading || collections.length === 0}
              >
                <option value="">-- Select Collection --</option>
                {collections.map(col => (
                  <option key={col.id} value={col.id}>{col.name}</option>
                ))}
              </select>
              {collections.length === 0 && (
                <small className="text-warning">Create a collection first</small>
              )}
            </div>

            <div className="col-md-7">
              <label className="form-label">URL</label>
              <input
                type="text"
                className="form-control"
                value={url}
                onChange={(e) => setUrl(e.target.value)}
                placeholder="https://api.example.com/endpoint"
                disabled={loading}
              />
            </div>
          </div>

          {/* Headers section */}
          <div className="mb-3">
            <label className="form-label">Headers (JSON)</label>
            <textarea
              className="form-control"
              rows="3"
              value={headers}
              onChange={(e) => setHeaders(e.target.value)}
              placeholder='{"Content-Type": "application/json"}'
              disabled={loading}
              style={{ fontFamily: 'monospace' }}
            />
            <small className="text-muted">Enter headers as JSON object</small>
          </div>

          {/* Body section (only for POST/PUT/PATCH) */}
          {['POST', 'PUT', 'PATCH'].includes(method) && (
            <div className="mb-3">
              <label className="form-label">Body (JSON)</label>
              <textarea
                className="form-control"
                rows="4"
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder='{"key": "value"}'
                disabled={loading}
                style={{ fontFamily: 'monospace' }}
              />
              <small className="text-muted">Enter request body as JSON</small>
            </div>
          )}

          {/* Buttons */}
          <div className="d-flex gap-2">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Sending...' : 'Send Request'}
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              disabled={loading}
              onClick={() => {
                setUrl('');
                setBody('');
                setHeaders('{\n  "Content-Type": "application/json"\n}');
              }}
            >
              Clear
            </button>
          </div>
        </form>

        {/* Loading spinner */}
        {loading && <LoadingSpinner />}
      </div>
    </div>
  );
}