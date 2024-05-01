# Scouting Data Dashboard

The Scouting Data Dashboard is a comprehensive tool designed for FIRST Robotics Competition (FRC) teams to manage scouting data effectively. This project is divided into three main components: the client, the server, and the worker scripts. This project was initially developed for Team 9432, but we are happy to open it up to the public, allowing other team's to modify it to work with their own Scouting Systems.

## Components

### 1. Scouting System Client (`scouting-system-client`)

The client side of the dashboard is a React application that provides a user-friendly interface to display and interact with the scouting data.

#### Getting Started
To set up and run the client:
1. Navigate to the `scouting-system-client` directory:
   ```bash
   cd scouting-system-client
2. Install the necesarry packages using npm:
   ```bash
   npm install
3. Start the Application:
   ```bash
   npm start
The application will be available at 'http://localhost:3000'

### Scouting System Server ('Scouting-System-Server')
The server is built with Kotlin using the ktor framework. It handles API rrequests fromt he client and interacts with PostgresSQL database to fetch or update data.

####Running the server
To launch the server:
1. Navigate to the 'Scouting-System-Server' direcotry
   ```bash
   cd Scouting-System-Server
2. Run the server using gradle
   ```bash
   ./gradlew run
The server will start and lsiten for requests on its configured port

### Scouting System Worker ('scouting-system-worker')
The worker consists of two Python scripts:

- Setup.py fills the database with initial data such as team names, number, and the competition schedule. You will need to modify this script to use your own API key and ensure it is pulling data from the event you are participating at
- Worker.py is designed to be run via Apple Automator, but other Automation software available on other Operating Systems should work as well.

1. To run Setup.py:
   ```bash
   python3 Setup.py
2. To set up 'Worker.py'

   - Open Apple Automator and create a new "Folder Action" script
   - Set the folder to watch as '/Volumes' (this corresponds to mounted drives)
   - Add a "Run Shell Script" Action and unclude this command: "python3 path/to/Worker.py \<expected file\>" Where expected file is a .csv holding your data
   - Save and activate the Automator Action


![image](https://github.com/gatlinfarrington/ScoutingDataDashboard/assets/72674932/1f2a977d-da60-4fd6-99e4-467e1986c985)

### Future Plans:
- The Setup.py script should allow for event info to be passes in as a Command Line argument when running the script, eliminating the need to modify the script between events
- The KTOR API should be modified to use an ORM, allowing for different teams to easily swap database systems
- The API can be improved by serving more data, less frequently, and endpoints should be organized in controllers as the functionality grows.
- The Client Application has a known issue where it does not work on safari, API requests are never properly received
- Project naming shoudl be more consistent, Server proejct has each word capitalized, whereas other two proejcts are lowercase.
- Adding a coding standard for each language is on the Radar for Team 9432, once these standards are implemented, formatting will change as is applicable.
