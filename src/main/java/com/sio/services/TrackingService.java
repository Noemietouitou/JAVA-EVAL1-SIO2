package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Position;
import com.sio.models.Target;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
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
     * If the target does not exist in the database, creatge it
     * If the target exists in the database, add its new position
     * Print the following message after the position is successfully acquired : - {target code name} : Position successfully acquired
     * Print the following message after the position is not acquired (API has not yet acquired the position : - {target code name} : Position not acquired
     */
    public void updateTargetsPositions() {
        //TODO implements this method
        MockChrevTzyonApiClient apiClient = new MockChrevTzyonApiClient();
        ArrayList<JSONObject> targets = apiClient.getTargets();
        ArrayList<Target> lesTargets = targetRepository.findAll();

        if (targets != null) {
            for (JSONObject targetJson : targets) {
                String hash = (String) targetJson.get("hash");
                Target dbTarget = lesTargets.stream().filter(t -> t.getHash().equals(hash)).findFirst().orElse(null);

                if (dbTarget != null) {
                    JSONArray positionsJson = (JSONArray) targetJson.get("positions");

                    for (Object posObj : positionsJson) {
                        JSONObject posJson = (JSONObject) posObj;
                        double latitude = (double) posJson.get("latitude");
                        double longitude = (double) posJson.get("longitude");

                        Instant instant = Instant.parse(posJson.get("Time").toString());
                        Timestamp timestamp = Timestamp.from(instant);

                        Position newPosition = new Position(dbTarget, dbTarget.getLastPosition().getLatitude(), dbTarget.getLastPosition().getLongitude(), timestamp);
                        positionRepository.create(newPosition);
                    }
                } else {
                    System.out.println("Cible avec le hash " + hash + " non trouvée en base de données.");
                }
            }
        } else {
            System.err.println("Erreur lors de la récupération des cibles.");
        }
    }
}
