package persistence;

import com.google.gson.*;
import ui.UserSession;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;

// https://www.javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/package-summary.html
// https://mkyong.com/java/how-to-parse-json-with-gson/

// Represents a writer object to serialize JournalLog and CategoryList into JSON format and write to file in data folder
public class SaveWriter {
    FileWriter writer;
    Gson gson;

    // Constructor
    public SaveWriter(File file) throws IOException {
        writer = new FileWriter(file);
    }

    // MODIFIES: this, ./data/users_save.json
    // EFFECTS: serializes user list to JSON and writes to save file
    public void save(ArrayList<String> userList) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(userList, writer);
    }

    public void save(UserSession userSession) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(userSession, writer);
    }

    // MODIFIES: this
    // EFFECTS: closes FileWriter object
    public void close() throws IOException {
        writer.close();
    }
}