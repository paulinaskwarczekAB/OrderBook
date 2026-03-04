import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import AuthPage     from './pages/AuthPage';
import Dashboard    from './pages/Dashboard';
import OrdersPage   from './pages/OrdersPage';
import TradesPage   from './pages/TradesPage';
import ExchangesPage from './pages/ExchangesPage';
import Sidebar from './components/Sidebar';
import './index.css';

const PAGE_TITLES = {
  dashboard: 'Dashboard',
  orders:    'Orders',
  trades:    'Trades',
  exchanges: 'Exchanges',
};

function AppInner() {

    const { user, loading } = useAuth();
    const [page, setPage] = useState('dashboard');

    if (loading) {
        return (
          <div className="loading" style={{ height: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            // initializing...
          </div>
        );
      }

 if (!user) return <AuthPage />;

    const renderPage = () => {
        switch (page) {
          case 'dashboard': return <Dashboard />;
          case 'orders':    return <OrdersPage />;
          case 'trades':    return <TradesPage />;
          case 'exchanges': return <ExchangesPage />;
          default:          return <Dashboard />;
        }
      };

    return (
        <div className="app-layout">

          <Sidebar page={page} setPage={setPage} />
          <div className="main-content">
            <div className="topbar">
              <div className="topbar-title">
                TradeBook <span style={{ color: 'var(--text3)', margin: '0 6px' }}>/</span>
                <span>{PAGE_TITLES[page]}</span>
              </div>
              <div className="topbar-spacer" />

              <div className="topbar-user">
                <span className="live-dot" />
                <div className="avatar">{user.username?.charAt(0)?.toUpperCase()}</div>
                {user.username}
              </div>
            </div>

            <div className="page-scroll">
              {renderPage()}
            </div>
          </div>

        </div>
      );
    }

    export default function App() {
      return (
        <AuthProvider>
          <AppInner />
        </AuthProvider>
      );


    }