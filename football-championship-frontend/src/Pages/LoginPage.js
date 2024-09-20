import React, { useState } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from '../firebase';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (event) => {
    event.preventDefault();
    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate('/main');
    } catch (error) {
      setError(error.message);
    }
  };

  const handleSignupRedirect = () => {
    navigate('/signup');
  };

  return (
    <Container className="d-flex flex-column justify-content-center align-items-center" style={{ height: '100vh', color: 'white', padding: '20px' }}>
      <h2>Login</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleLogin} className="w-100" style={{ maxWidth: '600px' }}>
        <Form.Group controlId="email">
          <Form.Label style={{ color: 'white' }}>Email</Form.Label>
          <Form.Control
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter email"
            required
            style={{ width: '100%', maxWidth: '600px' }}
          />
        </Form.Group>
        <Form.Group controlId="password" className="mt-3">
          <Form.Label style={{ color: 'white' }}>Password</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter password"
            required
            style={{ width: '100%', maxWidth: '600px' }}
          />
        </Form.Group>
        <Button variant="primary" type="submit" className="mt-3" style={{ width: '100%', maxWidth: '600px' }}>Login</Button>
        <Button variant="secondary" onClick={handleSignupRedirect} className="mt-3" style={{ width: '100%', maxWidth: '600px' }}>Go to Signup</Button>
      </Form>
    </Container>
  );
};

export default LoginPage;