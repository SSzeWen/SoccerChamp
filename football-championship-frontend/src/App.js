import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import { Button, Container } from 'react-bootstrap';
import TeamDetails from './TeamDetails';
import RankingsPage from './RankingsPage';
import AddTeamPage from './AddTeamPage';
import AddMatchPage from './AddMatchPage';
import './index.css';

function App() {
  const [teams, setTeams] = useState([]);
  const [matches, setMatches] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState(null);

  const handleTeamsChange = (newTeams) => {
    setTeams(newTeams);
  };

  const handleMatchesChange = (newMatches) => {
    setMatches(newMatches);
  };

  const handleTeamSelect = (teamName) => {
    setSelectedTeam(teamName);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={
          <Container className="main-container">
            <h1>GovTech Football Championship</h1>
            <Link to="/add-team">
              <Button variant="primary">Add Team</Button>
            </Link>
            <Link to="/add-match">
              <Button variant="primary">Add Match</Button>
            </Link>
            <Link to="/rankings">
              <Button variant="primary">Rankings</Button>
            </Link>
            {selectedTeam && <TeamDetails teamName={selectedTeam} teams={teams} matches={matches} />}
          </Container>
        } />
        <Route path="/rankings" element={
          <RankingsPage teams={teams} matches={matches} onTeamSelect={handleTeamSelect} />
        } />
        <Route path="/add-team" element={
          <AddTeamPage onTeamsChange={handleTeamsChange} />
        } />
        <Route path="/add-match" element={
          <AddMatchPage onMatchesChange={handleMatchesChange} />
        } />
      </Routes>
    </Router>
  );
}

export default App;