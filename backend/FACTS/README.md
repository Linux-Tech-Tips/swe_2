### Prequesites.
 - Have gradle installed.
 - For mac: `brew install gradle`
 - For fedora: `sudo dnf gradle`


## For development.
- run `gradle test`

## How to run
- Go into `/FACTS`
- run `gradle bootRun`
- go to `localhost:8080/` to see a "Welcome to FACTS" message.

### For elementary backend tests.
#### Submitting data to non-persistent database.
##### For submitting students data to the application.
-  Submit the name, and list of courses to the link `localhost:8080/api/upload/student`
- i.e `[curl localhost:8080/api/upload/student -d Ethan -d [MA284, CS326]]`

- The server will returned saved upon completion.
#### Retrieving data from the non-persistent database.
##### Retriving lecturer's data.
##### Retriving 


## Backend design
- The set up for the backend is the following.
- The stored data (Entities) are associated with repositories under Controllers.
- Controllers are mapped to a path on the network.
- Each function in the controller is mapped to an extended path.

- The provided services as of current is:
* 