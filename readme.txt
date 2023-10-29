
mvn -f ./clap/pom.xml spring-boot:run
mvn -f ./mass/pom.xml spring-boot:run
mvn -f ./rdw/pom.xml spring-boot:run
mvn -f ./iot/pom.xml spring-boot:run
mvn -f ./exe/pom.xml spring-boot:run



http://localhost:8080 				- Temporal Web UI
http://localhost:8085 				- Grafana dashboards
http://localhost:9090 				- Prometheus UI
http://localhost:9090/targets 		- Prometheus targets
http://localhost:8000/metrics 		- Server metrics