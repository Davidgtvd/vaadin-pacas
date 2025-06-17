import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import PaginaPrincipal from './PaginaPrincipal';
import Registro from './Registro';
import Login from './Login';

const App: React.FC = () => {
  return (
    <Router>
      <nav>
        <Link to="/">Inicio</Link> | <Link to="/registro">Registrarse</Link> | <Link to="/login">Iniciar Sesión</Link>
      </nav>
      <Routes>
        <Route path="/" element={<PaginaPrincipal />} />
        <Route path="/registro" element={<Registro onClose={() => {}} />} />
        <Route path="/login" element={<Login onClose={() => {}} />} />
      </Routes>
    </Router>
  );
};

export default App;