import React from "react";

function ResponseViewer({ response }) {
  if (!response) return <p>No response yet</p>;

  let formattedBody = response.body;

  try {
    formattedBody = JSON.stringify(JSON.parse(response.body), null, 2);
  } catch {
    // keep raw if not JSON
  }

  const isSuccess =
    response.status >= 200 && response.status < 300;

  return (
    <div>
      <h3>Response</h3>

      <p style={{ color: isSuccess ? "green" : "red" }}>
        Status: {response.status}
      </p>

      <p>Time: {response.timeTaken} ms</p>

      <pre
        style={{
          background: "#f4f4f4",
          padding: "10px",
          maxHeight: "250px",
          overflow: "auto"
        }}
      >
        {formattedBody}
      </pre>

      {response.error && (
        <p style={{ color: "red" }}>{response.error}</p>
      )}
    </div>
  );
}

export default ResponseViewer;