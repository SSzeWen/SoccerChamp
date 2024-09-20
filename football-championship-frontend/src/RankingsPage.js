import React, { useState, useEffect } from 'react';
import { Container, Alert, Table, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { auth } from './firebase';

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
    fetch('http://localhost:8080/api/rankings', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({email:auth.currentUser.uid}),
    })
      .then((response) => {
        if (!response.ok) {
          console.log(response.status);
          throw new Error('Network response was not ok');
        }
        if (response.status === 204) {
          throw new Error('No Rankings available. Please add teams and matches.');
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);
        const rankedData = addRankingToData(data);
        setRankings(rankedData);
        determineQualifyingTeams(rankedData);
      })
      .catch((error) => {
        console.error('Error:', error);
        setError(error.message);
      });
  };

  const addRankingToData = (data) => {
    const groups = {};

    // Group teams by their group number
    data.forEach(team => {
      const groupNumber = team.groupNumber;
      if (!groups[groupNumber]) {
        groups[groupNumber] = [];
      }
      groups[groupNumber].push(team);
    });

    // Sort each group by points and assign ranking
    Object.keys(groups).forEach(groupNumber => {
      groups[groupNumber].sort((a, b) => b.points - a.points);
      groups[groupNumber].forEach((team, index) => {
        team.ranking = index + 1;
      });
    });

    // Flatten the grouped data back into a single array
    return Object.values(groups).flat();
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
    navigate('/main');
  };

  return (
    <Container className="extended-container" style={{ color: 'white', paddingTop: '300px' }}>
      <h2>Rankings</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      <Table striped bordered hover variant="dark">
        <thead>
          <tr>
            <th>Rank</th>
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
              <td>{team.ranking}</td>
              <td>{team.teamName}</td>
              <td>{team.groupNumber}</td>
              <td>{team.points}</td>
              <td>{team.goalsFor}</td>
              <td>{team.goalsAgainst}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <h2>Qualifying Teams</h2>
      <Table striped bordered hover variant="dark">
        <thead>
          <tr>
            <th>Rank</th>
            <th>Team Name</th>
            <th>Group Number</th>
          </tr>
        </thead>
        <tbody>
          {qualifyingTeams.map((team, index) => (
            <tr key={index}>
              <td>{team.ranking}</td>
              <td>{team.teamName}</td>
              <td>{team.groupNumber}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Button variant="primary" onClick={handleReturnToMain}>Back to Main</Button>
    </Container>
  );
};

export default RankingsPage;