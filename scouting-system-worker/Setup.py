#API INFORMATION

#username: gatlinf
#auth_token: e2504182-5ef1-47a4-8041-144f731ed1f0

#event codes:
#AZ Valley: AZVA
#AZ East: AZGL

#conda create --name scouting-sys python=3.9
#conda activate scouting-sys
#pip install requests psycopg2-binary

import requests
import psycopg2

def main():
    # Set up database connection
    conn = psycopg2.connect(database="Scouting-System", user="gatlinfarrington", password="password", host="localhost", port="5432")
    cursor = conn.cursor()

    # Fetch and process team list
    tournament = "CAVE"
    team_list_response = requests.get(f"https://frc-api.firstinspires.org/v3.0/2024/teams?teamNumber&eventCode={tournament}&districtCode&state&page", auth=("gatlinf", "e2504182-5ef1-47a4-8041-144f731ed1f0"))
    processed_teams = process_team_list(team_list_response.json())
    store_teams_data(processed_teams, cursor)

    # Fetch and process match list
    clear_records(cursor)
    tournament = "CAVE"
    match_list_response = requests.get(f"https://frc-api.firstinspires.org/v3.0/2024/matches/{tournament}?tournamentLevel=Qual&teamNumber&matchNumber&start&end", auth=("gatlinf", "e2504182-5ef1-47a4-8041-144f731ed1f0"))
    processed_matches = process_match_list(match_list_response.json())
    store_matches_data(processed_matches, cursor)

    # Commit changes and close the database connection
    conn.commit()
    cursor.close()
    conn.close()




def process_team_list(api_response):
    teams_data = api_response.get("teams", [])
    processed_teams = [{"team_number": team["teamNumber"], "team_name": team["nameShort"]} for team in teams_data]
    return processed_teams

def process_match_list(api_response):
    matches_data = api_response.get("Matches", [])
    processed_matches = []
    for match in matches_data:
        match_info = {
            "match_num": match["matchNumber"],
            "red_1": match["teams"][0]["teamNumber"],
            "red_2": match["teams"][1]["teamNumber"],
            "red_3": match["teams"][2]["teamNumber"],
            "blue_1": match["teams"][3]["teamNumber"],
            "blue_2": match["teams"][4]["teamNumber"],
            "blue_3": match["teams"][5]["teamNumber"],
        }
        processed_matches.append(match_info)
    return processed_matches

def store_teams_data(teams_data, cursor):
    for team in teams_data:
        try:
            insert_query = """INSERT INTO public."Teams"("TEAM_NUMBER", "TEAM_NAME") VALUES (%s, %s) ON CONFLICT ("TEAM_NUMBER") DO NOTHING;"""
            cursor.execute(insert_query, (team["team_number"], team["team_name"]))
        except Exception as e:
            print(f"Error inserting team data: {e}")

def store_matches_data(matches_data, cursor):
    for match in matches_data:
        try:
            insert_query = """INSERT INTO public."MatchSchedule"("MATCH_NUM", "RED_1", "RED_2", "RED_3", "BLUE_1", "BLUE_2", "BLUE_3") VALUES (%s, %s, %s, %s, %s, %s, %s) ON CONFLICT ("MATCH_NUM") DO NOTHING;"""
            cursor.execute(insert_query, (match["match_num"], match["red_1"], match["red_2"], match["red_3"], match["blue_1"], match["blue_2"], match["blue_3"]))
        except Exception as e:
            print(f"Error inserting match data: {e}")

def clear_records(cursor):
    try:
        query = """DELETE FROM public."MatchSchedule"; """
        cursor.execute(query)
    except Exception as e:
        print("didn't work?")

if __name__ == "__main__":
    main()