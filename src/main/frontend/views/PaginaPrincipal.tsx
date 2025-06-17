import React, { useState } from 'react';
import { Button, Dialog } from '@vaadin/react-components';
import Login from './Login';
import Registro from './Registro';

const PaginaPrincipal: React.FC = () => {
  const [mostrarLogin, setMostrarLogin] = useState(false);
  const [mostrarRegistro, setMostrarRegistro] = useState(false);

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <header
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '30px',
        }}
      >
        <h1>Tienda de Ropa Online</h1>
        <div>
          <Button theme="primary" onClick={() => setMostrarRegistro(true)}>
            Registrarse
          </Button>
          <Button
            theme="primary"
            onClick={() => setMostrarLogin(true)}
            style={{ marginLeft: 10 }}
          >
            Iniciar Sesión
          </Button>
        </div>
      </header>

      <main>
        <h2>Productos Destacados</h2>
        {/* Aquí puedes agregar la lista o galería de productos local */}
      </main>

      <Dialog
        aria-label="Iniciar Sesión"
        opened={mostrarLogin}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) =>
          setMostrarLogin(e.detail.value)
        }
        draggable
      >
        <Login onClose={() => setMostrarLogin(false)} />
      </Dialog>

      <Dialog
        aria-label="Registro de Usuario"
        opened={mostrarRegistro}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) =>
          setMostrarRegistro(e.detail.value)
        }
        draggable
      >
        <Registro onClose={() => setMostrarRegistro(false)} />
      </Dialog>
    </div>
  );
};

export default PaginaPrincipal;