package com.sio.apis;

import com.sio.models.Target;
import com.sio.tools.ConfigManager;
import com.sio.tools.HttpRequestBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class MockChrevTzyonApiClient {
    private final ConfigManager cm = new ConfigManager();
    private final JSONParser parser = new JSONParser();

    /**
     * Get all targets from the API
     * @return ArrayList<JSONObject>
     */
    public ArrayList<JSONObject> getTargets() {

        try {
            HttpResponse<String> response = HttpRequestBuilder.get(cm.getProperty("api.url") + "/targets");
            JSONObject jsonItem = (JSONObject) parser.parse(response.body());
            ArrayList<JSONObject> targets = new ArrayList<>();
            JSONArray jsonTargets = (JSONArray) jsonItem.get("targets");
            for (Object item : jsonTargets) {
                JSONObject jsonT = (JSONObject) item;
                targets.add(jsonT);
            }
            return targets;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


        return null;
    }

    /**
     * Add a target to the API
     * @param target Target
     * @return boolean
     */
    public boolean addTarget(Target target) {
        //TODO : Implement this method
        try {
            JSONObject json = new JSONObject();
            json.put("codeName", target.getCodeName());
            json.put("name", target.getName());
            String jsonBody = json.toString();

            HttpResponse<String> response = HttpRequestBuilder.post(
                    cm.getProperty("api.url") + "/target/add",
                    jsonBody
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = (JSONObject) parser.parse(response.body());
                String generatedHash = (String) responseBody.get("hash");
                target.setHash(generatedHash);
                return true;
            } else {
                System.err.println("Failed to add target. Status code: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a target from the API
     * @param target Target
     * @return boolean
     */
    public boolean deleteTarget(Target target) {
        //TODO : Implement this method
        try {
            HttpResponse<String> response = HttpRequestBuilder.delete(
                    cm.getProperty("api.url") + "/target/" + target.getHash()
            );
            if (response.statusCode() == 200) {
                JSONObject jsonObject = (JSONObject) parser.parse(response.body());
                return (boolean) jsonObject.get("success");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Build a JSON string from a Target object
     * @param t Target
     * @return String
     */
    private String buildJsonStringFromObject(Target t){
        //TODO : Implement this method
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash",t.getHash());
        jsonObject.put("codeName",t.getCodeName());
        jsonObject.put("name",t.getName());
        jsonObject.put("positions",t.getPositions());
        return jsonObject.toString();
    }
}
