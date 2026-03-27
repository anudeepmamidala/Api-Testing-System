import React, { useState, useEffect } from "react";
import RequestBuilder from "./components/RequestBuilder";
import ResponseViewer from "./components/ResponseViewer";
import History from "./components/History";

function App() {
  const [response, setResponse] = useState(null);
  const [history, setHistory] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);

  const fetchHistory = () => {
    fetch("http://localhost:8080/api/history")
      .then(res => res.json())
      .then(data => setHistory(data));
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  return (
    <div style={{ display: "flex", height: "100vh", fontFamily: "Arial" }}>

      {/* Sidebar */}
      <div style={{ width: "20%", borderRight: "1px solid #ccc", padding: "10px", overflowY: "auto" }}>
        <History history={history} onSelect={setSelectedRequest} />
      </div>

      {/* Main */}
      <div style={{ width: "80%", display: "flex", flexDirection: "column" }}>

        <div style={{ flex: 1, padding: "15px", borderBottom: "1px solid #ccc" }}>
          <RequestBuilder
            setResponse={setResponse}
            selectedRequest={selectedRequest}
            refreshHistory={fetchHistory}
          />
        </div>

        <div style={{ flex: 1, padding: "15px" }}>
          <ResponseViewer response={response} />
        </div>

      </div>
    </div>
  );
}

export default App;