# Splunk Tracer bundle.

The Splunk distributed tracing library for the standard Java runtime environment, as a fat-jar containing the
OkHttp collector layer and a `TracerFactory` implementation, which can be configured through a `tracer.properties`
configuration file:

```properties
spl.accessToken=myaccesstoken
spl.componentName=MyApplication
spl.collectorHost=localhost
spl.collectorProtocol=https
spl.collectorPort=8088
``` 

Parameters can be overriden through System properties, too:

```
java -cp:$MYCLASSPATH:splunk.jar \
	-Dspl.componentName=AnotherService \
	com.mycompany.MyService
```

## Parameters

Splunk Tracer parameters use the prefix `spl.`. The only required parameter is `spl.accessToken`, and no Tracer will be created if this parameter is missing. In case of error, a log showing the error will be shown.

Common parameters are:

|Parameter | Type|
|----------|-----|
|spl.accessToken | String|
|spl.componentName | String|
|spl.collectorHost | String|
|spl.collectorProtocol | http or https|
|spl.collectorPort | Integer larger than 0|
