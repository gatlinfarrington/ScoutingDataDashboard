import CompAverageModel from '../models/CompAverageModel';

const apiUrl = 'http://0.0.0.0:8080/comp-avgs'; // Ensure this is the url on your machine when you run the API

async function fetchCompAvgs() {
  console.log("Trying to fetch!")
  try {
    const response = await fetch(apiUrl); //I am having an issue where this request never makes it to the API... Not sure what is causing it. Didn't have time to look any further.
    console.log('Response:', response);

    const data = await response.json();
    console.log('Data:', data);
    
    return new CompAverageModel(data.AvgAutoSpeaker, data.AvgAutoAmp, data.AvgSpeaker, data.AvgAmp, data.AvgClimb, data.AvgTrap);
    
  } catch (error) {
    console.error('Error fetching Top 8 list:', error);
    return [];
  }
}



export default fetchCompAvgs;