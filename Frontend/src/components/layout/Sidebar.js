import React from 'react';
import { FiPlay, FiClock, FiFolder } from 'react-icons/fi';

export default function Sidebar({ activeTab, setActiveTab }) {
  // Menu items
  const menuItems = [
    { id: 'execute', icon: FiPlay, label: 'Execute Request' },
    { id: 'history', icon: FiClock, label: 'History' },
    { id: 'collections', icon: FiFolder, label: 'Collections' },
  ];

  return (
    <div className="bg-light p-3" style={{ height: '100%' }}>
      <h5 className="mb-3">Menu</h5>
      <ul className="list-unstyled">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <li key={item.id} className="mb-2">
              <button
                className={`btn w-100 text-start d-flex align-items-center gap-2 ${
                  activeTab === item.id ? 'btn-primary' : 'btn-outline-secondary'
                }`}
                onClick={() => setActiveTab(item.id)}
              >
                <Icon size={18} />
                {item.label}
              </button>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
