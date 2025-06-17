import React, { useState } from 'react';

interface RegistroProps {
  onClose: () => void;
  onRegisterSuccess?: (usuario: { usuario: string; email: string }) => void;
}

const Registro: React.FC<RegistroProps> = ({ onClose, onRegisterSuccess }) => {
  const [usuario, setUsuario] = useState('');
  const [email, setEmail] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!usuario.trim() || !email.trim() || !contrasena.trim()) {
      setError('Por favor, completa todos los campos.');
      return;
    }

    if (usuario.length < 3 || usuario.length > 50) {
      setError('El usuario debe tener entre 3 y 50 caracteres.');
      return;
    }

    if (contrasena.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres.');
      return;
    }

    // Simula éxito de registro local
    alert(`Usuario ${usuario} registrado con éxito!`);
    setError('');
    onRegisterSuccess?.({ usuario, email });
    onClose();
  };

  return (
    <div style={{ padding: '20px', width: '320px', fontFamily: 'Arial, sans-serif' }}>
      <h3>Registrarse</h3>
      {error && (
        <p role="alert" style={{ color: 'red', marginBottom: '10px' }}>
          {error}
        </p>
      )}
      <form onSubmit={handleSubmit} noValidate>
        <div style={{ marginBottom: '12px' }}>
          <label htmlFor="usuario">Usuario:</label><br />
          <input
            id="usuario"
            type="text"
            value={usuario}
            onChange={e => setUsuario(e.target.value)}
            required
            minLength={3}
            maxLength={50}
            autoFocus
            style={{ width: '100%', padding: '6px', boxSizing: 'border-box' }}
          />
        </div>
        <div style={{ marginBottom: '12px' }}>
          <label htmlFor="email">Correo electrónico:</label><br />
          <input
            id="email"
            type="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
            style={{ width: '100%', padding: '6px', boxSizing: 'border-box' }}
          />
        </div>
        <div style={{ marginBottom: '12px' }}>
          <label htmlFor="contrasena">Contraseña:</label><br />
          <input
            id="contrasena"
            type="password"
            value={contrasena}
            onChange={e => setContrasena(e.target.value)}
            required
            minLength={6}
            style={{ width: '100%', padding: '6px', boxSizing: 'border-box' }}
          />
        </div>
        <div>
          <button type="submit" style={{ marginRight: '10px', padding: '8px 16px' }}>
            Registrarse
          </button>
          <button type="button" onClick={onClose} style={{ padding: '8px 16px' }}>
            Cerrar
          </button>
        </div>
      </form>
    </div>
  );
};

export default Registro;