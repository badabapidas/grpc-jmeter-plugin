# grpc-jmeter-plugin

This is a java project (gradle) to create a gRPC client with JMeter samplers to enable stress testing with JMeter

## Installation (Jmeter with gRPC client)
```
1. Clone the project using 
    git clone https://github.com/badabapidas/grpc-jmeter-plugin.git
2. Build the project
    gradlew clean build
3. Copy the plugin lib to Jmeter lib folder
    - copy grpc-jmeter-plugin-1.0.0-all.jar from <codebase>\build\libs location
    - paste to path/to/jmeter/lib/ext so that the samplers are in the class path
4. launch JMeter and you should be able to see the classes in the "java request" samplers.
5. Sample jmx is available under resources/GrpcHealth.jmx. You can Import and run.
```

## Installation gRPC server
As grpc is known for exchanging the same messages define in a .proto files, so to use this jmeter grpc client the same proto and its related services has to be depoyed in the server side. Implementation of the grpc server(HealthServer.java) and the service (HealthCheckServiceImpl.java) is already provided in source of this project.

```
Steps to deploy the server side implementations
1. copy the health.proto in server codes
2. Execute gradle task to generate the java impl for this proto
3. copy HealthCheckServiceImpl.java in the defined package or any package and do the changes accordingly
4. Add this service in the grpc server, for testing you can use HealthServer.java also
5. Start the server and it sould serve you jemeter grpc client requests
```

Jmeter GRPC plugin will be visible like 
<img src="http://irepo.eur.ad.sag/projects/WID/repos/jiraireviewplugin/raw/mail.png?at=refs%2Fheads%2Fmaster" alt="Grpc Jmeter Plugin"/>