package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCategoryData {
    public static void main(String[] args) {
        // Path to your file
        String filePath = "category_data.txt";

        // List to store category names
        List<String> categoryNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                String categoryName = line.trim(); // Trim any white spaces
                // Add category name to the list
                categoryNames.add(categoryName);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException 
        }

        // Display the list of category names and format them 
        System.out.print("Category Names: ");
        for (int i = 0; i < categoryNames.size(); i++) {
            if (i == categoryNames.size() - 1) {
                System.out.print(categoryNames.get(i));
            } else {
                System.out.print(categoryNames.get(i) + ", ");
            }
        }
    }
}