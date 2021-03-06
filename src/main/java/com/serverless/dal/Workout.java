package com.serverless.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@DynamoDBTable(tableName = "PLACEHOLDER_WORKOUT_TABLE_NAME")
public class Workout {

    // get the table name from env. var. set in serverless.yml
    private static final String WORKOUTS_TABLE_NAME = System.getenv("WORKOUTS_TABLE_NAME");

    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private Logger logger = Logger.getLogger(this.getClass());

    private String id;
    private String user;
    private String group;
    private String name;
    private String result;
    private String date;

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBRangeKey(attributeName = "user")
    public String getUser() {
        return this.user;
    }
    public void setUser(String name) {
        this.user = name;
    }

    @DynamoDBRangeKey(attributeName = "name")
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBRangeKey(attributeName = "group")
    public String getGroup() {
        return this.group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    @DynamoDBAttribute(attributeName = "result")
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    @DynamoDBAttribute(attributeName = "date")
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Workout() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(WORKOUTS_TABLE_NAME))
            .build();
        // get the db adapter
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    public String toString() {
        return String.format("Workout [id=%s, exercise=%s:%s, user=%s, result=$s]", this.id, this.group, this.name, this.user, this.result);
    }

    // methods
    public Boolean ifTableExists() {
        return this.client.describeTable(WORKOUTS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    public List<Workout> byUserAndExercise(String user, String name, String group) throws IOException {
        AttributeValue nameValue = DateToStringMarshaller.instance().marshall(name);
        AttributeValue groupValue = DateToStringMarshaller.instance().marshall(group);
        AttributeValue userValue = DateToStringMarshaller.instance().marshall(user);
      DynamoDBScanExpression scanExp = new DynamoDBScanExpression().withFilterExpression("name = :name and group = :group and user = :user")
              .addExpressionAttributeValuesEntry(":name", nameValue)
              .addExpressionAttributeValuesEntry(":group", groupValue)
              .addExpressionAttributeValuesEntry(":user", userValue);

      List<Workout> results = this.mapper.scan(Workout.class, scanExp);
      for (Workout p : results) {
        logger.info("Workout - list(): " + p.toString());
      }
      return results;
    }

    public Workout get(String id) throws IOException {
        Workout exercise = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Workout> queryExp = new DynamoDBQueryExpression<Workout>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Workout> result = this.mapper.query(Workout.class, queryExp);
        if (result.size() > 0) {
          exercise = result.get(0);
          logger.info("Workouts - get(): workout - " + exercise.toString());
        } else {
          logger.info("Workouts - get(): workout - Not Found.");
        }
        return exercise;
    }

    public void save(Workout workout) throws IOException {
        logger.info("Workouts - save(): " + workout.toString());
        this.mapper.save(workout);
    }

    public Boolean delete(String id) throws IOException {
        Workout workout = null;

        // get workout if exists
        workout = get(id);
        if (workout != null) {
          logger.info("Workouts - delete(): " + workout.toString());
          this.mapper.delete(workout);
        } else {
          logger.info("Workouts - delete(): workout - does not exist.");
          return false;
        }
        return true;
    }

}