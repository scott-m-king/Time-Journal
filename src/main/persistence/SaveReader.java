package persistence;

import com.google.gson.Gson;
import model.CategoryList;
import model.JournalLog;

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
    public JournalLog readJournalEntries() {
        return gson.fromJson(fileReader, JournalLog.class);
    }

    // EFFECTS: deserializes CategoryList save file and returns reconstructed object
    public CategoryList readCategoryList() {
        return gson.fromJson(fileReader, CategoryList.class);
    }

    // EFFECTS: deserializes user list save file and returns reconstructed object
    public ArrayList<String> readUserList() {
        return gson.fromJson(fileReader, (Type) ArrayList.class);
    }

}
