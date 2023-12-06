# Temporal Server docker-compose files

This repository provides docker-compose files that enable you to run a local instance of the Temporal Server.
There are a variety of docker-compose files, each utilizing a different set of dependencies.
Every major or minor release of the Temporal Server has a corresponding docker-compose release.

Alongside the docker compose files you can find Kubernetes manifests suitable for setting up a development version of Temporal in a Kubernetes cluster. These files can be found in [k8s](./k8s) directory, each directory holds the manifests related to one of the docker compose files. Details of using these manifests can be found in [KUBERNETES](./KUBERNETES.md).

## Prerequisites

To use these files, you must first have the following installed:

- [Docker](https://docs.docker.com/engine/installation/)
- [docker-compose](https://docs.docker.com/compose/install/)

## How to use

The following steps will run a local instance of the Temporal Server using the default configuration file (`docker-compose.yml`):

1. Clone this repository.
2. Run the 
```shell
docker-compose -f docker-compose-temporal.yml up -d
```
3. wait for the admin tool to create the namespaces 
4. Run the 
```shell
docker-compose -f docker-compose-apps.yml up -d
```

After the Server has started, you can open the Temporal Web UI in your browser: [http://localhost:8080](http://localhost:8080).

You can also interact with the Server using a preconfigured CLI (tctl).
First create an alias for `tctl`:

```bash
alias tctl="docker exec temporal-admin-tools tctl"
```

The following is an example of how to register a new namespace `test-namespace` with 1 day of retention:

```bash
tctl --ns test-namespace namespace register -rd 1
```

You can find our `tctl` docs on [docs.temporal.io](https://docs.temporal.io/docs/system-tools/tctl/).

Get started building Workflows with a [Go sample](https://github.com/temporalio/samples-go), [Java sample](https://github.com/temporalio/samples-java), or write your own using one of the [SDKs](https://docs.temporal.io/docs/sdks-introduction).

Some exposed endpoints:
- http://localhost:8080 - Temporal Web UI
- http://localhost:8085 - Grafana dashboards
- http://localhost:9090 - Prometheus UI
- http://localhost:9090/targets - Prometheus targets
- http://localhost:8000/metrics - Server metrics

## Building
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