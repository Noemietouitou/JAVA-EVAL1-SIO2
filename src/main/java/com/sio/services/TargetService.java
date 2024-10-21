package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Target;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;

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

        String hash = generateHash(codename);
        Target newTarget = new Target();
        newTarget.setHash(hash);
        newTarget.setName(name);
        newTarget.setCodeName(codename);

        MockChrevTzyonApiClient apiClient = new MockChrevTzyonApiClient();
        boolean apiSuccess = apiClient.addTarget(newTarget);

        if (apiSuccess) {
            tRepository.create(newTarget);
            System.out.println("Target successfully added. You will now have to wait 60 seconds before the target is available for position acquisition.");
        } else {
            System.out.println("Target not added.");
        }
    }

    // Je rajoute une methode pour generer un hash afin de faciliter l'entree de la target en base de donnees
    private String generateHash(String codename) {
        return String.valueOf(codename.hashCode());
    }

    /**
     * Delete a target from the database and the API
     * @param t Target
     */
    public void deleteTarget(Target t) {
        //TODO implements this method
        MockChrevTzyonApiClient apiClient = new MockChrevTzyonApiClient();
        boolean apiSuccess = apiClient.deleteTarget(t);

        if (apiSuccess) {
            pRepository.deleteByTargetHash(t.getHash());
            tRepository.delete(t);
            System.out.println("Target and its positions were successfully deleted.");
        } else {
            System.out.println("Error: target not deleted from API.");
        }
    }

}
