/**
 * JMeter Sampler for GRPC
 */
package grpc.jmeter.plugin;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import grpc.client.HealthClient;
import io.grpc.StatusRuntimeException;

public class GrpcJmeterClientSampler extends AbstractJavaSamplerClient {

	HealthClient healthClient = null;

	@Override
	public void setupTest(JavaSamplerContext context) {
		String host = context.getParameter("host");
		int port = context.getIntParameter("port");
		this.healthClient = new HealthClient(host, port);
		super.setupTest(context);
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("host", "localhost");
		defaultParameters.addArgument("port", "50051");
		return defaultParameters;
	}

	public SampleResult runTest(JavaSamplerContext context) {

		SampleResult result = new SampleResult();
		boolean success = true;
		String response = "";
		result.sampleStart();

		try {
			response = this.healthClient.healthcheck();
			result.sampleEnd();
			result.setSuccessful(success);
			result.setResponseData(response.getBytes());
			result.setResponseMessage(response);
//			result.setResponseMessage("Successfully performed backup healthcheck");
			result.setResponseCodeOK(); // 200 code
		} catch (StatusRuntimeException e) {
			result.sampleEnd(); // stop stopwatch
			result.setSuccessful(false);
			result.setResponseMessage("Exception: " + e);
			success = false;
			result.setSuccessful(success);
			// get stack trace as a String to return as document data
			java.io.StringWriter stringWriter = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(stringWriter));
			result.setResponseData(stringWriter.toString().getBytes());
			result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
			result.setResponseCode("500");
		}

		return result;
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		try {
			healthClient.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		super.teardownTest(context);
	}

}
