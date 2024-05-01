import Top8Model from '../models/Top8Model';

const apiUrl = 'http://0.0.0.0:8080/top-8'; // Ensure this is the url on your machine when you run the API

async function fetchTop8List() {
  console.log("Trying to fetch!")
  try {
    const response = await fetch(apiUrl); //I am having an issue where this request never makes it to the API... Not sure what is causing it. Didn't have time to look any further.
    console.log('Response:', response);

    const data = await response.json();
    console.log('Data:', data);

    if (Array.isArray(data.teams)) {
      return data.teams.map(item => new Top8Model(item.name, item.avgSpeaker, item.avgAmp, item.ranking));
    } else {
      console.error('Expected "teams" array not found in response:', data);
      return [];
    }
  } catch (error) {
    console.error('Error fetching Top 8 list:', error);
    return [];
  }
}



export default fetchTop8List;
