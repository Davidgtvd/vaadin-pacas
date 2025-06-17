import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import PaginaPrincipal from './PaginaPrincipal';
import Registro from './Registro';
import Login from './Login';

const App: React.FC = () => {
  return (
    <Router>
      <nav style={{ padding: '1rem', backgroundColor: '#f0f0f0' }}>
        <Link to="/" style={{ marginRight: '1rem' }}>Inicio</Link>
        <Link to="/registro" style={{ marginRight: '1rem' }}>Registrarse</Link>
        <Link to="/login">Iniciar Sesión</Link>
      </nav>
      <main style={{ padding: '1rem' }}>
        <Routes>
          <Route path="/" element={<PaginaPrincipal />} />
          <Route path="/registro" element={<Registro onClose={() => {}} />} />
          <Route path="/login" element={<Login onClose={() => {}} />} />
        </Routes>
      </main>
    </Router>
  );
};

export default App;