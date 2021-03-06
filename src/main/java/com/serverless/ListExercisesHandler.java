package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Exercise;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ListExercisesHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    try {
        // get all exercises
        List<Exercise> exercises = new Exercise().list();

        // send the response back
        return ApiGatewayResponse.builder()
    				.setStatusCode(200)
    				.setObjectBody(exercises)
    				.build();
    } catch (Exception ex) {
        logger.error("Error in listing exercises: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in listing exercises: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.build();
    }
	}
}