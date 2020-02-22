package persistence;

import com.google.gson.Gson;
import model.CategoryList;
import model.JournalLog;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

// Represents a reader object to deserialize JSON files and reconstruct objects
public class SaveReader {
    Reader fileReader;
    Gson gson;

    public SaveReader(String file) throws IOException {
        fileReader = new FileReader(file);
        gson = new Gson();
    }

    public JournalLog readJournalEntries() {
        return gson.fromJson(fileReader, JournalLog.class);
    }

    public CategoryList readCategoryList() {
        return gson.fromJson(fileReader, CategoryList.class);
    }

}
