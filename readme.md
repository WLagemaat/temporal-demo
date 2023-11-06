## Starting Temporal Stack

``` bash 
$ docker-compose -f ./docker/docker-compose.yml up -d 
```

## Starting applications (need to be still containerized )
``` bash
$ mvn -f ./clap/pom.xml spring-boot:run 
```
``` bash
$ mvn -f ./mass/pom.xml spring-boot:run
```
``` bash
$ mvn -f ./rdw/pom.xml spring-boot:run
```
``` bash
$ mvn -f ./iot/pom.xml spring-boot:run
```
``` bash
$ mvn -f ./exe/pom.xml spring-boot:run
```
``` bash
$ mvn -f ./dog/pom.xml spring-boot:run
```

[DEMO.md](DEMO.md)

- [Temporal Web UI](http://localhost:8080)
- [Grafana dashboards](http://localhost:8085)
- [Prometheus UI](http://localhost:9090)
- [Prometheus targets](http://localhost:9090/targets)
- [Server metrics](http://localhost:8000/metrics)
