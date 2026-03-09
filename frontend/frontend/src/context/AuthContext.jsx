import React, { createContext, useContext, useState, useEffect } from 'react';
import { userApi, setToken, getToken, removeToken } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {

  const [user, setUser]       = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
      const token = getToken();
      if (token) {
        try {
          const saved = localStorage.getItem('user_data');
          if (saved) {
            setUser(JSON.parse(saved));
          } else {
            const payload = JSON.parse(atob(token.split('.')[1]));
            setUser({
              id:       payload.sub,
              username: payload.username || payload.sub,
              role:     payload.role
            });
          }
        } catch {
          removeToken();
        }
      }
      setLoading(false);
  }, []);

  const login = async (credentials) => {
      const res = await userApi.login(credentials);
      const { token, user: userData } = res.data;
      setToken(token);
      localStorage.setItem('user_data', JSON.stringify(userData));
      setUser(userData);
      return userData;
  };

  const register = async (data) => {
      const res = await userApi.register(data);
      const { token, user: userData } = res.data;
      setToken(token);
      localStorage.setItem('user_data', JSON.stringify(userData));
      setUser(userData);
      return userData;
  };

  const logout = () => {
      removeToken();
      localStorage.removeItem('user_data');
      setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);