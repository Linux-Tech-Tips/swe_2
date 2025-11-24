# Infrastructure Setup Details

This folder contains all the files required to host the project on the platform we used.

## Platform used

The current platform is the following:

- **Server:** Self-hosted on a Raspberry Pi 5
- **DNS:** Cloudflare domain registrar
- **Routing:** Cloudflare tunnel
- **Services:** Docker container set using `docker-compose`

### Server configuration

The server uses a headless Raspberry Pi OS installation, with a LAN-accessible SSH daemon blocked off from the rest of the internet,
with all the exposed services running on a Docker network to sandbox the project and make sure the actual device isn't accessible over the internet.

### DNS and Routing

The DNS setup that allows the self-hosted server to be accessible over the internet is a domain (`unixusergroup.org`) purchased through the Cloudflare registrar.
The subdomains for each service are also registered through Cloudflare DNS, and are linked directly to the routing component of the project infrastructure.

The routing that allows end-points on our device to be accessed through the internet is achieved through Cloudflare tunnels.
The device itself has a Cloudflare daemon that exposes a local docker network to the remote Cloudflare network,
which is then used to route requests going to the registered subdomains to services on the local Pi network.

### Services configuration

The actual project runs entirely within **Docker Compose**, which creates multiple containers (one for each component of the project)
and joins them together into a local virtual docker network.
Each container has a hostname on this network corresponding to its purpose.
Since the containers share a virtual docker network (which differs from the host network), the underlying Raspberry Pi OS is inaccessible to the containers,
allowing the services to be sandboxed in their own virtual environment, independent on the host device.

Bringing the topic back to DNS and routing, one of the containers running in the Docker network contains a Cloudflare daemon, 
which makes the other containers accessible through Cloudflare.
The subdomains, as mentioned in the previous section, are routed (using said daemon) to ports on specific hostnames running the various components of our project.

## Infrastructure folder structure

The `infra` directory contains all the files required for the aforementioned infrastructure setup.

The directory structure is as follows:
```
infra
├── backend
│   ├── Dockerfile
│   ├── entrypoint.sh
│   └── github_key
├── compose.yaml
├── docs
│   ├── default.conf
│   ├── Dockerfile
│   └── github_key
├── frontend
│   ├── Dockerfile
│   └── github_key
├── nginx
│   └── conf.d
│       └── default.conf
├── README.md
└── setup.sh
```

The `compose.yaml` file contains the **Docker Compose** setup for the project, including all the various containers.

The `setup.sh` file contains code that runs all setup required for the `docker-compose` to work properly, and then runs `docker-compose`.

### Docker Configuration

The `compose.yaml` file contains multiple services used to run the project.

The existing services are the following:

- `reverse-proxy` ... reverse proxy Nginx server, routing subdomain hostnames to appropriate containers
- `frontend` ... container running the frontend of the project
- `backend-wrapper` ... container running the backend of the project (a wrapper for Docker-in-Docker, as the backend itself uses `docker-compose`)
- `jenkins` ... Jenkins CI server
- `docs` ... Nginx server hosting a static webpage containing the Javadoc pages for the project
- `cloudflared` ... Cloudflare Daemon container, allowing external access to the Docker network

The routes established through the cloudflare tunnnel are as follows:

- *https://frontend.unixusergroup.org* -> *http://reverse-proxy:80* -> *http://frontend:5173*
- *https://backend.unixusergroup.org* -> *http://reverse-proxy:80* -> *http://backend-wrapper:8080*
- *https://ci.unixusergroup.org* -> *http://jenkins:8080*
- *https://docs.unixusergroup.org* -> *http://docs:8081*

If desired, the CI and docs could also be routed through the Proxy container.
The Proxy container could also include any additional extra configuration and request-based security, if necessary.

The folders `backend`, `docs`, `frontend` and `nginx` contain files necessary to set up custom docker containers for the corresponding project components.

### Private Secrets Configuration

The subdirectories `frontend`, `backend` and `docs` all contain a file called `github_key`.
This file is necessary to be able to clone the latest stable version of the project's remote repository.
The file should contain a GitHub deploy key linked with the GitHub remote, however, this should not be exposed in the actual repository.

For security reasons, the existing files are placeholders (containing just the text 'PLACEHOLDER') and are populated locally upon setup with the `setup.sh` script.
The script finds and copies keys from a directory on the host machine but outside of the project repository, adds them to the `github_key` files, runs setup,
and finishes by overwriting the `github_key` files with the 'PLACEHOLDER' text to prevent accidentally committing a private key.

## How To Run

The project is currently run on a Raspberry Pi 5, but the current configuration should be usable on any self-hosted platform.

### Minimum Requirements

The minimum system requirements for the project are estimated based on the current infrastructure,
as the Raspberry Pi setup works, but can be observed to slow down from time to time.

The minimum requirements are estimated to be as follows:

- **CPU:** quad-core 64-bit Arm or x86-64
- **RAM:** 4 GB
- **Storage:** ~5 GB

### Steps to Run

1. Install the dependencies `git`, `docker`, `docker-compose` from the relevant package manager
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
also self-hosted on the same Raspberry Pi.

As the authentication is not a part of the project directly, rather being an auxiliary system, it isn't included in the `infra` folder.
The provider used here is `Authentik`, hosted in a completely separate docker container, routed through a different Cloudflare tunnel.
