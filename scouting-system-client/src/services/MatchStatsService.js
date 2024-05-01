import { MatchStats } from '../models/MatchStats';

const apiUrl = 'http://localhost:8080/next-match'; // Adjust the URL as necessary

export async function fetchMatchStats() {
  try {
    const response = await fetch(apiUrl);
    const data = await response.json();
    return new MatchStats(...Object.values(data));
  } catch (error) {
    console.error('Error fetching match stats:', error);
    return null;
  }
}