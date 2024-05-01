import React, { useState, useEffect } from 'react';
import { Stack } from '@mui/material';
import Box from '@mui/material/Box';
import fetchCompAvgs from '../services/CompAverageService';

function CompAvgerageComponent() {
  const [compAvgs, setCompAvgs] = useState(null); // Initialize to null since we're expecting an object

  useEffect(() => {
    async function fetchData() {
      const data = await fetchCompAvgs();
      setCompAvgs(data);
    }
    fetchData();
  }, []);

  return (
    <div>
      <h2>Competition Averages</h2>
      <Box>
        {compAvgs ? (
          <Stack direction='row' spacing={3}>
            <div>Avg Auto Speaker: {compAvgs.AvgAutoSpeaker}</div>
            <div>Avg Auto Amp: {compAvgs.AvgAutoAmp}</div>
            <div>Avg Speaker: {compAvgs.AvgSpeaker}</div>
            <div>Avg Amp: {compAvgs.AvgAmp}</div>
            <div>Avg Climb: {compAvgs.AvgClimb}</div>
            <div>Avg Trap: {compAvgs.AvgTrap}</div>
          </Stack>
        ) : (
          <div>Could Not fetch Comp Avg Data</div> // Display loading or some placeholder text until data is fetched
        )}
      </Box>
    </div>
  );
}

export default CompAvgerageComponent;
