import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import MatchInput from './MatchInput';

function AddMatchPage({ onMatchesChange }) {
  return (
    <Container className="add-match-page">
      <h2>Add Match</h2>
      <MatchInput onMatchesChange={onMatchesChange} />
      <Link to="/">
        <Button variant="primary">Back to Main</Button>
      </Link>
    </Container>
  );
}

export default AddMatchPage;