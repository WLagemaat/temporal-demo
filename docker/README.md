# Temporal Demo docker-compose files

There are two docker-compose files in this directory:
- `docker-compose-temporal.yml` - starts the Temporal Server Stack and the Temporal Web UI.
- `docker-compose-apps.yml` - starts the applications.

## Prerequisites

To use these files, you must first have the following installed:
- [Docker](https://docs.docker.com/engine/installation/)
- [docker-compose](https://docs.docker.com/compose/install/)

## How to use

The following steps will run a local instance of the Temporal Server using the default configuration file:

1. Clone this repository.

2. Run the following commands from ./docker/ in the repository to start the Temporal Stack: 
```shell
docker-compose -f docker-compose-temporal.yml up -d
```
3. wait for the admin tool to start up. 

After the Server has started, you can open the Temporal Web UI in your browser: [http://localhost:8080](http://localhost:8080). 
You can also interact with the Server using a preconfigured CLI (tctl).

First create an alias for `tctl`:

```bash
alias tctl="docker exec temporal-admin-tools tctl"
```
4. Create the namespaces
```bash
tctl --ns PRE_INTAKE n re
tctl --ns MANUAL_FLOWS n re
tctl admin cluster add-search-attributes --name InsuranceCaseState --type Keyword
```
Now we have some namespaces that the demo will use. We could have done all on the 'default' namespace, but this way we can show how to use namespaces.
You can find `tctl` docs on [docs.temporal.io](https://docs.temporal.io/docs/system-tools/tctl/).

5. Run the following commands from ./docker/ in the repository: 
```shell
docker-compose -f docker-compose-apps.yml up -d
```


## Building the containers
Set with the tags and names used in the docker-compose files.

```bash
docker build -t wvdl/temporal-demo:clap-1 ../clap/.
```
```bash
docker build -t wvdl/temporal-demo:dog-1 ../dog/.
```
```bash
docker build -t wvdl/temporal-demo:exe-1 ../exe/.
```
```bash
docker build -t wvdl/temporal-demo:iot-1 ../iot/.
```
```bash
docker build -t wvdl/temporal-demo:mass-1 ../mass/.
```
```bash
docker build -t wvdl/temporal-demo:rdw-1 ../rdw/.
```