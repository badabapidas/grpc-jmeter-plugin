/**
 * Grpc Health Client 
 */
package grpc.client;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sag.uhm.grpc.client.health.HealthCheckRequest;
import com.sag.uhm.grpc.client.health.HealthCheckResponse;
import com.sag.uhm.grpc.client.health.HealthCheckerGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class HealthClient {
	private static final Logger logger = Logger.getLogger("Health");

	private final ManagedChannel channel;
	private final HealthCheckerGrpc.HealthCheckerBlockingStub blockingStub;
	private final HealthCheckerGrpc.HealthCheckerStub asyncStub;
	private String host;
	private int port;

	public HealthClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
		logger.setLevel(Level.WARNING);
		this.host = host;
		this.port = port;
	}

	/**
	 * Construct client for accessing Health server using the existing channel.
	 */
	HealthClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = HealthCheckerGrpc.newBlockingStub(channel);
		asyncStub = HealthCheckerGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	/** check health of the server. */
	public String healthcheck() {
		logger.info("running grpc health check");
		HealthCheckRequest request = HealthCheckRequest.newBuilder().setHost(host).setPort(port).build();
		HealthCheckResponse response = null;
		try {
			response = blockingStub.healthCheck(request);

		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			throw e;
		}
		logger.info("Health Check Response is: " + response.getMessage());
		return response.getMessage();
	}

	public static void main(String[] args) throws Exception {
		HealthClient client = new HealthClient("localhost", 50051);
		try {
			System.out.println(client.healthcheck());
		} finally {
			client.shutdown();
		}
	}
}
