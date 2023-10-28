# Getting Started

## Start
> docker Rabbit & postgres

> temporal server start-dev -n PRE_INTAKE
![part_1_async_external.png](docs%2Fpart_1_async_external.png)
## Run MessageRabbitMqTest

<p>The Temporal Server will be available on <a>localhost:7233</a></p>
<p>The Temporal Web UI will be available at <a>http://localhost:8233</a></p>

### Publish Message to RabbitMQ
http://localhost:15672/#/queues/%2F/vroem 

**Properties**
```
content_encoding:	UTF-8
content_type:		text/plain
```
**payload:**
```
{
"transgressionNumber" : "TEST-268951-JQ",
"validOdds" : 95,
"rdwOdds" : 80,
"rdwTechnicalErrorOdds" : 3,
"isMulder" : true,
"mulderTechnicalErrorOdds" : 5,
"wormTechnicalErrorOdds" : 5,
"svenTechnicalErrorOdds" : 5,
"cvomTechnicalErrorOdds" : 5,
"party" : "HANS"
}
```


