#this will be run by Apple Automater when a drive is detected to have been plugged in

import sys
import os
import glob
import psycopg2
from datetime import datetime
import subprocess
import sys
import time
from os import listdir
from os.path import isfile, join



def find_most_recent_file(volume_path):
    time.sleep(5)
    onlyfiles = [f for f in listdir(volume_path) if isfile(join(volume_path, f))]
    print(f"new try {onlyfiles}")
    print(glob.glob(os.path.join(volume_path, "*")))
    # Search for txt files and return the most recent one
    list_of_files = glob.glob(os.path.join(volume_path, '*.csv'))  # Adjust the pattern as needed
    print(list_of_files)
    if not list_of_files:  # No txt files found
        return None
    latest_file = max(list_of_files, key=os.path.getctime)
    return latest_file

def list_files(path):
    try:
        files = os.listdir(path)
        print(f"Files in {path}: {files}")
    except Exception as e:
        print(f"Error listing files: {e}")


def parse_and_store(file_path, cursor):
    # AppleScript command to show a dialog
    applescript_command = f'display dialog "{file_path}"'
    
    # Execute the AppleScript command from Python
    subprocess.run(["osascript", "-e", applescript_command])
    with open(file_path, 'r') as file:
        for line in file:
            # Split the line by ';' and trim whitespace
            data = [value.strip() for value in line.split(';')]
            
            # Convert 'T'/'F' to True/False for boolean fields, including the 'disabled' field
            data = [True if value == 'T' else False if value == 'F' else value for value in data]

            # Replace any 'N/A' or other placeholders with None for NULL values in the database
            data = [None if value in ['N/A', 'No Attempt', ''] else value for value in data]


            # Convert numeric strings to integers where applicable
            for i in [0, 1, 4, 5, 13, 14, 15, 16, 18]:  # Adjust indexes if necessary
              data[i] = int(data[i])

            # Check if the "notes" section is empty (resulting in a tuple of length 20)
            if len(data) == 20:
                data.append("n/a")  # Append "n/a" as the 21st entry

            insert_query = """
            INSERT INTO public."MatchData"(
                "MATCH", "TEAM_NUMBER", "ALLIANCE", "SCOUTER",
                "AUTO_SPEAKER", "AUTO_AMP", "PICKUP_AUTO_1", "PICKUP_AUTO_2",
                "PICKUP_AUTO_3", "PICKUP_AUTO_4", "PICKUP_AUTO_5", "AUTO_STARTING_POS",
                "CROSSED_AUTO_LINE", "TELE_AMP", "TELE_SPEAKER", "TELE_AMP_MISS",
                "TELE_SPEAKER_MISS", "ENDGAME", "SCORED_TRAP", "DISABLED", "NOTES")
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT ("MATCH", "TEAM_NUMBER")
            DO UPDATE SET
                "ALLIANCE" = EXCLUDED."ALLIANCE", "SCOUTER" = EXCLUDED."SCOUTER",
                "AUTO_SPEAKER" = EXCLUDED."AUTO_SPEAKER", "AUTO_AMP" = EXCLUDED."AUTO_AMP",
                "PICKUP_AUTO_1" = EXCLUDED."PICKUP_AUTO_1", "PICKUP_AUTO_2" = EXCLUDED."PICKUP_AUTO_2",
                "PICKUP_AUTO_3" = EXCLUDED."PICKUP_AUTO_3", "PICKUP_AUTO_4" = EXCLUDED."PICKUP_AUTO_4",
                "PICKUP_AUTO_5" = EXCLUDED."PICKUP_AUTO_5", "AUTO_STARTING_POS" = EXCLUDED."AUTO_STARTING_POS",
                "CROSSED_AUTO_LINE" = EXCLUDED."CROSSED_AUTO_LINE", "TELE_AMP" = EXCLUDED."TELE_AMP",
                "TELE_SPEAKER" = EXCLUDED."TELE_SPEAKER", "TELE_AMP_MISS" = EXCLUDED."TELE_AMP_MISS",
                "TELE_SPEAKER_MISS" = EXCLUDED."TELE_SPEAKER_MISS", "ENDGAME" = EXCLUDED."ENDGAME",
                "SCORED_TRAP" = EXCLUDED."SCORED_TRAP", "DISABLED" = EXCLUDED."DISABLED", "NOTES" = EXCLUDED."NOTES";
            """

            try:
                # Execute the query
                print(len(tuple(data)))
                cursor.execute(insert_query, tuple(data))
            except Exception as e:
                print(f"Error inserting data: {e}")



if __name__ == "__main__":
        # Define your log file path
    log_file_path = "/Users/gatlinfarrington/Documents/log.txt"

    # Redirect stdout and stderr to the log file
    sys.stdout = open(log_file_path, 'a')
    sys.stderr = sys.stdout
    volume_path = sys.argv[1].rstrip(":")
    print(f"Sanitized volume path: {volume_path}")    
    volume_path = f"/Volumes/{volume_path}"
    list_files(volume_path)
    recent_file = find_most_recent_file(volume_path)
    # Connect to the database
    conn = psycopg2.connect(database="Scouting-System", user="gatlinfarrington", password="password", host="localhost", port="5432")
    cursor = conn.cursor()
    if recent_file:
        parse_and_store(recent_file, cursor)
        conn.commit()
    else:
        print("No recent file found.")
    conn.close()
