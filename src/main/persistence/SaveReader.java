package persistence;

import com.google.gson.Gson;
import ui.UserSession;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

// Represents a reader object to deserialize JSON files and reconstruct objects
public class SaveReader {
    Reader fileReader;
    Gson gson;

    // Constructor
    public SaveReader(String file) throws IOException {
        fileReader = new FileReader(file);
        gson = new Gson();
    }

    // EFFECTS: deserializes JournalEntry save file and returns reconstructed object
    public UserSession readUserSession() {
        return gson.fromJson(fileReader, UserSession.class);
    }

    // EFFECTS: deserializes user list save file and returns reconstructed object
    public ArrayList<String> readUserList() {
        return gson.fromJson(fileReader, (Type) ArrayList.class);
    }

}
