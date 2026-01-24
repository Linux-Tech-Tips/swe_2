# Infrastructure Setup Details

This folder contains all the files required to host the project on the platform we used.

## Platform used

The current platform is the following:

- **Server:** Self-hosted on a Dell OptiPlex-based home server
- **DNS:** Cloudflare domain registrar
- **Routing:** Cloudflare tunnel
- **Services:** Docker container set using `docker compose`

### Server configuration

The server uses a headless Debian OS installation, with a LAN-accessible SSH daemon blocked off from the rest of the internet.
Remote connectivity is ensured through a Cloudflare tunnel-based VPN that can make the SSH daemon accessible remotely to connected users, restricted to explicitly allowed email addresses.
All the exposed services are running on a Docker network to sandbox the project and make sure the actual device isn't accessible over the internet to unauthorised users.

### DNS and Routing

The DNS setup that allows the self-hosted server to be accessible over the internet is a domain (`unixusergroup.org`) purchased through the Cloudflare registrar.
The subdomains for each service are also registered through Cloudflare DNS, and are linked directly to the routing component of the project infrastructure.

The routing that allows end-points on our device to be accessed through the internet is achieved through Cloudflare tunnels.
Each Docker network that contains a set of remotely accessible services has a Cloudflare daemon running in a separate container in said network,
allowing it to forward any traffic to any of the other containers (services) on the Docker network.

This allows the services to be accessible remotely over the internet through a domain, without exposing any parts of the underlying physical device.

### Services configuration

The actual project runs using **Docker Compose**, which creates multiple containers (one for each component of the project)
and joins them together into a local virtual Docker network.
Each container has a hostname on this network corresponding to its purpose.
Since the containers share a virtual docker network (which differs from the host network), the underlying OS is inaccessible to the containers,
allowing the services to be sandboxed in their own virtual environment, independent on the host device.

Bringing the topic back to DNS and routing, one of the containers running in the Docker network contains a Cloudflare daemon, 
which makes the other containers accessible through the aforementioned Cloudflare tunnel.
The subdomains, as mentioned in the previous section, are routed to ports on specific hostnames running the various components of our project.

## Infrastructure folder structure

The `infra` directory contains all the files required for the infrastructure setup described above.

The directory structure is as follows:
```
infra
├── backend
│   └── Dockerfile
├── backend-db-include.yaml
├── common
│   ├── Dockerfile
│   └── github_key   
├── compose.yaml
├── docs
│   ├── default.conf
│   └── Dockerfile
├── frontend
│   └── Dockerfile
├── README.md
└── setup.sh
```

The `compose.yaml` file contains the **Docker Compose** setup for the project, including all the various containers.

The `backend-db-include.yaml` file contains a separate, reusable **Docker Compose** configuration for the backend database container,
used by the infrastructure as well as the backend container when running it locally for testing purposes.

The `setup.sh` file contains code that runs all setup required for the `docker compose` to work properly, and then runs `docker compose`.

### Docker Configuration

The `compose.yaml` file contains multiple services used to run the project.

The existing services are the following:

- `jenkins` ... Jenkins CI server
- `docs` ... Nginx server hosting a static webpage containing the Javadoc pages for the project
- `frontend` ... container running the frontend of the project, using the compiled React page served through an Nginx server
- `backend` ... container running the backend Spring Boot code of the project, using the assembled/built `jar` file
- `cloudflared` ... Cloudflare Daemon container, allowing external access to the Docker network

In addition, the `backend-db-include.yaml` file is included at the top of the configuration file, which introduces another service:

- `db` ... MariaDB database holding backend data

The compose configurations additionally set up 2 docker networks and a docker volume:

- network `default` ... Docker network with all the services, including `cloudflared`, other than the `db` container
- network `db_internal` ... Docker network containing the `backend` and `db` services, to ensure the database is properly separated from the Cloudflare daemon.
- volume `db-data` ... Docker volume within the `db` service ensuring persistence of database data on disk

The routes established through the cloudflare tunnnel are as follows:

- *https://frontend.unixusergroup.org* -> *http://frontend:8080*
- *https://backend.unixusergroup.org* -> *http://backend:8080*
- *https://ci.unixusergroup.org* -> *http://jenkins:8080*
- *https://docs.unixusergroup.org* -> *http://docs:8080*

The folders `common`, `backend`, `docs` and `frontend` contain files necessary to set up custom docker containers for the corresponding project components.

### Docker Build Setup

To save memory and storage and for increased efficiency, the Docker configuration uses multistage builds as well as a common shared container with the repository.

The `common` folder contains a Dockerfile and Github key that uses a simple Alpine/Git base image to clone the stable branch of the remote repository.
This image, named `martin/tt_common` is then used as the base step in the remaining containers.

The other containers use a multistage build, starting with the common image and copying the section of the repo that's necessary for said service.
Then, in another stage based on an image containing the appropriate build tool, the containers build whatever service they intend to run.
Lastly, the final stage involves copying the build artifacts into a minimal container able to run the project component, and running the service.

### Private Secrets Configuration

The subdirectory `common` contains a file called `github_key`.
This file is necessary to be able to clone the latest stable version of the project's remote repository.
The file should contain a GitHub deploy key linked with the GitHub remote, however, this should not be exposed in the actual repository.

For security reasons, the existing file is a placeholder (containing just the text 'PLACEHOLDER') and is populated locally upon setup with the `setup.sh` script.
The script finds and copies a key from a directory on the host machine but outside of the project repository, adds it to the `github_key` file, runs setup,
and finishes by overwriting the `github_key` file with the 'PLACEHOLDER' text to prevent accidentally committing a private key.

## How To Run

The project is currently run on a Dell OptiPlex-based home server, but the current configuration should be usable on any self-hosted platform.

### Minimum Requirements

The minimum system requirements for the project are estimated based on the current infrastructure.

The minimum requirements are estimated to be as follows:

- **CPU:** quad-core 64-bit Arm or x86-64
- **RAM:** ~2.5 GB
- **Storage:** ~5 GB

### Steps to Run

1. Install the dependencies `git`, `docker`, `docker-compose` (or ensure `docker compose` command present) from the relevant package manager
2. Add the required secrets to the `private` and `infra` folders:
  - The default private directory is in the same folder as the repository, called `private/`
  - A custom `private` folder path can be provided to the setup script using the environment variable `PRIVATE_PATH`
  - The `private` path needs to contain a file `github_key` containing a GitHub deploy key of the *swe_2* repository
  - The `private` path needs to contain a file `private_env.sh` containing the following environment variable:
    - `GIT_ACCESS_TOKEN` with an access token for the GitHub API for the *swe_2* repo
  - A local copy of the `infra` folder itself should also contain a `.env` file, ignored by git, with the following environment variable:
    - `CLOUDFLARE_TUNNEL_TOKEN` with the token generated by Cloudflare to link the tunnel to the remote Cloudflare network
3. Run `setup.sh` to get the infrastructure up and running

## Authentication Notes

The Authentication in the app is done through an external OAuth2 provider, hosted at https://oauth2.unixusergroup.org,
also self-hosted on the same server.

As the authentication is not a part of the project directly, rather being an auxiliary system, it isn't included in the `infra` folder.
The provider used here is `Authentik`, hosted in a completely separate docker container, routed through a different Cloudflare tunnel.

Theoretically, it should be fairly simple to adjust the frontend and backend to use any OAuth2/OIDC-compatible external identity provider.
