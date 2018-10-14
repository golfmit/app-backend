package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Workout;
import org.apache.log4j.Logger;

import java.util.Collections;

@SuppressWarnings("unused")
public class CreateWorkoutHandler implements RequestHandler<Workout, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Workout input, Context context) {

      try {
          // create the Exercise object for post
          Workout workout = new Workout();
          workout.setName(input.getName());
		  workout.setGroup(input.getGroup());
          workout.setUser(input.getUser());
		  workout.setResult(input.getResult());
          workout.setDate(input.getDate());
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