import React, { useState, useEffect } from 'react';
import { Container, Alert, Table, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const RankingsPage = () => {
  const [rankings, setRankings] = useState([]);
  const [qualifyingTeams, setQualifyingTeams] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchRankingsFromBackend();
  }, []);

  const fetchRankingsFromBackend = () => {
    fetch('http://localhost:8080/api/rankings')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);
        setRankings(data);
        determineQualifyingTeams(data);
        setSuccess('Rankings successfully fetched!');
      })
      .catch((error) => {
        console.error('Error:', error);
        setError('Failed to fetch rankings from the backend.');
      });
  };

  const determineQualifyingTeams = (rankings) => {
    const groups = {};

    // Group teams by their group number
    rankings.forEach(team => {
      const groupNumber = team.groupNumber;
      if (!groups[groupNumber]) {
        groups[groupNumber] = [];
      }
      groups[groupNumber].push(team);
    });

    const qualifyingTeams = [];

    // Determine top 2 teams from each group
    Object.keys(groups).forEach(groupNumber => {
      const groupTeams = groups[groupNumber];
      qualifyingTeams.push(...groupTeams.slice(0, 2));
    });

    setQualifyingTeams(qualifyingTeams);
  };

  const handleReturnToMain = () => {
    navigate('/');
  };

  return (
    <Container className="extended-container">
      <h2>Rankings</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Team Name</th>
            <th>Group Number</th>
            <th>Points</th>
            <th>Goals For</th>
            <th>Goals Against</th>
          </tr>
        </thead>
        <tbody>
          {rankings.map((team, index) => (
            <tr key={index}>
              <td>{team.name}</td>
              <td>{team.groupNumber}</td>
              <td>{team.points}</td>
              <td>{team.goalsFor}</td>
              <td>{team.goalsAgainst}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <h2>Qualifying Teams</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Team Name</th>
            <th>Group Number</th>
          </tr>
        </thead>
        <tbody>
          {qualifyingTeams.map((team, index) => (
            <tr key={index}>
              <td>{team.name}</td>
              <td>{team.groupNumber}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Button variant="primary" onClick={handleReturnToMain}>Return to Main</Button>
    </Container>
  );
};

export default RankingsPage;