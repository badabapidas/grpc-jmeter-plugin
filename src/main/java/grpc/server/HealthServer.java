/**
 * Sampe grp server code along with the service implementation from the health.proto.
 * 
 * Similar kind of approach has to be done in the grpc server side and the same HealthCheckServiceImpl has to be copy there 
 * and have to be added in the server
 */
package grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

public class HealthServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("gRPC server is running...");

		// plain text server
		Server server = ServerBuilder.forPort(50051).addService(ProtoReflectionService.newInstance())
				.addService(new HealthCheckServiceImpl()).build();

		// secure server
//		Server server = ServerBuilder.forPort(50051).addService(new GreetServiceImpl())
		// .useTransportSecurity(new File("new_ssl/server.crt"), new
		// File("new_ssl/server.pem")).build();

		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Received shutdown request");
			server.shutdown();
			System.out.println("Successfully stopped the server");
		}));
		server.awaitTermination();
	}
}
