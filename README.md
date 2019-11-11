# splunk-tracer-java

[ ![Download](https://api.bintray.com/packages/splunk/maven/splunk-tracer-jre/images/download.svg) ](https://bintray.com/splunk/maven/)[![MIT license](http://img.shields.io/badge/license-MIT-blue.svg)](http://opensource.org/licenses/MIT)

The Splunk distributed tracing library for the standard Java runtime environment.

* [Getting Started](#getting-started)
  * [JRE](#getting-started-jre)
* [API documentation](#apidocs)
* [Options](#options)

<a name="#getting-started"></a>
<a name="#getting-started-jre"></a>

## Getting started: JRE

The JRE library is hosted on Bintray, jcenter, and Maven Central. 
The Bintray [splunk-tracer-jre](https://bintray.com/splunk/maven/splunk-tracer-jre/view) project contains 
additional installation and setup information for using the library with various build systems such as Ivy and Maven.

### Maven

```xml
<dependency>
    <groupId>com.splunk.tracer</groupId>
    <artifactId>splunk-tracer-jre</artifactId>
    <version> VERSION </version>
</dependency>
<dependency>
   <groupId>com.splunk.tracer</groupId>
   <artifactId>tracer-okhttp</artifactId>
   <version> VERSION </version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.25</version>
</dependency>
```

* Be sure to replace `VERSION` with the current version of the library. For `TRACER-GRPC-VERSION` you can refer to  [splunk-tracer-common](https://github.com/splunk/splunk-tracer-java-common) which contains `tracer-okhttp`.
* Splunk libraries use provided scope for netty-tcnative-boringssl-static and slf4j. In other words, Splunk tracer libraries will rely on whichever Netty/sl4j version is currently available (i.e. pulled in at runtime) to avoid conflicting with existing versions within your project

### Gradle

In most cases, modifying your `build.gradle` with the below is all that is required:

```
repositories {
    mavenCentral() // OR jcenter()
}
dependencies {
    compile 'com.splunk.tracer:splunk-tracer-jre:VERSION'
    compile 'com.splunk.tracer:tracer-okhttp:VERSION'
    compile 'org.sl4j:sl4j-simple:1.7.25'
}
```

* Be sure to replace `VERSION` with the current version of the library
* The artifact is published to both `jcenter()` and `mavenCentral()`. Use whichever you prefer.

### Initializing the Splunk Tracer

```java
// Important the OpenTracing interfaces
import io.opentracing.Span;
import io.opentracing.Tracer;

// ...

// Initialize the OpenTracing Tracer with Splunk's implementation
Tracer tracer = new com.splunk.tracer.jre.JRETracer(
         new com.splunk.tracer.shared.Options.OptionsBuilder()
            .withAccessToken("{your_access_token}")
            .build()
);

// Start and finish a Span
Span span = tracer.buildSpan("my_span").start();
this.doSomeWorkHere();
span.finish();
```

<a name="apidocs"></a>
## API Documentation

Tracing instrumentation should use the OpenTracing APIs to stay portable and in sync with the standard:

* [OpenTracing API (javadoc)](http://javadoc.io/doc/io.opentracing/opentracing-api)


For reference, the generated Splunk documentation is also available:

* [splunk-tracer-jre (javadoc)](http://javadoc.io/doc/com.splunk.tracer/splunk-tracer-jre)

## Options

### Setting a custom component name

To set the name used in the Splunk UI for this instance of the Tracer, call `withComponentName()` on the `OptionsBuilder` object:

```java
options = new com.splunk.tracer.shared.Options.OptionsBuilder()
                      .withAccessToken("{your_access_token}")
                      .withComponentName("your_custom_name")
                      .build();

```

### Disabling the reporting loop

By default, the Java library does a report of any buffered data on a fairly regular interval. To disable this behavior and rely only on explicit calls to `flush()` to report data, initialize with:

```java
options = new com.splunk.tracer.shared.Options.OptionsBuilder()
                      .withAccessToken("{your_access_token}")
                      .withDisableReportingLoop(true)
                      .build();
```

To then manually flush by using the Splunk tracer object directly:

```java
// Flush any buffered tracing data
((com.splunk.tracer.jre.JRETracer)tracer).flush();
```

### Flushing the report at exit

In order to send a final flush of the data prior to exit, clients should manually flush by using the Splunk tracer object as described above.

### Disabling default clock correction

By default, the Java library performs clock correction based on timestamp information provided in the spans. To disable this behavior, initialize with: 

```java
options = new com.splunk.tracer.shared.Options.OptionsBuilder()
                      .withAccessToken("{your_access_token}")
                      .withClockSkewCorrection(false)
                      .build();
```

### Advanced Option: Transport and Serialization Protocols

By following the above configuration, the tracer will send information to Splunk using HTTP  which is the recommended configuration. If there are no specific transport protocol needs you have, there is no need to change this default.

There is just one option for transport protocol:

- GZIPped JSON over HTTP using [OkHttp](http://square.github.io/okhttp/) - The default solution.



#### Maven 

```xml
<dependency>
  <groupId>com.splunk.tracer</groupId>
  <artifactId>splunk-tracer-jre</artifactId>
  <version> VERSION </version>
</dependency>
<dependency>
   <groupId>com.splunk.tracer</groupId>
   <artifactId>tracer-okhttp</artifactId>
   <version> VERSION </version>
</dependency>
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-tcnative-boringssl-static</artifactId>
    <version>2.0.12.Final</version>
</dependency>
```

#### Gradle

```
repositories {
    jcenter()
}
dependencies {
    compile 'com.splunk.tracer:splunk-tracer-jre:VERSION'
    compile 'com.splunk.tracer:tracer-okhttp:VERSION'
    compile 'io.netty:netty-tcnative-boringssl-static:2.0.12.Final'
}
```

## Development info

See [DEV.md](DEV.md) for information on contributing to this instrumentation library.
