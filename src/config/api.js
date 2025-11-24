import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Create axios instance with default configuration
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000, // 30 seconds timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.code === 'ECONNABORTED') {
      error.message = 'Request timeout. Please check if the server is running.';
    } else if (error.message === 'Network Error') {
      error.message = 'Cannot connect to server. Please make sure the backend is running on port 8080.';
    } else if (!error.response) {
      error.message = 'Network error. Please check your connection and ensure the backend server is running.';
    }
    return Promise.reject(error);
  }
);

export default apiClient;

