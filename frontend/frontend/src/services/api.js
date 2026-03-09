import axios from 'axios';

export const getToken    = ()      => localStorage.getItem('jwt_token');
export const setToken    = (token) => localStorage.setItem('jwt_token', token);
export const removeToken = ()      => localStorage.removeItem('jwt_token');

const api = axios.create({ baseURL: '' });

api.interceptors.request.use(config => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const userApi = {
  register: (data) => api.post('/api/auth/register', data),
  login:    (data) => api.post('/api/auth/login', data),
  getById:  (id)   => api.get(`/api/users/${id}`),
  getAll:   ()     => api.get('/api/users'),
};

export const orderApi = {
  getAll:      ()        => api.get('/api/orders'),
  getById:     (id)      => api.get(`/api/orders/${id}`),
  getByTrader: (tid)     => api.get(`/api/orders/trader/${tid}`),
  create:      (data)    => api.post('/api/orders', data),
  update:      (id, d)   => api.put(`/api/orders/${id}/update`, d),
  cancel:      (id)      => api.put(`/api/orders/${id}/cancel`),
};

export const tradeApi = {
  getAll:          ()    => api.get('/api/trades'),
  getByInstrument: (i)   => api.get(`/api/trades/instrument/${i}`),
  getByTrader:     (tid) => api.get(`/api/trades/trader/${tid}`),
  getById:         (id)  => api.get(`/api/trades/${id}`),
};

export const exchangeApi = {
  getAll:      ()         => api.get('/api/exchanges'),
  getById:     (id)       => api.get(`/api/exchanges/${id}`),
  getByRegion: (region)   => api.get(`/api/exchanges/region/${region}`),
  create:      (data)     => api.post('/api/exchanges', data),
  update:      (id, data) => api.put(`/api/exchanges/${id}`, data),
  executeTrade: (region, instrument, price, quantity) =>
    api.post(`/api/exchanges/execute/${region}`, null, {
      params: { instrument, price, quantity }
    }),
};
