package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import util.PropertiesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class RestAPIBase {

    protected static final String ADMIN_PROPERTIES = "admin.properties";
    protected PropertiesUtil properties = new PropertiesUtil(ADMIN_PROPERTIES);
    private RestAssuredConfig restAssuredConfig;
    private RequestSpecBuilder builder;

    public RestAPIBase() throws IOException {
        super();
        RestAssured.baseURI = properties.getValue("BASE.URI");
        RestAssured.basePath = properties.getValue("BASE.PATH");
        ConnectionConfig connectionConfig = new ConnectionConfig().closeIdleConnectionsAfterEachResponseAfter(120,
                TimeUnit.SECONDS);
        restAssuredConfig = new RestAssuredConfig().connectionConfig(connectionConfig);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("email", properties.getValue("USER.MAIL"));
        jsonAsMap.put("password", properties.getValue("USER.PASSWORD"));
        Response response = given().contentType("application/json").body(jsonAsMap).when().post(
                properties.getValue("LOGIN"));
        JsonPath jsonPath = new JsonPath(response.asString());
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addHeader("content-type", "application/json;charset=utf-8");
        requestSpecBuilder.addHeader("authorization", "Bearer " + jsonPath.get("token").toString());
        builder = requestSpecBuilder;
    }

    public Response methodGET(String endpoint) {
        RequestSpecification requestSpec = builder.build();
        return given().log().all().config(restAssuredConfig).spec(requestSpec).when().get(endpoint);
    }

    public Response methodPOST(String endpoint, HashMap<String, Object> payload) {
        RequestSpecification requestSpec = builder.build();
        return given().log().all().config(restAssuredConfig).spec(requestSpec).body(payload).when().post(endpoint);
    }

    public Response methodPUT(String endpoint, HashMap<String, Object> payload) {
        RequestSpecification requestSpec = builder.build();
        return given().log().all().config(restAssuredConfig).spec(requestSpec).body(payload).when().put(endpoint);
    }

    public Response methodDELETE(String endpoint) {
        RequestSpecification requestSpec = builder.build();
        return given().log().all().config(restAssuredConfig).spec(requestSpec).when().delete(endpoint);
    }

}


