import React, { useState, useEffect } from 'react';
import { exchangeApi } from '../services/api';

function ExchangeModal({ exchange, onClose, onSuccess }) {
  const isEdit = !!exchange;
  const [form, setForm] = useState(exchange || { name: '', region: 'US', feeLadder: 0.001, bulkFeeLadder: 0.0008, bulkThreshold: 1000 });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handle = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const submit = async () => {
    setError('');
    setLoading(true);
    try {
      const data = { ...form, feeLadder: parseFloat(form.feeLadder), bulkFeeLadder: parseFloat(form.bulkFeeLadder), bulkThreshold: parseInt(form.bulkThreshold) };
      if (isEdit) {
        await exchangeApi.update(exchange.id, data);
      } else {
        await exchangeApi.create(data);
      }
      onSuccess();
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-title">// {isEdit ? 'Edit Exchange' : 'New Exchange'}</div>

        {error && <div className="error-msg">{error}</div>}

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Name</label>
            <input className="form-input" name="name" value={form.name} onChange={handle} placeholder="NYSE" />
          </div>
          <div className="form-group">
            <label className="form-label">Region</label>
            <select className="form-select" name="region" value={form.region} onChange={handle}>
              <option>US</option><option>EU</option><option>ASIA</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Maker Fee</label>
            <input className="form-input" name="makerFee" value={form.makerFee} onChange={handle} type="number" step="0.0001" />
          </div>
          <div className="form-group">
            <label className="form-label">Taker Fee</label>
            <input className="form-input" name="takerFee" value={form.takerFee} onChange={handle} type="number" step="0.0001" />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Liquidity</label>
          <input className="form-input" name="liquidity" value={form.liquidity} onChange={handle} type="number" />
        </div>

        <div className="modal-actions">
          <button className="btn btn-ghost" onClick={onClose}>Cancel</button>
          <button className="btn btn-primary" onClick={submit} disabled={loading}>
            {loading ? '...' : `→ ${isEdit ? 'UPDATE' : 'CREATE'}`}
          </button>
        </div>
      </div>
    </div>
  );
}

function ExecuteTradeModal({ onClose }) {
  const [form, setForm] = useState({ region: 'US', instrument: 'AAPL', price: '', quantity: '' });
  const [result, setResult] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handle = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const submit = async () => {
    setError(''); setResult('');
    setLoading(true);
    try {
      const res = await exchangeApi.executeTrade(form.region, form.instrument, parseFloat(form.price), parseInt(form.quantity));
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-title">// Execute Trade (SORT)</div>

        {error && <div className="error-msg">{error}</div>}
        {result && (
          <div style={{ background: 'rgba(0,212,170,0.08)', border: '1px solid rgba(0,212,170,0.2)', borderRadius: '4px', padding: '12px', marginBottom: '16px', fontFamily: 'var(--font-mono)', fontSize: '12px', color: 'var(--accent)' }}>
            ✓ {result}
          </div>
        )}

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Region</label>
            <select className="form-select" name="region" value={form.region} onChange={handle}>
              <option>US</option><option>EU</option><option>ASIA</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Instrument</label>
            <input className="form-input" name="instrument" value={form.instrument} onChange={handle} placeholder="AAPL" />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Price</label>
            <input className="form-input" name="price" value={form.price} onChange={handle} type="number" step="0.01" />
          </div>
          <div className="form-group">
            <label className="form-label">Quantity</label>
            <input className="form-input" name="quantity" value={form.quantity} onChange={handle} type="number" />
          </div>
        </div>

        <div className="modal-actions">
          <button className="btn btn-ghost" onClick={onClose}>Close</button>
          <button className="btn btn-primary" onClick={submit} disabled={loading}>
            {loading ? '...' : '→ EXECUTE SORT'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default function ExchangesPage() {
  const [exchanges, setExchanges] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const res = await exchangeApi.getAll();
      setExchanges(res.data);
    } catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  return (
    <div>
      {modal === 'new' && <ExchangeModal onClose={() => setModal(null)} onSuccess={load} />}
      {modal === 'execute' && <ExecuteTradeModal onClose={() => setModal(null)} />}
      {modal && typeof modal === 'object' && <ExchangeModal exchange={modal} onClose={() => setModal(null)} onSuccess={load} />}

      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-label">Exchanges</div>
          <div className="stat-value blue">{exchanges.length}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Total Liquidity</div>
          <div className="stat-value green">{exchanges.reduce((s, e) => s + (e.liquidity || 0), 0).toLocaleString()}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Regions</div>
          <div className="stat-value">{[...new Set(exchanges.map(e => e.region))].length}</div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <span className="card-title">Exchanges</span>
          <div style={{ display: 'flex', gap: '8px' }}>
            <button className="btn btn-ghost btn-sm" onClick={() => setModal('execute')}>⚡ Execute SORT</button>
            <button className="btn btn-primary btn-sm" onClick={() => setModal('new')}>+ New Exchange</button>
          </div>
        </div>

        {loading ? (
          <div className="loading">// loading exchanges...</div>
        ) : exchanges.length === 0 ? (
          <div className="empty-state"><div className="icon">🏦</div>Lack of stock exchanges</div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Region</th>
                <th>Maker Fee</th>
                <th>Taker Fee</th>
                <th>Liquidity</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {exchanges.map(ex => (
                <tr key={ex.id}>
                  <td className="mono">{ex.id}</td>
                  <td style={{ fontWeight: 500 }}>{ex.name}</td>
                  <td><span className="badge badge-TRADER">{ex.region}</span></td>
                  <td className="mono" style={{ color: 'var(--yellow)' }}>{(ex.makerFee * 100).toFixed(2)}%</td>
                  <td className="mono" style={{ color: 'var(--yellow)' }}>{(ex.takerFee * 100).toFixed(2)}%</td>
                  <td className="mono" style={{ color: 'var(--green)' }}>{ex.liquidity?.toLocaleString()}</td>
                  <td>
                    <button className="btn btn-ghost btn-sm" onClick={() => setModal(ex)}>Edit</button>
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
