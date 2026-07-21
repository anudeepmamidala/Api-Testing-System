import React, { useState, useEffect } from 'react';
import { historyAPI } from '../services/api';
import { LoadingSpinner, ErrorAlert, SuccessAlert } from './common/Alerts';
import { FiTrash2 } from 'react-icons/fi';
import { formatDistanceToNow } from 'date-fns';

export default function History() {
  // State variables
  const [history, setHistory] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const pageSize = 20;

  // Fetch history on page load or page change
  useEffect(() => {
    fetchHistory(page);
  }, [page]);

  // Fetch history function
  const fetchHistory = async (pageNum) => {
    try {
      setLoading(true);
      setError('');
      const response = await historyAPI.getAll(pageNum, pageSize);
      setHistory(response.data.data || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load history');
    } finally {
      setLoading(false);
    }
  };

  // Delete single history entry
  const handleDeleteEntry = async (id) => {
    if (!window.confirm('Delete this history entry?')) return;

    try {
      await historyAPI.delete(id);
      setSuccess('Entry deleted successfully');
      fetchHistory(page);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete entry');
    }
  };

  // Delete all history
  const handleDeleteAll = async () => {
    if (!window.confirm('Delete ALL history? This cannot be undone.')) return;

    try {
      await historyAPI.deleteAll();
      setSuccess('All history deleted');
      setHistory([]);
      setPage(0);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete history');
    }
  };

  // Get badge color based on method
  const getMethodBadgeColor = (method) => {
    const colors = {
      GET: 'primary',
      POST: 'success',
      PUT: 'warning',
      DELETE: 'danger',
      PATCH: 'info',
    };
    return colors[method] || 'secondary';
  };

  // Get status badge color
  const getStatusBadgeColor = (status) => {
    if (status >= 200 && status < 300) return 'success';
    if (status >= 400 && status < 500) return 'warning';
    if (status >= 500) return 'danger';
    return 'secondary';
  };

  if (loading && history.length === 0) {
    return <LoadingSpinner />;
  }

  return (
    <div className="card">
      <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
        <h5 className="mb-0">Request History</h5>
        <button
          className="btn btn-sm btn-danger"
          onClick={handleDeleteAll}
          disabled={history.length === 0}
        >
          <FiTrash2 /> Clear All
        </button>
      </div>

      <div className="card-body">
        {/* Error and Success alerts */}
        <ErrorAlert message={error} onClose={() => setError('')} />
        <SuccessAlert message={success} onClose={() => setSuccess('')} />

        {/* No history */}
        {history.length === 0 && !loading && (
          <p className="text-muted text-center py-4">No history yet. Execute a request to see it here.</p>
        )}

        {/* History list */}
        {history.length > 0 && (
          <div>
            {history.map((entry) => (
              <div key={entry.id} className="border-bottom pb-3 mb-3">
                <div className="d-flex justify-content-between align-items-start mb-2">
                  <div className="flex-grow-1">
                    <div>
                      <span className={`badge bg-${getMethodBadgeColor(entry.method)} me-2`}>
                        {entry.method}
                      </span>
                      <span className={`badge bg-${getStatusBadgeColor(entry.responseStatus)}`}>
                        {entry.responseStatus}
                      </span>
                    </div>
                    <p className="small text-muted mt-1 mb-1">
                      {formatDistanceToNow(new Date(entry.createdAt), { addSuffix: true })}
                    </p>
                    <p className="small text-break" style={{ wordBreak: 'break-all' }}>
                      {entry.url}
                    </p>
                    <p className="small text-muted">
                      Latency: {entry.latencyMs}ms
                    </p>
                  </div>
                  <button
                    className="btn btn-sm btn-outline-danger"
                    onClick={() => handleDeleteEntry(entry.id)}
                    title="Delete"
                  >
                    <FiTrash2 />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Pagination */}
        {totalPages > 1 && (
          <nav aria-label="Page navigation">
            <ul className="pagination justify-content-center">
              <li className={`page-item ${page === 0 ? 'disabled' : ''}`}>
                <button
                  className="page-link"
                  onClick={() => setPage(page - 1)}
                  disabled={page === 0}
                >
                  Previous
                </button>
              </li>
              {[...Array(totalPages)].map((_, i) => (
                <li key={i} className={`page-item ${page === i ? 'active' : ''}`}>
                  <button
                    className="page-link"
                    onClick={() => setPage(i)}
                  >
                    {i + 1}
                  </button>
                </li>
              ))}
              <li className={`page-item ${page === totalPages - 1 ? 'disabled' : ''}`}>
                <button
                  className="page-link"
                  onClick={() => setPage(page + 1)}
                  disabled={page === totalPages - 1}
                >
                  Next
                </button>
              </li>
            </ul>
          </nav>
        )}

        {loading && <LoadingSpinner />}
      </div>
    </div>
  );
}
