package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Position;
import com.sio.models.Target;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TrackingService {

    private final MockChrevTzyonApiClient mockChrevTzyonApiClient = new MockChrevTzyonApiClient();
    private final TargetRepository targetRepository;
    private final PositionRepository positionRepository;

    public TrackingService() {
        this.targetRepository = new TargetRepository();
        this.positionRepository = new PositionRepository();
    }

    /**
     * Update the positions of all targets
     * If the target does not exist in the database, create it
     * If the target exists in the database, add its new position
     * Print the following message after the position is successfully acquired : - {target code name} : Position successfully acquired
     * Print the following message after the position is not acquired (API has not yet acquired the position : - {target code name} : Position not acquired
     */
    public void updateTargetsPositions() {
        //TODO implements this method
        ArrayList<JSONObject> apiTargets = mockChrevTzyonApiClient.getTargets();

        for (JSONObject apiTarget : apiTargets) {
            String targetHash = (String) apiTarget.get("hash");
            String codeName = (String) apiTarget.get("codeName");
            String name = (String) apiTarget.get("name");

            if (targetHash == null || codeName == null || codeName.isEmpty()) {
                System.out.println("Invalid target data for hash: " + targetHash);
                continue;
            }

            Target target = targetRepository.findByHash(targetHash);
            if (target == null) {
                target = new Target(targetHash, codeName, name);
                targetRepository.create(target);
            }

            JSONArray positionsArray = (JSONArray) apiTarget.get("positions");
            if (positionsArray == null || positionsArray.isEmpty()) {
                System.out.println("- " + target.getCodeName() + " : no positions");
                continue;
            }

            for (Object positionObj : positionsArray) {
                JSONObject positionJson = (JSONObject) positionObj;
                Float latitude = ((Number) positionJson.get("latitude")).floatValue();
                Float longitude = ((Number) positionJson.get("longitude")).floatValue();
                Timestamp timestamp = Timestamp.valueOf((String) positionJson.get("timestamp"));

                Position newPosition = new Position(target, latitude, longitude, timestamp);
                positionRepository.create(newPosition);

                System.out.println("- " + target.getCodeName() +
                        (newPosition.getId() != 0 ? " Position successfully acquired" : " Position not acquired "));
            }
        }
    }
}
