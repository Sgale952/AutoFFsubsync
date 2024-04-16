package github.sgale;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static github.sgale.PropertyGenerator.loadSettingsFile;
import static github.sgale.SubSyncer.processSubs;

public class Main {
    public static void main(String[] args) {
        loadSettingsFile();

        try {
            processSubs();
        }
        catch (Exception e) {
            writeError(e.toString());
        }
    }

    private static void writeError(String message) {
        try {
            File file = new File("autoFFsubsyncError.txt");
            FileWriter writer = new FileWriter(file);

            file.createNewFile();
            writer.write(message);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}