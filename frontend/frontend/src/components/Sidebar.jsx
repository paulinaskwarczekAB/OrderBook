import React from 'react';
import { useAuth } from '../context/AuthContext';

const NAV_ITEMS = [
  { id: 'dashboard', label: 'Dashboard', icon: '◈' },
  { id: 'orders',    label: 'Orders',    icon: '≡' },
  { id: 'trades',    label: 'Trades',    icon: '↕' },
  { id: 'exchanges', label: 'Exchanges', icon: '◎' },
];


export default function Sidebar({ page, setPage }) {
  const { user, logout } = useAuth();

  return (
    <nav className="sidebar">

      <div className="sidebar-logo">
        <h1>TradeBook</h1>
        <p>// v1.0 system</p>
      </div>

      <div className="sidebar-nav">
        <div className="nav-section">Navigation</div>


        {NAV_ITEMS.map(item => (
          <div
            key={item.id}
            className={`nav-item ${page === item.id ? 'active' : ''}`}
            onClick={() => setPage(item.id)}
          >
            <span className="icon">{item.icon}</span>
            {item.label}
          </div>
        ))}
      </div>

      <div style={{ padding: '12px', borderTop: '1px solid var(--border)' }}>
        <div style={{ padding: '8px', background: 'var(--bg2)', borderRadius: '4px', marginBottom: '8px' }}>
          <div style={{ fontFamily: 'var(--font-mono)', fontSize: '11px', color: 'var(--text3)' }}>
            Logged in as
          </div>
          <div style={{ fontFamily: 'var(--font-mono)', fontSize: '12px', color: 'var(--accent)', marginTop: '2px' }}>
            {user?.username || '—'}
          </div>
        </div>

        <button
          className="btn btn-ghost"
          style={{ width: '100%', justifyContent: 'center', fontSize: '10px' }}
          onClick={logout}
        >
          → LOGOUT
        </button>
      </div>

    </nav>
  );
}
