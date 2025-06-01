// src/App.tsx
import React from 'react';
import LeftPanel from './LeftPanel';

const App: React.FC = () => {
  return (
      <div style={{ display: 'flex', flexDirection: 'row' }}>
        <LeftPanel />
        <div style={{ width: '50vw' }}>
          {/* Right panel or analytics placeholder */}
        </div>
      </div>
  );
};

export default App;
