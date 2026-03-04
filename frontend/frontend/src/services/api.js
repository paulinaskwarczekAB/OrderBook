import axios from 'axios';

const BASE = process.env.REACT_APP_API_BASE || 'http://localhost';
const ORDER_API    = `${BASE}:8082`;
const TRADE_API    = `${BASE}:8083`;
const EXCHANGE_API = `${BASE}:8084`;
const USER_API     = `${BASE}:8081`;

export const getToken    = ()      => localStorage.getItem('jwt_token');
export const setToken    = (token) => localStorage.setItem('jwt_token', token);
export const removeToken = ()      => localStorage.removeItem('jwt_token');

const orderAxios    = axios.create({ baseURL: ORDER_API });
const tradeAxios    = axios.create({ baseURL: TRADE_API });
const exchangeAxios = axios.create({ baseURL: EXCHANGE_API });
const userAxios     = axios.create({ baseURL: USER_API });

[orderAxios, tradeAxios, exchangeAxios].forEach(instance => {
  instance.interceptors.request.use(config => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
});

export const orderApi = {
  getAll: () => orderAxios.get('/orders'),

  getById: (id) => orderAxios.get(`/orders/${id}`),

  getByTrader: (traderId) => orderAxios.get(`/orders/trader/${traderId}`),

  create: (data) => orderAxios.post('/orders', data),

  update: (id, data) => orderAxios.put(`/orders/${id}/update`, data),

  cancel: (id) => orderAxios.put(`/orders/${id}/cancel`),
};

export const tradeApi = {
  getAll: () => tradeAxios.get('/trades'),
  getByInstrument: (instrument) => tradeAxios.get(`/trades/instrument/${instrument}`),
  getByTrader: (traderId) => tradeAxios.get(`/trades/trader/${traderId}`),
  getById: (id) => tradeAxios.get(`/trades/${id}`),
};

export const exchangeApi = {
  getAll: () => exchangeAxios.get('/'),
  getById: (id) => exchangeAxios.get(`/${id}`),
  getByRegion: (region) => exchangeAxios.get(`/region/${region}`),
  create: (data) => exchangeAxios.post('/', data),
  update: (id, data) => exchangeAxios.put(`/${id}`, data),

  executeTrade: (region, instrument, price, quantity) =>
    exchangeAxios.post(`/execute/${region}`, null, {
      params: { instrument, price, quantity }
    }),
};

export const userApi = {
  register: (data) => userAxios.post('/auth/register', data),
  login:    (data) => userAxios.post('/auth/login', data),
  getById:  (id)   => userAxios.get(`/users/${id}`),
  getAll:   ()     => userAxios.get('/users'),
};
