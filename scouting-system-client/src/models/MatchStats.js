import { Team } from './Team';

export class MatchStats {
  constructor(matchNum, red1, red2, red3, blue1, blue2, blue3) {
    this.matchNum = matchNum;
    this.red1 = new Team(...Object.values(red1));
    this.red2 = new Team(...Object.values(red2));
    this.red3 = new Team(...Object.values(red3));
    this.blue1 = new Team(...Object.values(blue1));
    this.blue2 = new Team(...Object.values(blue2));
    this.blue3 = new Team(...Object.values(blue3));
  }
}