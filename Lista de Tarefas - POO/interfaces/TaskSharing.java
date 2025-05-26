package todoapp.interfaces;

import java.util.List;

public interface TaskSharing {
    void shareWith(String userId);
    void removeSharing(String userId);
    boolean isSharedWith(String userId);
    List<String> getSharedUsers();
}