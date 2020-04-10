package test;

import base.PageBase;
import base.RestAPIBase;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestCreateProjectDeleteChild extends RestAPIBase {

    public TestCreateProjectDeleteChild() throws IOException {
        super();
    }

    private int technologyId;
    private int seniorityId;
    private int roleId;
    private int peopleId;
    private int projectId;

    @Test(priority = 1)
    public void testCreateProjectWithAllChildren() throws IOException {
        RestAPIBase rest_api_base = new RestAPIBase();
        JsonPath jsonPath = rest_api_base.methodPOST(properties.getValue("TECHNOLOGY"),
                getHashMap("technology_title", PageBase.getRandomString())).jsonPath();
        technologyId = jsonPath.getInt("technology_id");
        jsonPath = rest_api_base.methodPOST(properties.getValue("SENIORITY"),
                getHashMap("seniority_title", PageBase.getRandomString())).jsonPath();
        seniorityId = jsonPath.getInt("seniority_id");
        jsonPath = rest_api_base.methodPOST(properties.getValue("ROLE"),
                getHashMap("role_name", PageBase.getRandomString())).jsonPath();
        roleId = jsonPath.getInt("role_id");
        HashMap<String, Object> payload = new HashMap<>();
        Object[] array = new Object[1];
        array[0] = technologyId;
        payload.put("people_name", PageBase.getRandomString());
        payload.put("seniority_id", seniorityId);
        payload.put("technologies", array);
        payload.put("role_id", roleId);
        jsonPath = rest_api_base.methodPOST(properties.getValue("PERSON"), payload).jsonPath();
        peopleId = jsonPath.getInt("people_id");
        payload.clear();
        array = new Object[1];
        array[0] = peopleId;
        payload.put("project_title", PageBase.getRandomString());
        payload.put("people", array);
        jsonPath = rest_api_base.methodPOST(properties.getValue("PROJECT"), payload).jsonPath();
        projectId = jsonPath.getInt("project_id");
        jsonPath = rest_api_base.methodGET(properties.getValue("PROJECT") + "/" + projectId).jsonPath();
        assertEquals("Role in the project is not correct!", roleId, jsonPath.getInt("roles[0].role_id"));
        assertEquals("Person in the project is not correct!", peopleId, jsonPath.getInt("roles[0].people[0].people_id"));
        assertEquals("Seniority in the project is not correct!", seniorityId,
                jsonPath.getInt("roles[0].people[0].seniority.seniority_id"));
        assertEquals("Technology in the project is not correct!", technologyId,
                jsonPath.getInt("roles[0].people[0].technologies[0].technology_id"));
    }

    @Test(priority = 2)
    public void testDeleteChildAndVerifyInProject() throws IOException {
        RestAPIBase restAPIBase = new RestAPIBase();
        Response deleteSeniority = restAPIBase.methodDELETE(properties.getValue("SENIORITY") + "/" + seniorityId);
        assertEquals("Seniority is not deleted!", 200, deleteSeniority.statusCode());
        JsonPath jsonPath = restAPIBase.methodGET(properties.getValue("PROJECT") + "/" + projectId).jsonPath();
        assertEquals("Role in the project is not correct!", roleId, jsonPath.getInt("roles[0].role_id"));
        assertEquals("Person in the project is not correct!", peopleId, jsonPath.getInt("roles[0].people[0].people_id"));
        assertNull("Seniority should be removed from the project!", jsonPath.get("roles[0].people[0].seniority"));
        assertEquals("Technology in the project is not correct!", technologyId,
                jsonPath.getInt("roles[0].people[0].technologies[0].technology_id"));
    }

    private HashMap<String, Object> getHashMap(String key, Object value) {
        HashMap<String, Object> roleName = new HashMap<>();
        roleName.put(key, value);
        return roleName;
    }

}
