import React, { useState, useEffect } from 'react';
import { fetchMatchStats } from '../services/MatchStatsService';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, createTheme } from '@mui/material';

import { red, blue } from '@mui/material/colors';

function MatchStatsComponent() {
  const [matchStats, setMatchStats] = useState(null);

  useEffect(() => {
    async function fetchData() {
      const data = await fetchMatchStats();
      setMatchStats(data);
    }
    fetchData();
  }, []);

  if (!matchStats) return <div>Loading...</div>;

  // Styles
  const cellStyle = (index) => ({
    color:"#eaeaea",
    backgroundColor: index < 3 ? 'rgba(255, 0, 0, 0.3)' : 'rgba(0, 0, 255, 0.3)', // Using Material-UI colors
    fontWeight: '',
    fontSize: '2rem', // Larger text
  });
  const firstCellStyle = (index) => ({
    color: '#eaeaea', // Using Material-UI colors
    fontWeight: '',
    fontSize: '2rem', // Larger text
  });


  const headerStyle = { 
    color: 'white',
    fontWeight: 'bold',
    fontSize: '5 rem', // Even larger text for headers
  };

  return (
    <div>
      <h2 style={{marginBottom:"1vh"}}>Our Next Match</h2>
      <p style={{padding:"-1vh", margin:"-1vh"}}>Match {matchStats.matchNum}</p>
    <TableContainer component={Paper} style={{ backgroundColor: 'transparent' }}>
      <Table aria-label="match stats table">
      
        <TableBody>
        <TableRow key={"first"}>
              <TableCell align="left" style={firstCellStyle(1)}>Team Name</TableCell>
              <TableCell align="right" style={firstCellStyle(1)}>Team Number</TableCell>
              <TableCell align="right" style={firstCellStyle(1)}>Avg Speaker</TableCell>
              <TableCell align="right" style={firstCellStyle(1)}>Avg Amp</TableCell>
              <TableCell align="right" style={firstCellStyle(1)}>Climb %</TableCell>
              <TableCell align="right" style={firstCellStyle(1)}>Avg Trap</TableCell>
            </TableRow>
          {[matchStats.red1, matchStats.red2, matchStats.red3, matchStats.blue1, matchStats.blue2, matchStats.blue3].map((team, index) => (
            <TableRow key={index}>
              <TableCell component="th" scope="row" style={cellStyle(index)}>
                {team.teamName}
              </TableCell>
              <TableCell align="right" style={cellStyle(index)}>{team.teamNum}</TableCell>
              <TableCell align="right" style={cellStyle(index)}>{team.avgSpeaker}</TableCell>
              <TableCell align="right" style={cellStyle(index)}>{team.avgAmp}</TableCell>
              <TableCell align="right" style={cellStyle(index)}>{team.avgClimb}</TableCell>
              <TableCell align="right" style={cellStyle(index)}>{team.avgTrap}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    </div>
  );
}

export default MatchStatsComponent;
