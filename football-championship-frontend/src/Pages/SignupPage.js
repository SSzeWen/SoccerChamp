import React, { useState } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth } from '../firebase';

const SignupPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSignup = async (event) => {
    event.preventDefault();
    try {
      await createUserWithEmailAndPassword(auth, email, password);
      navigate('/main');
    } catch (error) {
      setError(error.message);
    }
  };

  const handleSkip = () => {
    navigate('/login');
  };

  return (
    <Container className="d-flex flex-column justify-content-center align-items-center" style={{ height: '100vh', color: 'white', padding: '20px' }}>
      <h2>Sign Up</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSignup} className="w-100" style={{ maxWidth: '600px' }}>
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
        <Button variant="primary" type="submit" className="mt-3" style={{ width: '100%', maxWidth: '600px' }}>Sign Up</Button>
        <Button variant="secondary" onClick={handleSkip} className="mt-3" style={{ width: '100%', maxWidth: '600px' }}>Skip</Button>
      </Form>
    </Container>
  );
};

export default SignupPage;