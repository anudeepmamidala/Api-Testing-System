import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import RequestBuilder from '../components/RequestBuilder';
import ResponseViewer from '../components/ResponseViewer';
import History from '../components/History';
import Collections from '../components/Collections';
import Navbar from '../components/layout/Navbar';
import Sidebar from '../components/layout/Sidebar';

export function DashboardPage() {
  // State variables
  const [response, setResponse] = useState(null);
  const [activeTab, setActiveTab] = useState('execute'); // 'execute', 'history', 'collections'

  // Hooks
  const { user } = useAuth();

  return (
    <>
      {/* Navbar at top */}
      <Navbar user={user} />

      <div className="container-fluid" style={{ paddingTop: '60px' }}>
        <div className="row g-0" style={{ height: 'calc(100vh - 60px)' }}>
          {/* Sidebar on left */}
          <div className="col-md-2" style={{ borderRight: '1px solid #ddd', overflowY: 'auto', backgroundColor: '#f8f9fa' }}>
            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
          </div>

          {/* Main content on right */}
          <div className="col-md-10" style={{ overflowY: 'auto' }}>
            <div className="p-4">
              {/* Execute Request Tab */}
              {activeTab === 'execute' && (
                <div>
                  <h2 className="mb-4">Execute Request</h2>
                  <div className="row">
                    <div className="col-md-6">
                      <RequestBuilder setResponse={setResponse} />
                    </div>
                    <div className="col-md-6">
                      <ResponseViewer response={response} />
                    </div>
                  </div>
                </div>
              )}

              {/* History Tab */}
              {activeTab === 'history' && (
                <div>
                  <h2 className="mb-4">Request History</h2>
                  <History />
                </div>
              )}

              {/* Collections Tab */}
              {activeTab === 'collections' && (
                <div>
                  <h2 className="mb-4">Collections</h2>
                  <Collections />
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
