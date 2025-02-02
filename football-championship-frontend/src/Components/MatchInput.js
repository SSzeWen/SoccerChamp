import React, { useState } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { auth } from '../firebase';
import { json } from 'react-router-dom';

function MatchInput({ onMatchesChange }) {
  const [input, setInput] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(''); // State for success message

  const handleInputChange = (e) => {
    setInput(e.target.value);
  };

  const validateInput = (lines, matches) => {
    for (let line of lines) {
      const [teamHomeName, teamAwayName, goalsHome, goalsAway] = line.split(' ');

      // Must be String names
      if (!teamHomeName || typeof teamHomeName !== 'string' || !teamAwayName || typeof teamAwayName !== 'string') {
        setError('Invalid team names. Team names must be non-empty strings.');
        return false;
      }

      // Cannot fight itself
      if (teamHomeName == teamAwayName) {
        setError('Same team cannot play against itself.');
        return false;
      }
      
      // Cannot have negative goals or non-numeric goals
      if (!goalsHome || isNaN(goalsHome) || goalsHome < 0 || !goalsAway || isNaN(goalsAway) || goalsAway < 0) {
        setError('Invalid goals. Goals must be positive numbers.');
        return false;
      }
      matches.push({ matchId:{email:auth.currentUser.uid, teamHomeName: teamHomeName, teamAwayName: teamAwayName}, 
        teamHomeGoals: parseInt(goalsHome), teamAwayGoals: parseInt(goalsAway) });
    }
    return true;
  };

  
  // Function to submit teams to the backend
const submitMatchesToBackend = (matches) => {
  if (matches.length > 0) {
    console.log(JSON.stringify(matches));
    // Send the teams to the backend
    fetch('http://localhost:8080/api/matches', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(matches),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        console.log('Success:', data);
        setSuccess('Matches successfully added!');
      })
      .catch((error) => {
        console.error('Error:', error);
        setError("Error: Please check if the teams exist and if the match results are duplicated.");
        setSuccess(''); // Clear success message on error
      });
  } else {
    setSuccess(''); // Clear success message if no teams were added
  }
};

const submitEditedatchesToBackend = (matches) => {
  if (matches <= 0) {
    return;
  }
  fetch('http://localhost:8080/api/editMatches', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(matches),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then((data) => {
      setSuccess('Matches successfully edited!');
      setError('');
      onMatchesChange(data);
    })
    .catch((error) => {
      console.error('Error:', error);
      setError('Failed to edit matches.');
      setSuccess('');
    });
}

const clearAllMatches = () => {
  fetch('http://localhost:8080/api/clearMatches', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({email:auth.currentUser.uid}),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response;
    })
    .then((data) => {
      console.log(data);
      setSuccess('All matches successfully cleared!');
      setError('');
      onMatchesChange(data);
    })
    .catch((error) => {
      console.error('Error:', error);
      setError('Failed to clear matches.');
      setSuccess('');
    });
};
  

  const handleSubmit = (isEdit) => {
    const lines = input.split('\n');
    const matches = [];
    if (!validateInput(lines, matches)) {
      return;
    }
    if (!isEdit) {
      submitMatchesToBackend(matches);
    } else {
      submitEditedatchesToBackend(matches);
    }

    // setSuccess('Teams successfully added!');
    setError('');
    onMatchesChange(matches);
    setInput(''); // Clear input field only upon successful input format
  };

  return (
    <Container className="extended-container">
      <h2>Enter Match Results</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}
      <Form>
        <Form.Group controlId="matchInput" style={{ padding: '15px' }}>
        <Form.Control
          as="textarea"
          rows={10}
          value={input}
          onChange={handleInputChange}
          placeholder="<Team A> <Team B> <Goals A> <Goals B>"
        />
        </Form.Group>
        <div className="d-flex justify-content-center">
          <Button variant="primary" onClick={() => handleSubmit(false)}>
            Submit
          </Button>
          <div style={{ width: '15px' }}></div>
          <Button variant="primary" onClick={() => handleSubmit(true)}>
            Edit
          </Button>
          <div style={{ width: '15px' }}></div>
          <Button variant="primary" onClick={clearAllMatches}>Clear Previous Matches</Button>
        </div>
        </Form>
        </Container>
        );
}

export default MatchInput;