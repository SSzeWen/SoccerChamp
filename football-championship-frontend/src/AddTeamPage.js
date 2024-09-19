import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import TeamInput from './TeamInput';

function AddTeamPage({ onTeamsChange }) {
  return (
    <Container className="add-team-page">
      <h2>Add Team</h2>
      <TeamInput onTeamsChange={onTeamsChange} />
      <Link to="/">
        <Button variant="primary">Back to Main</Button>
      </Link>
    </Container>
  );
}

export default AddTeamPage;