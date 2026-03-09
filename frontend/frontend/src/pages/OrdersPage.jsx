import React, { useState, useEffect } from 'react';
import { orderApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

const INSTRUMENTS = ['AAPL', 'GOOGL', 'MSFT', 'AMZN', 'TSLA', 'META', 'NVDA', 'NFLX'];

function NewOrderModal({ onClose, onSuccess }) {
  const { user } = useAuth();

  const [form, setForm] = useState({
    traderId:   user?.id || '',
    instrument: 'AAPL',
    side:       'BUY',
    price:      '',
    quantity:   '',
    region:     'US',
  });
  const [error, setError]     = useState('');
  const [loading, setLoading] = useState(false);

  const handle = e => setForm(f => ({ ...f, [e.target.name]: e.target.value }));

  const submit = async () => {
    setError('');
    if (!form.price || !form.quantity) {
      setError('Fill price and quantity');
      return;
    }
    setLoading(true);
    try {
      await orderApi.create({
        ...form,
        price:    parseFloat(form.price),
        quantity: parseInt(form.quantity),
      });
      onSuccess();
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Error during creating order');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-title">// New Order</div>

        {error && <div className="error-msg">{error}</div>}

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Instrument</label>
            <select className="form-select" name="instrument" value={form.instrument} onChange={handle}>
              {INSTRUMENTS.map(i => <option key={i}>{i}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Side</label>
            <select className="form-select" name="side" value={form.side} onChange={handle}>
              <option>BUY</option>
              <option>SELL</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Price (USD)</label>
            <input className="form-input" name="price" value={form.price} onChange={handle}
              type="number" step="0.01" placeholder="150.00" />
          </div>
          <div className="form-group">
            <label className="form-label">Quantity</label>
            <input className="form-input" name="quantity" value={form.quantity} onChange={handle}
              type="number" placeholder="100" />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label className="form-label">Region</label>
            <select className="form-select" name="region" value={form.region} onChange={handle}>
              <option>US</option><option>EU</option><option>ASIA</option>
            </select>
          </div>
        </div>

        <div className="modal-actions">
          <button className="btn btn-ghost" onClick={onClose}>Cancel</button>
          <button className="btn btn-primary" onClick={submit} disabled={loading}>
            {loading ? '...' : `→ PLACE ${form.side}`}
          </button>
        </div>
      </div>
    </div>
  );
}

export default function OrdersPage() {
  const { user } = useAuth();
  const [orders, setOrders]           = useState([]);
  const [loading, setLoading]         = useState(true);
  const [showModal, setShowModal]     = useState(false);
  const [filterStatus, setFilterStatus] = useState('ALL');
  const [filterSide, setFilterSide]   = useState('ALL');

  const load = async () => {
    setLoading(true);
    try {
      const res = await orderApi.getByTrader(user.id);
      setOrders(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const cancel = async (id) => {
    if (!window.confirm('Cancel order?')) return;
    try {
      await orderApi.cancel(id);
      load();
    } catch (e) {
      console.error(e);
    }
  };

  const filtered = orders.filter(o =>
    (filterStatus === 'ALL' || o.status === filterStatus) &&
    (filterSide   === 'ALL' || o.side   === filterSide)
  );

  const stats = {
    total:     orders.length,
    pending:   orders.filter(o => o.status === 'PENDING').length,
    filled:    orders.filter(o => o.status === 'FILLED').length,
    cancelled: orders.filter(o => o.status === 'CANCELLED').length,
  };

  return (
    <div>
      {showModal && (
        <NewOrderModal
          onClose={() => setShowModal(false)}
          onSuccess={load}
        />
      )}

      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-label">Total Orders</div>
          <div className="stat-value blue">{stats.total}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Pending</div>
          <div className="stat-value yellow">{stats.pending}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Filled</div>
          <div className="stat-value green">{stats.filled}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Cancelled</div>
          <div className="stat-value red">{stats.cancelled}</div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <span className="card-title">Orders</span>
            <span className="live-dot" />
          </div>
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <select className="form-select" style={{ width: 'auto', padding: '4px 8px' }}
              value={filterSide} onChange={e => setFilterSide(e.target.value)}>
              <option value="ALL">All Sides</option>
              <option value="BUY">BUY</option>
              <option value="SELL">SELL</option>
            </select>
            <select className="form-select" style={{ width: 'auto', padding: '4px 8px' }}
              value={filterStatus} onChange={e => setFilterStatus(e.target.value)}>
              <option value="ALL">All Status</option>
              <option value="PENDING">Pending</option>
              <option value="FILLED">Filled</option>
              <option value="PARTIALLY_FILLED">Partial</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
            <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
              + New Order
            </button>
          </div>
        </div>

        {loading ? (
          <div className="loading">// loading orders...</div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <div className="icon">📋</div>
            Lack of orders
          </div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th><th>Instrument</th><th>Side</th><th>Price</th>
                <th>Qty</th><th>Remaining</th><th>Region</th><th>Status</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(o => (
                <tr key={o.id}>
                  <td className="mono">{o.id}</td>
                  <td className="mono" style={{ color: 'var(--accent2)' }}>{o.instrument}</td>
                  <td><span className={`badge badge-${o.side}`}>{o.side}</span></td>
                  <td className="mono">${o.price?.toFixed(2)}</td>
                  <td className="mono">{o.quantity}</td>
                  <td className="mono" style={{ color: o.remainingQuantity < o.quantity ? 'var(--yellow)' : 'inherit' }}>
                    {o.remainingQuantity}
                  </td>
                  <td className="mono" style={{ color: 'var(--text3)' }}>{o.region}</td>
                  <td><span className={`badge badge-${o.status}`}>{o.status}</span></td>
                  <td>
                    {o.status === 'PENDING' && (
                      <button className="btn btn-danger btn-sm" onClick={() => cancel(o.id)}>
                        Cancel
                      </button>
                    )}
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