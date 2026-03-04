import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function AuthPage() {
  const { login, register } = useAuth();
  const [mode, setMode]   = useState('login');

  const [form, setForm] = useState({
    username:  '',
    email:     '',
    password:  '',
    firstName: '',
    lastName:  '',
  });

  const [error, setError]     = useState('');
  const [loading, setLoading] = useState(false);
  const handle = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const submit = async () => {
    setError('');
    setLoading(true);
    try {
      if (mode === 'login') {
        await login({ username: form.username, password: form.password });
      } else {
        await register(form);
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Błąd połączenia z serwerem');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        {/* Logo */}
        <div className="auth-logo">
          <h1>TradeBook</h1>
          <p>// trading system v1.0</p>
        </div>

        {error && <div className="error-msg">{error}</div>}
        <div className="form-group">
          <label className="form-label">Username</label>
          <input
            className="form-input"
            name="username"
            value={form.username}
            onChange={handle}
            placeholder="trader01"
            autoComplete="username"
          />
        </div>

        {mode === 'register' && (
          <>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" name="email" value={form.email} onChange={handle}
                type="email" placeholder="trader@firm.com" />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">First Name</label>
                <input className="form-input" name="firstName" value={form.firstName} onChange={handle} placeholder="Jan" />
              </div>
              <div className="form-group">
                <label className="form-label">Last Name</label>
                <input className="form-input" name="lastName" value={form.lastName} onChange={handle} placeholder="Kowalski" />
              </div>
            </div>
          </>
        )}

        <div className="form-group">
          <label className="form-label">Password</label>
          <input
            className="form-input"
            name="password"
            value={form.password}
            onChange={handle}
            type="password"
            placeholder="••••••••"
            autoComplete="current-password"
            onKeyDown={e => e.key === 'Enter' && submit()}
          />
        </div>

        <button
          className="btn btn-primary"
          style={{ width: '100%', justifyContent: 'center' }}
          onClick={submit}
          disabled={loading}
        >
          {loading ? '...' : mode === 'login' ? '→ LOGIN' : '→ REGISTER'}
        </button>
        <div className="auth-toggle">
          {mode === 'login' ? (
            <>Don't have account? <button onClick={() => { setMode('register'); setError(''); }}>Register</button></>
          ) : (
            <>Already have account? <button onClick={() => { setMode('login'); setError(''); }}>Login</button></>
          )}
        </div>

      </div>
    </div>
  );
}