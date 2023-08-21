import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tracert {
    public static void main(String[] args) throws IOException {
        // Define input and output file paths
        String tcpdump = "sampletcpdump.txt";
        String analysis = "analysis.txt";

        // Create BufferedReader to read from input file
        BufferedReader inputFile = new BufferedReader(new FileReader(tcpdump));
        // Create FileWriter to write to output file
        FileWriter outputFile = new FileWriter(analysis);
        // Create an ArrayList to store lines from input file
        List<String> lines = new ArrayList<>();

        // Read first line from input file
        String line = inputFile.readLine();
        // Loop until end of file is reached
        while (line != null) {
            // Add current line to list of lines
            lines.add(line);
            // Read next line from input file
            line = inputFile.readLine();
        }

        // Initialize variables for loop
        int placeHolder = 0;
        int writer = 0;

        // Iterate over list of lines
        for (String curr : lines) {
            // Initialize variables for current line
            String id = null;
            String ttl = null;
            String time_1 = null;
            String time_2 = null;

            // Check if current line contains "id"
            if (curr.contains("id")) {
                // Extract id, ttl, and time_1 from current line
                id = curr.substring(curr.indexOf("id"), curr.indexOf(", o")).trim();
                ttl = curr.substring(curr.indexOf("ttl"), curr.indexOf(", i")).trim();
                time_1 = curr.substring(0, curr.indexOf("IP")).trim();
            }

            // Iterate over subsequent lines
            for (int i = placeHolder + 1; i < lines.size(); i++) {
                // Get current line to compare
                String compare = lines.get(i);
                // Check if compare line contains "id"
                if (compare.contains("id")) {
                    // Extract id from compare line
                    String check = compare.substring(compare.indexOf("id"), compare.indexOf(", o")).trim();
                    // Check if ids match and id is not 0
                    if (check.equals(id) && !check.equals("id 0")) {
                        // Extract IP from previous line
                        String ip_line = lines.get(i - 1);
                        String IP = ip_line.substring(0, ip_line.indexOf(" >")).trim();
                        // Extract time_2 from two lines before
                        String timeLine = lines.get(i - 2);
                        time_2 = timeLine.substring(0, timeLine.indexOf("IP")).trim();
                        // Calculate time difference and round to 3 decimal places
                        double time_difference = Math.round((Double.parseDouble(time_2) - Double.parseDouble(time_1)) * 1000 * 1000) / 1000.0;
                        // Check value of writer variable to determine what to write to output file
                        if (writer == 0) {
                            outputFile.write(ttl.toUpperCase() + "\n");
                            outputFile.write(IP + "\n");
                            outputFile.write(time_difference + " ms\n");
                            writer++;
                        } else if (writer % 3 == 0 && writer != 0) {
                            outputFile.write(ttl.toUpperCase() + "\n");
                            outputFile.write(IP + "\n");
                            outputFile.write(time_difference + " ms\n");
                            writer++;
                        } else {
                            outputFile.write(time_difference + " ms\n");
                            writer++;
                        }
                    }
                }
            }
            placeHolder++;
        }

        // Close input and output files
        inputFile.close();
        outputFile.close();
    }
}