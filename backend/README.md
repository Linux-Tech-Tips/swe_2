# Tutorial Time Project Backend

This folder contains the backend files, source codes, configuration and notes for the Tutorial Time (TT) project.

## Folder Structure

The folders within the `tutorial-time` directory contain the source codes, gradle configuration and, upon building, the gradle build files (which aren't in the repo).
The source codes, contained within the `tutorial-time/src` folder, are organised in accordance with the Clean architecture.

The folder itself has 2 subfolders, `main` and `test`.

### main

The `main` directory contains the main project source codes, contained within the nested folder `main/java/org/unixusergroup/tutorial_time/`.

The root folder, `tutorial_time`, contains the main file, `TutorialTimeApplication.java`, and subfolders split based on the Clean architecture.

The subfolders are the following:

- `domain`
  - the core folder, containing the entities and their core business logic;
  - inner-most layer, independent of Spring and any other project files.
- `application`
  - the folder containing the use cases, which wrap domain/entity logic with specific tasks (use cases);
  - independent of Spring, depends on `domain`.
- `infrastructure`
  - the folder containing database and infrastructure communication logic files, a concrete implementation of the domain's repository system;
  - implements interfaces from `domain`, independent of Spring.
- `api`
  - the outermost presentation layer, containing the REST controllers for the use cases, using specific infrastructure;
  - depends on Spring and the internal application and infrastructure layers.

### test

The `test` directory contains test source codes, with unit tests and E2E tests for the application.

The nested folder `test/java/org/unixusergroup/tutorial_time/` contains the various test source classes.

## Prerequisties

Have `gradle` installed:

- for mac: `brew install gradle`
- for fedora: `sudo dnf install gradle`

Have `docker` and `docker compose` installed:
- for mac: `brew install docker` and `brew install docker-compose`
- UHH IDK LINUX COMMANDS

START THE DAEMON? SMTH MAC EVIL

## How to run

- Navigate towards `/tutorial-time`
- Run `docker-compose up --build`

Note that this may require extra steps if using a mac computer, as mac runs docker in a virtual machine and doesn't necessarily expose the port to the actual machine.

## How to run test cases

- Navigate towards `/tutorial-time`
- Run `gradle clean test`

## Usage

- Go to `localhost:8080` to see a welcome message.

