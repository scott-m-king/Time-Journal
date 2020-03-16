package persistence;

import com.google.gson.*;
import model.*;
import ui.TimeJournalApp;

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

    // MODIFIES: this, user journal_save.json file
    // EFFECTS: serializes JournalLog object to JSON and writes to user save file
    public void save(JournalLog j) {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        gson.toJson(j, writer);
    }

    // MODIFIES: this, user category_save.json file
    // EFFECTS: serializes JournalLog object to JSON and writes to user save file
    public void save(CategoryList c) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(c, writer);
    }

    // MODIFIES: this, ./data/users_save.json
    // EFFECTS: serializes user list to JSON and writes to save file
    public void save(ArrayList<String> userList) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(userList, writer);
    }

    public void save(TimeJournalApp userSession) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(userSession, writer);
    }

    // MODIFIES: this
    // EFFECTS: closes FileWriter object
    public void close() throws IOException {
        writer.close();
    }
}