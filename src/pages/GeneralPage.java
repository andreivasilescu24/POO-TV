package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import input.data.User;

import java.util.ArrayList;

public class GeneralPage {
    @JsonIgnore
    private ArrayList<String> permissions = new ArrayList<>();
    private User currentUser;

    /**
     *
     * @return current user of the platform
     */
    public User getCurrentUser() {
        return currentUser;
    }
    /**
     *
     * @return permissions for every tupe of page in the platform
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    /**
     *
     * @param currentUser a new user of the platform
     */
    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     *
     * @param permissions permissions for a page in the platform
     */
    public void setPermissions(final ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
