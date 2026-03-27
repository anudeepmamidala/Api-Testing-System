import React, { useState, useEffect } from "react";

function RequestBuilder({ setResponse, selectedRequest, refreshHistory }) {
  const [url, setUrl] = useState("");
  const [method, setMethod] = useState("GET");
  const [body, setBody] = useState("");
  const [loading, setLoading] = useState(false);

  const [headers, setHeaders] = useState([{ key: "", value: "" }]);

  useEffect(() => {
    if (selectedRequest) {
      setUrl(selectedRequest.url);
      setMethod(selectedRequest.method);
      setBody(selectedRequest.body || "");

      const headerEntries = Object.entries(selectedRequest.headers || {});

      setHeaders(
        headerEntries.length > 0
          ? headerEntries.map(([key, value]) => ({ key, value }))
          : [{ key: "", value: "" }]
      );
    }
  }, [selectedRequest]);

  const handleHeaderChange = (index, field, value) => {
    const newHeaders = [...headers];
    newHeaders[index][field] = value;
    setHeaders(newHeaders);
  };

  const addHeader = () => {
    setHeaders([...headers, { key: "", value: "" }]);
  };

  const removeHeader = (index) => {
    setHeaders(headers.filter((_, i) => i !== index));
  };

  const buildHeadersObject = () => {
    const obj = {};
    headers.forEach(h => {
      if (h.key) obj[h.key] = h.value;
    });
    return obj;
  };

  const sendRequest = async () => {

    if (!url) {
      alert("URL is required");
      return;
    }

    if (method === "POST" && url.match(/\/\d+$/)) {
      alert("POST should be used on base endpoint (e.g., /posts)");
      return;
    }

    if (body && method !== "GET" && method !== "DELETE") {
      try {
        JSON.parse(body);
      } catch {
        alert("Invalid JSON");
        return;
      }
    }

    setLoading(true);

    try {
      let finalUrl = url;
      if (!url.startsWith("http://") && !url.startsWith("https://")) {
        finalUrl = "http://" + url;
      }

      const res = await fetch("http://localhost:8080/api/send", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          url: finalUrl,
          method,
          headers: buildHeadersObject(),
          body,
        }),
      });

      const data = await res.json();
      setResponse(data);
      refreshHistory();

    } catch (err) {
      setResponse({
        status: "ERROR",
        error: err.message,
        body: ""
      });
    }

    setLoading(false);
  };

  return (
    <div>
      <h3>Request</h3>

      {/* URL */}
      <div style={{ marginBottom: "15px" }}>
        <input
          placeholder="Enter URL"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          style={{ width: "100%", padding: "8px" }}
        />
      </div>

      {/* Method */}
      <div style={{ marginBottom: "15px" }}>
        <select
          value={method}
          onChange={(e) => {
            setMethod(e.target.value);
            if (e.target.value === "GET" || e.target.value === "DELETE") {
              setBody("");
            }
          }}
          style={{ padding: "8px" }}
        >
          <option>GET</option>
          <option>POST</option>
          <option>PUT</option>
          <option>DELETE</option>
          <option>PATCH</option>
        </select>
      </div>

      {/* Headers */}
      <div style={{ marginBottom: "15px" }}>
        <h4>Headers</h4>

        {headers.map((header, index) => (
          <div key={index} style={{ display: "flex", marginBottom: "5px" }}>
            <input
              placeholder="Key"
              value={header.key}
              onChange={(e) =>
                handleHeaderChange(index, "key", e.target.value)
              }
              style={{ padding: "6px", marginRight: "5px" }}
            />
            <input
              placeholder="Value"
              value={header.value}
              onChange={(e) =>
                handleHeaderChange(index, "value", e.target.value)
              }
              style={{ padding: "6px", marginRight: "5px" }}
            />
            <button onClick={() => removeHeader(index)}>X</button>
          </div>
        ))}

        <button onClick={addHeader}>+ Add Header</button>
      </div>

      {/* Body (ONLY for POST/PUT/PATCH) */}
      {method !== "GET" && method !== "DELETE" && (
        <div style={{ marginBottom: "15px" }}>
          <textarea
            placeholder="Body (JSON)"
            value={body}
            onChange={(e) => setBody(e.target.value)}
            style={{ width: "100%", height: "120px", padding: "8px" }}
          />
        </div>
      )}

      {/* Send */}
      <button
        onClick={sendRequest}
        style={{ padding: "10px 15px", cursor: "pointer" }}
      >
        Send
      </button>

      {loading && <p>Sending request...</p>}
    </div>
  );
}

export default RequestBuilder;