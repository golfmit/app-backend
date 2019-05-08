package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Workout;
import org.apache.log4j.Logger;

import java.util.Map;

public class ListWorkoutsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            // get the 'pathParameters' from input
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String user = pathParameters.get("user");
            String name = pathParameters.get("name");
            String group = pathParameters.get("group");
            // get the Exercise by id
            Iterable<Workout> workout = new Workout().byUserAndExercise(user, name, group);

            // send the response back
            if (workout != null) {
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(workout)
                        .build();
            } else {
                return ApiGatewayResponse.builder()
                        .setStatusCode(404)
                        .setObjectBody("Workouts by: '" + user  + "@" +group + ":" + name + "' not found.")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Error in retrieving Workout: " + ex);

            // send the error response back
            Response responseBody = new Response("Error in retrieving workout: ", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .build();
        }
    }
}