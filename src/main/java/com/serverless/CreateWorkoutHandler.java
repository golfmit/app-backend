package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dal.Exercise;
import com.serverless.dal.Workout;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class CreateWorkoutHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

          // create the Exercise object for post
          Workout workout = new Workout();
          // workout.setId(body.get("id").asText());
          workout.setName(body.get("name").asText());
		  workout.setGroup(body.get("group").asText());
          workout.setUser(body.get("user").asText());
		  workout.setResult(body.get("result").asText());
          workout.setDate(body.get("date").asText());
          workout.save(workout);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(workout)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();

      } catch (Exception ex) {
          logger.error("Error in saving workout: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving workout: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}
}