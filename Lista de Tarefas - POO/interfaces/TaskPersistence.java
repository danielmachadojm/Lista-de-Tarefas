package todoapp.interfaces;

import java.io.IOException;

public interface TaskPersistence {
    void saveToFile(String filePath) throws IOException;
    void loadFromFile(String filePath) throws IOException, ClassNotFoundException;
    String exportToCSV();
    void importFromCSV(String csvContent);
}