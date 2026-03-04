import React, { useState, useEffect } from 'react';
import { tradeApi } from '../services/api';

export default function TradesPage() {
  const [trades, setTrades]                 = useState([]);
  const [loading, setLoading]               = useState(true);
  const [filterInstrument, setFilterInstrument] = useState('');

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const res = await tradeApi.getAll();
        setTrades(res.data);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const instruments = [...new Set(trades.map(t => t.instrument))].sort();
  const filtered = trades.filter(t => !filterInstrument || t.instrument === filterInstrument);
  const totalVolume = filtered.reduce((sum, t) => sum + (t.price * t.quantity), 0);

  return (
    <div>
      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-label">Total Trades</div>
          <div className="stat-value blue">{filtered.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Volume</div>
          <div className="stat-value green">
            ${totalVolume.toLocaleString('en-US', { maximumFractionDigits: 0 })}
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Instruments</div>
          <div className="stat-value">{instruments.length}</div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <span className="card-title">Executed Trades</span>
            <span className="live-dot" />
          </div>
          <select
            className="form-select"
            style={{ width: 'auto', padding: '4px 8px' }}
            value={filterInstrument}
            onChange={e => setFilterInstrument(e.target.value)}
          >
            <option value="">All Instruments</option>
            {instruments.map(i => <option key={i}>{i}</option>)}
          </select>
        </div>

        {loading ? (
          <div className="loading">// loading trades...</div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <div className="icon">📈</div>
            Lack of executed trades
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th><th>Instrument</th><th>Price</th><th>Quantity</th>
                <th>Volume</th><th>Buy Trader</th><th>Sell Trader</th><th>Time</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(t => (
                <tr key={t.id}>
                  <td className="mono">{t.id}</td>
                  <td className="mono" style={{ color: 'var(--accent2)' }}>{t.instrument}</td>
                  <td className="mono" style={{ color: 'var(--green)' }}>${t.price?.toFixed(2)}</td>
                  <td className="mono">{t.quantity}</td>
                  <td className="mono">
                    ${(t.price * t.quantity).toLocaleString('en-US', { maximumFractionDigits: 0 })}
                  </td>
                  <td className="mono" style={{ color: 'var(--green)' }}>#{t.buyTraderId}</td>
                  <td className="mono" style={{ color: 'var(--red)' }}>#{t.sellTraderId}</td>
                  <td className="mono" style={{ color: 'var(--text3)' }}>
                    {t.executedAt ? new Date(t.executedAt).toLocaleString('pl-PL') : '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}