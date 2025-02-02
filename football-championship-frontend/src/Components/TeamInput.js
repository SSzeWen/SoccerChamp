import React, { useState } from 'react';
import { Form, Button, Container, Row, Col, Alert } from 'react-bootstrap';
import { auth } from '../firebase';

function TeamInput({ onTeamsChange }) {
  const [input, setInput] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(''); // State for success message

  const handleInputChange = (e) => {
    setInput(e.target.value);
  };

  const validateInput = (lines, dateRegex, teams) => {
    for (let line of lines) {
      if (!line.trim()) {
        continue; // Skip empty lines
      }

      const [teamName, registrationDate, groupNumber] = line.split(' ');

      if (!teamName || typeof teamName !== 'string') {
        setError('Invalid team name. Please enter a valid string.');
        return false;
      }

      if (!dateRegex.test(registrationDate)) {
        setError('Invalid registration date. Please use the DD/MM format.');
        return false;
      }

      const groupNum = parseInt(groupNumber);
      if (isNaN(groupNum) || groupNum <= 0) {
        setError('Invalid group number. Please enter a positive integer.');
        return false;
      }

      // Convert registrationDate to ISO format (YYYY-MM-DD) for LocalDate
      const [day, month] = registrationDate.split('/');
      const formattedDate = `2023-${month}-${day}`; // Assuming the year is 2023
      teams.push({teamId:{email:auth.currentUser.uid, teamName:teamName}, registrationDate: formattedDate, groupNumber: groupNum});
    }
    return true
  }


  // Function to submit teams to the backend
const submitTeamsToBackend = (teams) => {
  if (teams.length > 0) {
    console.log(JSON.stringify(teams));
    
    // Send the teams to the backend
    fetch('http://localhost:8080/api/teams/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(teams),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        console.log('Success:', data);
        setSuccess('Teams successfully added!');
      })
      .catch((error) => {
        console.error('Error:', error);
        setError('Duplicate Team being added!');
        setSuccess(''); // Clear success message on error
      });
  } else {
    setSuccess(''); // Clear success message if no teams were added
  }
};

  // Function to submit teams to the backend
  const submitEditedTeamsToBackend = (teams) => {
    if (teams.length > 0) {
      console.log(JSON.stringify(teams));
      // setSuccess('Teams successfully added!');
      // Send the teams to the backend
      fetch('http://localhost:8080/api/teams/edit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(teams),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((data) => {
          console.log('Success:', data);
          setSuccess('Teams successfully added!');
        })
        .catch((error) => {
          console.error('Error:', error);
          setError('Team does not exist!');
          setSuccess(''); // Clear success message on error
        });
    } else {
      setSuccess(''); // Clear success message if no teams were added
    }
  };

  const clearAllTeams = () => {
    console.log(auth.currentUser.uid)
    fetch('http://localhost:8080/api/clearTeams', {
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
        setSuccess('All teams successfully cleared!');
        setError('');
        onTeamsChange(data);
      })
      .catch((error) => {
        console.error('Error:', error);
        setError('Failed to clear teams.');
      });
  };

  const handleSubmit = (isEdit) => {
    const lines = input.split('\n');
    const teams = [];
    const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])$/; // Regex for DD/MM format
    setError(''); // Clear error message upon new submission
    setSuccess(''); // Clear success message upon new submission
    let isValidInput = validateInput(lines, dateRegex, teams);
    if (!isValidInput) {
      return;
    }
    if (!isEdit) {
      submitTeamsToBackend(teams);
    } else {
      submitEditedTeamsToBackend(teams)
    }
    setError('');
    onTeamsChange(teams);
    setInput(''); // Clear input field only upon successful input format
  };

  return (
    <Container className="extended-container">
      <Row className="justify-content-md-center">
      <Form.Label>Enter Team Information</Form.Label>
        <Col md="8">
          {error && <Alert variant="danger">{error}</Alert>}
          {success && <Alert variant="success">{success}</Alert>}
          <Form>
            <Form.Group controlId="teamInput" style={{ padding: '15px' }}>
              <Form.Control
                as="textarea"
                rows={10}
                value={input}
                onChange={handleInputChange}
                placeholder="<Team Name> <Registration Date in DD/MM> <Group Number>"
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
              <Button variant="primary" onClick={clearAllTeams}>
                Clear Previous Teams
              </Button>
            </div>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default TeamInput;