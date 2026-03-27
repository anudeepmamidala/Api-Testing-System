import React from "react";

function History({ history, onSelect }) {

  const getColor = (method) => {
    if (method === "GET") return "green";
    if (method === "POST") return "blue";
    if (method === "DELETE") return "red";
    if (method === "PUT") return "orange";
    return "black";
  };

  const clearHistory = () => {
    fetch("http://localhost:8080/api/history", {
      method: "DELETE"
    }).then(() => window.location.reload());
  };

  if (history.length === 0) {
    return <p>No history yet</p>;
  }

  return (
    <div>
      <h3>History</h3>

      <button onClick={clearHistory} style={{ marginBottom: "10px" }}>
        Clear History
      </button>

      {history.map((item) => (
        <div
          key={item.id}
          onClick={() => onSelect(item)}
          style={{
            borderBottom: "1px solid #ddd",
            padding: "10px",
            cursor: "pointer"
          }}
        >
          <strong style={{ color: getColor(item.method) }}>
            {item.method}
          </strong>

          <p style={{ fontSize: "12px", wordBreak: "break-all", margin: "5px 0" }}>
            {item.url}
          </p>

          <p style={{ fontSize: "12px" }}>
            Status: {item.statusCode}
          </p>
        </div>
      ))}
    </div>
  );
}

export default History;