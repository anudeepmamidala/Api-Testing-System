import axios from 'axios';

// Base URL for all API calls - point to backend on port 8080
const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to every request if it exists
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ==================== AUTH ENDPOINTS ====================

export const authAPI = {
  // Register new user
  register: (username, email, password) =>
    api.post('/auth/register', { username, email, password }),

  // Login user
  login: (username, password) =>
    api.post('/auth/login', { username, password }),

  // Health check (no auth required)
  health: () => api.get('/auth/health'),
};

// ==================== REQUEST EXECUTION ENDPOINTS ====================

export const requestAPI = {
  // Execute an HTTP request
  execute: (method, url, headers = {}, body = null) =>
    api.post('/request/execute', {
      method,
      url,
      headers,
      body,
    }),
};

// ==================== HISTORY ENDPOINTS ====================

export const historyAPI = {
  // Get all history entries (paginated)
  getAll: (page = 0, size = 20) =>
    api.get(`/history?page=${page}&size=${size}`),

  // Get single history entry
  getById: (id) =>
    api.get(`/history/${id}`),

  // Delete single history entry
  delete: (id) =>
    api.delete(`/history/${id}`),

  // Delete all history
  deleteAll: () =>
    api.delete('/history'),
};

// ==================== SAVED REQUESTS ENDPOINTS ====================

export const savedRequestsAPI = {
  // Get all saved requests (paginated)
  getAll: (page = 0, size = 20) =>
    api.get(`/storage/saved-requests?page=${page}&size=${size}`),

  // Get single saved request
  getById: (id) =>
    api.get(`/storage/saved-requests/${id}`),

  // Create new saved request
  create: (name, description, method, url, headers, body = null, collectionId = null, status = 'PENDING', responseCode = null) =>
    api.post('/storage/saved-requests', {
      name,
      description,
      method,
      url,
      headers,
      body,
      collectionId,
      status,
      responseCode,
    }),

  // Update saved request
  update: (id, name, description, method, url, headers, body = null, collectionId = null, status = 'PENDING', responseCode = null) =>
    api.put(`/storage/saved-requests/${id}`, {
      name,
      description,
      method,
      url,
      headers,
      body,
      collectionId,
      status,
      responseCode,
    }),

  // Delete saved request
  delete: (id) =>
    api.delete(`/storage/saved-requests/${id}`),
};

// ==================== COLLECTIONS ENDPOINTS ====================

export const collectionsAPI = {
  // Get all collections (paginated)
  getAll: (page = 0, size = 20) =>
    api.get(`/storage/collections?page=${page}&size=${size}`),

  // Get single collection
  getById: (id) =>
    api.get(`/storage/collections/${id}`),

  // Create new collection
  create: (name, description) =>
    api.post('/storage/collections', { name, description }),

  // Update collection
  update: (id, name, description) =>
    api.put(`/storage/collections/${id}`, { name, description }),

  // Delete collection
  delete: (id) =>
    api.delete(`/storage/collections/${id}`),
};

// Export axios instance for direct use if needed
export default api;
