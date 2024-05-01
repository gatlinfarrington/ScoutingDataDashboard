import React, { useEffect } from 'react';
import logo from './assets/Logo_Large_White.png';
import './App.css';
import Top8Component from './components/Top8Component';
import Stack from '@mui/material/Stack';
import CompAvgerageComponent from './components/CompAverageComponent';
import MatchStatsComponent from './components/MatchStatsComponent';

function App() {
  useEffect(() => {
    const interval = setInterval(() => {
      console.log("refreshing page!!");
      window.location.reload();
    }, 60000); // 60000 ms = 60 seconds

    // Clear the interval on component unmount
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} style={{width: '40vh', paddingTop: '5vh', position:'absolute', top:'0'}} alt="Logo"/>
        <Stack direction="row" spacing={10}>
          <MatchStatsComponent />
          <Top8Component />
        </Stack>
        <div style={{position:'absolute', bottom:'0', marginBottom:'5vh'}}>
          <CompAvgerageComponent /> 
        </div>
      </header>
    </div>
  );
}

export default App;
