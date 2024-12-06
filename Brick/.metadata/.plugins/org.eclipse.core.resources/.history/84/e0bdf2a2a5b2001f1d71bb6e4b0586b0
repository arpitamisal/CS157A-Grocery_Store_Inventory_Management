package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLocationData {
    public static void main(String[] args) {
        String filePath = "location_data.txt";

        // Lists to store location names and descriptions
        List<String> locationNames = new ArrayList<>();
        List<String> locationDescriptions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Split the line into name and description using tab as delimiter
                String[] parts = line.split("\t");
                // Extract the name and description
                String locationName = parts[0].trim(); // Trim any white spaces
                locationNames.add(locationName);
                if (parts.length > 1) {
                    String locationDesc = parts[1].trim(); // Trim any white spaces
                    locationDescriptions.add(locationDesc);
                } else {
                    locationDescriptions.add(""); // Add an empty description if none provided
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException 
        }

        // Display the list of location names and descriptions
        System.out.println("Locations:");
        for (int i = 0; i < locationNames.size(); i++) {
            System.out.print("Location Name: " + locationNames.get(i));
            if (!locationDescriptions.get(i).isEmpty()) {
                System.out.print(", Description: " + locationDescriptions.get(i));
            } else {
            	System.out.print(", Description: No Description");
            }
            System.out.println(); // Move to the next line for the next location
        }
    }
}