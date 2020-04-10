package test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import base.RestAPIBase;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestCreateRoleNegative extends RestAPIBase {

    public TestCreateRoleNegative() throws IOException {
        super();
    }

    @Test
    public void testCreateAndEditRoleWithTakenName() throws IOException {
        RestAPIBase restAPIBase = new RestAPIBase();
        JsonPath roles = restAPIBase.methodGET(properties.getValue("ROLES.ALL")).jsonPath();
        String existingRoleName = roles.getString("[0].role_name");
        String existingRoleId = roles.getString("[1].role_id");
        HashMap<String, Object> roleName = new HashMap<>();
        roleName.put("role_name", existingRoleName);
        Response createRole = restAPIBase.methodPOST(properties.getValue("ROLE"), roleName);
        assertEquals("Response should notify that " + existingRoleName + " role already exists!",
                "Role " + existingRoleName + " already exists",
                createRole.jsonPath().getString("roleexists"));
        assertEquals("Status code should be 404!", 404, createRole.getStatusCode());
        Response editRole = restAPIBase.methodPUT(
                properties.getValue("ROLE") + "/" + existingRoleId, roleName);
        assertEquals("Response should notify that " + existingRoleName + " role already exists!",
                "Role " + existingRoleName + " already exists", editRole.jsonPath().getString("role_name"));
        assertEquals("Status code should be 400!", 400, editRole.getStatusCode());
        roles = restAPIBase.methodGET(properties.getValue("ROLES.ALL")).jsonPath();
        int counter = 0;
        for (int i = 0; i < roles.getList("$").size(); i++) {
            if (roles.getString("[" + i + "].role_name").equals(existingRoleName)) {
                counter++;
            }
        }
        assertEquals("We shouldn't have more than one role with the same name!", 1, counter);
    }

}
