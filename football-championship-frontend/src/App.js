import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import { Button, Container } from 'react-bootstrap';
import { onAuthStateChanged } from "firebase/auth";
import { auth } from './firebase';
import LoginPage from './Pages/LoginPage';
import SignupPage from './Pages/SignupPage';
import RankingsPage from './Pages/RankingsPage';
import AddTeamPage from './Pages/AddTeamPage';
import AddMatchPage from './Pages/AddMatchPage';
import RetrieveInfoPage from './Pages/RetrieveInfoPage';
import './index.css';

function App() {
  const [teams, setTeams] = useState([]);
  const [matches, setMatches] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUser(user);
    });
    return unsubscribe;
  }, []);

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
        <Route path="/" element={<Navigate to="/signup" />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/main" element={
          user ? (
            <Container className="main-container">
              <h1>GovTech Football Championship</h1>
              <Link to="/add-team">
                <Button variant="primary">Add Team</Button>
              </Link>
              <Link to="/add-match">
                <Button variant="primary">Add Match</Button>
              </Link>
              <Link to="/retrieve-info">
                <Button variant="primary">Retrieve Info</Button>
              </Link>
              <Link to="/rankings">
                <Button variant="primary">Rankings</Button>
              </Link>
            </Container>
          ) : (
            <Navigate to="/login" />
          )
        } />
        <Route path="/rankings" element={
          user ? (
            <RankingsPage teams={teams} matches={matches} onTeamSelect={handleTeamSelect} />
          ) : (
            <Navigate to="/login" />
          )
        } />
        <Route path="/add-team" element={
          user ? (
            <AddTeamPage onTeamsChange={handleTeamsChange} />
          ) : (
            <Navigate to="/login" />
          )
        } />
        <Route path="/add-match" element={
          user ? (
            <AddMatchPage onMatchesChange={handleMatchesChange} />
          ) : (
            <Navigate to="/login" />
          )
        } />
        <Route path="/retrieve-info" element={
          user ? (
            <RetrieveInfoPage />
          ) : (
            <Navigate to="/login" />
          )
        } />
      </Routes>
    </Router>
  );
}

export default App;