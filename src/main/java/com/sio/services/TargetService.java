package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Target;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class TargetService {

    private final TargetRepository tRepository;
    private final PositionRepository pRepository;

    public TargetService() {
        this.tRepository = new TargetRepository();
        this.pRepository = new PositionRepository();
    }

    /**
     * Get all targets stored in database and their respective positions
     * @return targets ArrayList
     *
     */
    public ArrayList<Target> getTargets() {
      //TODO implements this method
        ArrayList<Target> targets = tRepository.findAll();
        for (Target target : targets) {
            target.setPositions(pRepository.findByTargetHash(target.getHash()));
        }
        return targets;
    }

    /**
     * Add a target to the API
     * Print the following message after the target is succesfully added to the API : Target succesfully added, you will now have to wait 60 seconds before the target is available for position acquisition
     * @param codename String
     * @param name String
     */
    public void addTarget(String codename, String name) {
        //TODO implements this method

        Target newTarget = new Target();
        String hash = Integer.toHexString((codename+name).hashCode());
        newTarget.setHash(hash);
        newTarget.setCodeName(codename);
        newTarget.setName(name);
        tRepository.create(newTarget);

        System.out.println("Target successfully added, you will now have to wait 60 seconds before the target is available for position acquisition");
    }


    /**
     * Delete a target from the database and the API
     *
     * @param t Target
     * @return
     */
    public boolean deleteTarget(Target t) {
        //TODO implements this method
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/v1/target/" + t.getHash()))
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString("{\n  \"code_name\":\"" + t.getCodeName() + "\",\n  \"name\":\"" + t.getName() + "\"\n}"))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            tRepository.delete(t);
            if (response.statusCode() == 200) {
                pRepository.deleteByTargetHash(t.getHash());
                System.out.println("La cible et ses positions ont été supprimées avec succès.");
                return true;
            } else {
                System.out.println("Erreur : cible non supprimée de l'API. Code de réponse : " + response.statusCode());
                System.out.println("Détails de la réponse : " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la communication avec l'API.");
        }
        return false;
    }

}
