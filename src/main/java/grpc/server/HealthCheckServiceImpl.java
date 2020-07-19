/**
 * Serivce impl for health.proto which has to be added in the server side to use this jmeter plugin
 */
package grpc.server;

import com.sag.uhm.grpc.client.health.HealthCheckRequest;
import com.sag.uhm.grpc.client.health.HealthCheckResponse;
import com.sag.uhm.grpc.client.health.HealthCheckerGrpc.HealthCheckerImplBase;

import io.grpc.stub.StreamObserver;

public class HealthCheckServiceImpl extends HealthCheckerImplBase {
	@Override
	public void healthCheck(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
		HealthCheckResponse response = HealthCheckResponse.newBuilder()
				.setMessage("Response recieved from server: " + request.getHost() + ":" + request.getPort()).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}