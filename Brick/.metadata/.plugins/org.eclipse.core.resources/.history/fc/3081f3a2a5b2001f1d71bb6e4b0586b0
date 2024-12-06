package Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadAssetData {

	// Class that is used to test that the data is being stored and printing it out 
    public static void main(String[] args) {
        readAssetData("asset_data.csv");
    }

    public static void readAssetData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by commas to get individual fields
                String[] fields = line.split(",");
                // Print each field
                for (String field : fields) {
                    System.out.print(field + "\t");
                }
                System.out.println(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}