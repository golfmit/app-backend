package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Exercise;
import org.apache.log4j.Logger;

@SuppressWarnings("unused")
public class CreateExerciseHandler implements RequestHandler<Exercise, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Exercise input, Context context) {

      try {
          // get the 'body' from input


          // create the Exercise object for post
          Exercise exercise = new Exercise();
          exercise.setName(input.getName());
		  exercise.setGroup(input.getGroup());
          exercise.setUrl(input.getUrl());
          exercise.setTimestamp(input.getTimestamp());
          exercise.save(exercise);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(exercise)
      				.build();

      } catch (Exception ex) {
          logger.error("Error in saving exercise: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving exercise: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.build();
      }
	}
}