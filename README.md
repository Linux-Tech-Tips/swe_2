# Tutorial Time

## Project purpose and overview
Tutorial Time is an application made for the CT5106 Software Engineering 2 module. Its purpose is to make scheduling labs or tutorials in a university setting easier.  

Features:  
- Two types of users, Students and Lecturers
- Secure Login via an external OAuth provider, which can be easily switched to the authorizer the given university uses
- Students can input their timetable to store their availability data
- Lecturers can create a module with a module code and a list of student IDs
- Lecturers can modify the list of students associated with a module
- Lecturers can also add students marked as TAs to a module, so that only times when TAs are available are considered by the scheduler
- Lecturers can schedule tutorials for a given module, for which the application finds one or more times that allow for all students registered for the module to attend at least one tutorial each week
- Lecturers can add constraints to the scheduling, such as a maximum number of students or a minimum number of TAs

## Using the application
The optimal way to use Tutorial Time is over the hosted version at https://frontend.unixusergroup.org. Navigate to this website, and follow the instructions to create a new account (or login if you already have one). Depending on your user type, you will be redirected to a page where you can enter your timetable (student) or where you can manage modules (lecturer).

## Running the app locally
Running the app locally is described in more detail in `backend/README.md` and in `frontend/README.md`.  
If you want to run the app locally, you must have the following dependencies installed: 
- node
- react dependencies
- gradle
- docker
- docker-compose (or docker compose)

If you are using a mac, you must also install UHHHH to configure a daemon and ensure that your docker instance is epxosing its ports to your machine.

You must also edit the local configuration files. 
In `/backend/tutorial-time/src/main/java/org/unixusergroup/tutorial_time/infrastructure/ProjectConfiguration.java` edit the variable `FRONTEND_DOMAIN_NAME` to be `http://localhost:5173/`.
In `/frontend/src/services/BackendConfig.jsx` edit the variable 

To start the application, run `docker-compose up --build` in `backend/tutorial-time`. Then navigate to the `frontend` directory and run `npm run dev`. Now go to localhost:5173 to see the web page.

## Documentation and CI
To run a jenkins build and see the test and code coverage reports, navigate to https://ci.unixusergroup.org.
Our documentation is hosted at https://docs.unixusergroup.org.

## Project structure
The repository is divided into the following directories: 
- frontend: all frontend code for the project, the structure of this directory is described in its README.md
- backend: all backend code for the project, the structure of this directory is described in its README.md and it follows clean architecture principles
- infra: the infrastructure set up for hosting
- doc: documentation, this includes an explanation of our Clean Architecture structure, our ubiquitous language, the tech stack, and a mathematical description of the Scheduling Algorithm
- org: organisation, this includes assignments, meeting notes, and agendas

