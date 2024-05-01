import React, { useState, useEffect } from 'react';
import fetchTop8List from '../services/Top8Service';
import { Stack } from '@mui/material';
import Box from '@mui/material/Box';


function Top8Component() {
  const [top8List, setTop8List] = useState([]);

  useEffect(() => {
    async function fetchData() {
      const data = await fetchTop8List();
      setTop8List(data);
    }
    fetchData();
  }, []);

  return (
    <div>
      <h2>Team 8-Bit's Top 8</h2>
      <Box>
        <Stack spacing={3}>
          {top8List.map((team, index) => (
            <div key={index}>
              <strong>{team.ranking}: {team.name}</strong> - Avg Speaker: {team.avgSpeaker}, Avg Amp: {team.avgAmp}
            </div>
          ))}
        </Stack>
      </Box>
    </div>
  );
}

export default Top8Component;
