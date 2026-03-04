import React, { useState, useEffect } from 'react';
import { orderApi, tradeApi, exchangeApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats]   = useState({ orders: [], trades: [], exchanges: [] });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const [o, t, e] = await Promise.allSettled([
          orderApi.getAll(),
          tradeApi.getAll(),
          exchangeApi.getAll(),
        ]);

        setStats({
          orders:    o.status === 'fulfilled' ? o.value.data : [],
          trades:    t.status === 'fulfilled' ? t.value.data : [],
          exchanges: e.status === 'fulfilled' ? e.value.data : [],
        });
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const { orders, trades, exchanges } = stats;

  const pendingOrders = orders.filter(o => o.status === 'PENDING');
  const filledOrders  = orders.filter(o => o.status === 'FILLED');
  const totalVolume   = trades.reduce((sum, t) => sum + (t.price * t.quantity), 0);
  const buyOrders     = orders.filter(o => o.side === 'BUY').length;
  const sellOrders    = orders.filter(o => o.side === 'SELL').length;

  const recentTrades = [...trades].reverse().slice(0, 5);
  const recentOrders = [...orders].reverse().slice(0, 5);

  if (loading) return <div className="loading">// loading dashboard...</div>;

  return (
    <div>
      <div style={{ marginBottom: '20px' }}>
        <div style={{ fontFamily: 'var(--font-mono)', fontSize: '11px', color: 'var(--text3)' }}>
          // welcome back, <span style={{ color: 'var(--accent)' }}>{user?.username || 'trader'}</span>
        </div>
        <div style={{ fontFamily: 'var(--font-mono)', fontSize: '20px', fontWeight: 700, marginTop: '4px' }}>
          Market Overview
        </div>
      </div>

      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-label">Total Orders</div>
          <div className="stat-value blue">{orders.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Pending</div>
          <div className="stat-value yellow">{pendingOrders.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Filled</div>
          <div className="stat-value green">{filledOrders.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Trades</div>
          <div className="stat-value">{trades.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Volume USD</div>
          <div className="stat-value green">${(totalVolume / 1000).toFixed(0)}K</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Exchanges</div>
          <div className="stat-value">{exchanges.length}</div>
        </div>
      </div>
      <div className="grid-2">
        <div className="card">
          <div className="card-header">
            <span className="card-title">Recent Trades</span>
            <span className="live-dot" />
          </div>
          {recentTrades.length === 0 ? (
            <div className="empty-state" style={{ padding: '24px' }}>
              <div className="icon">📈</div>Lack of transactions
            </div>
          ) : (
            <table className="data-table">
              <thead>
                <tr><th>Instrument</th><th>Price</th><th>Qty</th><th>Time</th></tr>
              </thead>
              <tbody>
                {recentTrades.map(t => (
                  <tr key={t.id}>
                    <td className="mono" style={{ color: 'var(--accent2)' }}>{t.instrument}</td>
                    <td className="mono" style={{ color: 'var(--green)' }}>${t.price?.toFixed(2)}</td>
                    <td className="mono">{t.quantity}</td>
                    <td className="mono" style={{ color: 'var(--text3)', fontSize: '11px' }}>
                      {t.executedAt ? new Date(t.executedAt).toLocaleTimeString('pl-PL') : '—'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <div className="card">
          <div className="card-header">
            <span className="card-title">Recent Orders</span>
          </div>
          {recentOrders.length === 0 ? (
            <div className="empty-state" style={{ padding: '24px' }}>
              <div className="icon">📋</div>Lack Of Orders
            </div>
          ) : (
            <table className="data-table">
              <thead>
                <tr><th>Instrument</th><th>Side</th><th>Price</th><th>Status</th></tr>
              </thead>
              <tbody>
                {recentOrders.map(o => (
                  <tr key={o.id}>
                    <td className="mono" style={{ color: 'var(--accent2)' }}>{o.instrument}</td>
                    <td><span className={`badge badge-${o.side}`}>{o.side}</span></td>
                    <td className="mono">${o.price?.toFixed(2)}</td>
                    <td><span className={`badge badge-${o.status}`} style={{ fontSize: '9px' }}>{o.status}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
      <div className="card">
        <div className="card-header">
          <span className="card-title">Order Flow</span>
        </div>
        <div style={{ display: 'flex', gap: '24px', alignItems: 'center' }}>
          <div>
            <div className="stat-label">BUY Orders</div>
            <div className="stat-value green">{buyOrders}</div>
          </div>
          <div style={{ flex: 1, height: '8px', background: 'var(--bg2)', borderRadius: '4px', overflow: 'hidden' }}>
            <div style={{
              height: '100%',
              width: `${orders.length > 0 ? (buyOrders / orders.length) * 100 : 50}%`,
              background: 'linear-gradient(90deg, var(--green), var(--accent2))',
              borderRadius: '4px',
              transition: 'width 0.5s ease',
            }} />
          </div>
          <div style={{ textAlign: 'right' }}>
            <div className="stat-label">SELL Orders</div>
            <div className="stat-value red">{sellOrders}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
