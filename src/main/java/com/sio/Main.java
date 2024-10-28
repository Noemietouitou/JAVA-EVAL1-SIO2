package com.sio;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Position;
import com.sio.models.Target;
import com.sio.services.TargetService;
import com.sio.services.TrackingService;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TargetService targetService = new TargetService();
    private static final TrackingService trackingService = new TrackingService();
    private static final String GREEN = "\u001B[32m";
    private static String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println(GREEN);
        printConnectBanner();

        while(true) {
            System.out.println("===============================================");
            System.out.println("Actions menu");
            System.out.println("===============================================");
            System.out.println("1. List targets");
            System.out.println("2. Acquire targets positions");
            System.out.println("3. Add target");
            System.out.println("4. Delete target");
            System.out.println("0. Exit");
            System.out.println("===============================================");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            ArrayList<Target> targets;
            switch (option) {
                case 1:
                    System.out.println("List targets");
                    System.out.println("-----------------------------------------------");
                    //TODO : Get all targets from the database and print them

                    MockChrevTzyonApiClient apiClient = new MockChrevTzyonApiClient();
                    ArrayList<JSONObject> apiTargets = apiClient.getTargets();

                    for (JSONObject jsonTarget : apiTargets) {
                        String codeName = (String) jsonTarget.get("code_name");
                        String name = (String) jsonTarget.get("name");

                        targetService.addTarget(codeName, name);
                    }

                    ArrayList<Target> allTargets = targetService.getTargets();
                    int nbTargets = allTargets.size();
                    System.out.println(nbTargets + " targets found.");
                    System.out.println("-----------------------------------------------");

                    if (nbTargets == 0) {
                        System.out.println("No targets found.");
                    } else {
                        for (Target target : allTargets) {
                            System.out.println(target.getCodeName() + " : " + target.getName() + " : " + target.getPositions().size() + " positions");
                        }
                    }
                    System.out.println("-----------------------------------------------");
                    break;


                case 2:
                    System.out.println("Acquire targets positions");
                    System.out.println("-----------------------------------------------");
                    //TODO : Acquire all targets positions
                    trackingService.updateTargetsPositions();
                    System.out.println("-----------------------------------------------");
                    break;
                case 3:
                    System.out.println("Add target");
                    System.out.println("-----------------------------------------------");
                    //TODO : Add a target

                    System.out.println("\nEnter new codeName :");
                    String codeName = scanner.nextLine();
                    System.out.println("\nEnter target's fullname :");
                    String name = scanner.nextLine();

                    targetService.addTarget(codeName, name);
                    break;

                case 4:
                    System.out.println("Delete target");
                    System.out.println("-----------------------------------------------");
                    //TODO : Delete a target

                    ArrayList<Target> lestargets = targetService.getTargets();

                    if (lestargets.isEmpty()) {
                        System.out.println("No targets available to delete.");
                    } else {

                        for (int i = 0; i < lestargets.size(); i++) {
                            System.out.println((i + 1) + ". " + lestargets.get(i).getName());
                        }

                        System.out.println("Choose a target to delete (enter 0 to leave delete mode):");
                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        if (choice == 0) {
                            System.out.println("Exiting delete mode.");
                        } else if (choice > 0 && choice <= lestargets.size()) {
                            Target targetToDelete = lestargets.get(choice - 1);
                            System.out.println("Deleting target " + targetToDelete.getName());

                            boolean deletionSuccess = targetService.deleteTarget(targetToDelete);

                            if (deletionSuccess) {
                                System.out.println("Target " + targetToDelete.getName() + " deleted successfully.");
                            } else {
                                System.out.println("Failed to delete target " + targetToDelete.getName() + ".");
                            }
                        } else {
                            System.out.println("Invalid selection. Please try again.");
                        }
                    }

                    System.out.println("-----------------------------------------------");
                    break;

                case 0:
                    printDisconnectBanner();
                    System.out.println(RESET);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void printConnectBanner() {

        System.out.println("===============================================");
        System.out.println("    CHREV TZYON INTERFACE     ");
        System.out.println("===============================================");
        System.out.println("    Establishing connection to satellite...    ");
        System.out.println("===============================================");

        String[] progressIndicators = {"|", "/", "-", "\\"};
        int iterations = 10;

        for (int i = 0; i < iterations; i++) {
            System.out.print("\rConnecting " + progressIndicators[i % progressIndicators.length]);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\rConnection established.        ");
        System.out.println("===============================================");
        System.out.println("Satellite link active. Ready to transmit data.");
        System.out.println("===============================================");

    }

    private static void printDisconnectBanner() {
        System.out.println("===============================================");
        System.out.println("    Closing connection to satellite...         ");
        System.out.println("===============================================");

        String[] progressIndicators = {"|", "/", "-", "\\"};
        int iterations = 10;

        for (int i = 0; i < iterations; i++) {
            System.out.print("\rDisconnecting " + progressIndicators[i % progressIndicators.length]);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\rConnection Closed.        ");
        System.out.println("===============================================");

    }
}