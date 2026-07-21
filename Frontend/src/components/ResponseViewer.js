import React, { useState } from 'react';

export default function ResponseViewer({ response }) {
  const [activeTab, setActiveTab] = useState('body'); // 'body', 'headers'

  if (!response) {
    return (
      <div className="card">
        <div className="card-header bg-secondary text-white">
          <h5 className="mb-0">Response</h5>
        </div>
        <div className="card-body">
          <p className="text-muted">No response yet. Send a request to see the response here.</p>
        </div>
      </div>
    );
  }

  // Check if response is success
  const isSuccess = response.statusCode >= 200 && response.statusCode < 300;
  const statusColor = isSuccess ? 'success' : 'danger';

  // Format JSON body nicely
  let formattedBody = response.body;
  try {
    formattedBody = JSON.stringify(JSON.parse(response.body), null, 2);
  } catch (e) {
    // Keep as is if not JSON
  }

  return (
    <div className="card">
      <div className="card-header bg-secondary text-white d-flex justify-content-between align-items-center">
        <h5 className="mb-0">Response</h5>
        <div>
          <span className={`badge bg-${statusColor} me-2`}>
            Status: {response.statusCode}
          </span>
          <span className="badge bg-info">
            Latency: {response.latencyMs}ms
          </span>
        </div>
      </div>

      <div className="card-body">
        {/* Tabs */}
        <ul className="nav nav-tabs mb-3">
          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === 'body' ? 'active' : ''}`}
              onClick={() => setActiveTab('body')}
            >
              Response Body
            </button>
          </li>
          <li className="nav-item">
            <button
              className={`nav-link ${activeTab === 'headers' ? 'active' : ''}`}
              onClick={() => setActiveTab('headers')}
            >
              Response Headers
            </button>
          </li>
        </ul>

        {/* Body Tab */}
        {activeTab === 'body' && (
          <pre
            className="bg-light p-3 rounded"
            style={{
              fontFamily: 'monospace',
              maxHeight: '400px',
              overflowY: 'auto',
              fontSize: '12px',
            }}
          >
            {formattedBody}
          </pre>
        )}

        {/* Headers Tab */}
        {activeTab === 'headers' && (
          <div className="table-responsive">
            <table className="table table-sm">
              <thead>
                <tr>
                  <th>Header</th>
                  <th>Value</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(response.headers || {}).map(([key, value]) => (
                  <tr key={key}>
                    <td>{key}</td>
                    <td style={{ wordBreak: 'break-word' }}>{value}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}