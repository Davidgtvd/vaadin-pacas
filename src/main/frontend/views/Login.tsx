interface LoginProps {
  onClose: () => void;
}

const Login: React.FC<LoginProps> = ({ onClose }) => {
  return (
    <div>
      {/* Your login form or content goes here */}
      <button onClick={onClose}>Close</button>
    </div>
  );
};
export default Login;