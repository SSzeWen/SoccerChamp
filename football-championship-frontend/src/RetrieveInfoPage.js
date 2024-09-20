import React, { useState } from 'react';
import { Container, Form, Button, Alert, Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { auth } from './firebase';

const RetrieveInfoPage = () => {
  const [teamName, setTeamName] = useState('');
  const [info, setInfo] = useState(null);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleInputChange = (event) => {
    setTeamName(event.target.value);
  };

  const handleRetrieveInfo = () => {
    if (!teamName) {
      setError('Please enter a team name.');
      return;
    }
    const teamId = {
      email: auth.currentUser.uid, // Ensure you use email instead of uid
      teamName: teamName
    };
    console.log(JSON.stringify(teamId));
    fetch(`http://localhost:8080/api/retrieveInfo`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(teamId),
    })
      .then((response) => {
        if (!response.ok) {
          if (response.status === 404) {
            throw new Error('No Info on team. Please add the team.');
          }
          throw new Error('Network response was not ok');
        }
        
        return response.json();
      })
      .then((data) => {
        setInfo(data);
        setError('');
      })
      .catch((error) => {
        console.error('Error:', error);
        setError(error.message);
      });
  };

  const handleReturn = () => {
    navigate('/main'); // Navigate back to the previous page
  };

  return (
    <Container style={{ color: 'white' }}>
      <h2>Retrieve Information</h2>
      <Form.Group controlId="teamName">
        <Form.Label>Team Name</Form.Label>
        <Form.Control
          type="text"
          value={teamName}
          onChange={handleInputChange}
          placeholder="Enter team name"
        />
      </Form.Group>
      <div style={{ height: '15px' }}></div> {/* Add some padding */}
      <div className="d-flex justify-content-center">
      <Button variant="primary" onClick={handleRetrieveInfo}>Retrieve Info</Button>
      <div style={{ width: '15px' }}></div>
      <Button variant="primary" onClick={handleReturn} className="ml-2">Back to Main</Button>
      </div>
      {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
      {info && (
        <div className="mt-3">
          <h3>Team Details:</h3>
          <p><strong>Team Name:</strong> {info.team.teamId.teamName}</p>
          <p><strong>Registration Date:</strong> {info.team.registrationDate}</p>
          <p><strong>Group Number:</strong> {info.team.groupNumber}</p>
          <h3>Matches:</h3>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Home Team</th>
                <th>Away Team</th>
                <th>Goals Home</th>
                <th>Goals Away</th>
              </tr>
            </thead>
            <tbody>
              {info.matches.map((match, index) => (
                <tr key={index}>
                  <td>{match.matchId.teamHomeName}</td>
                  <td>{match.matchId.teamAwayName}</td>
                  <td>{match.teamHomeGoals}</td>
                  <td>{match.teamAwayGoals}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </Container>
  );
};

export default RetrieveInfoPage;