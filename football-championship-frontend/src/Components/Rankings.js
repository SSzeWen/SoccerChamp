import React from 'react';
import { Table } from 'react-bootstrap';

function Rankings({ teams }) {
  return (
    <div>
      <h2>Team Rankings</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Team Name</th>
            <th>Points</th>
          </tr>
        </thead>
        <tbody>
          {teams.map((team, index) => (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{team.name}</td>
              <td>{team.points}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}

export default Rankings;