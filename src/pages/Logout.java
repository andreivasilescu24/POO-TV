package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import platform.Platform;
import input.data.Action;
import visitable.interfaces.VisitableLogout;
import visitor.interfaces.VisitorLogout;

import java.util.ArrayList;

public final class Logout extends GeneralPage implements VisitableLogout, VisitorLogout {
    @JsonIgnore
    private static Logout instance = null;

    private Logout() {

    }

    public static Logout getInstance() {
        if (instance == null) {
            instance = new Logout();
        }
        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all the possible pages returns to the homepage
     *                 not authentified page
     *                 <p>
     *                 logout functionality, by setting the current user to "null" (logging out)
     */
    public void logout(final ArrayList<Action> actions, final Platform platform) {
        platform.getHomepageAuthentified().setCurrentUser(null);
        platform.getHomepageNotAuthentified().accept(this, actions, platform);
    }

    @Override
    public void accept(final HomepageAuthentified homepageAuthentified,
                       final ArrayList<Action> actions, final Platform platform) {
        homepageAuthentified.visit(this, actions, platform);
    }

    @Override
    public void accept(final Upgrades upgrades,
                       final ArrayList<Action> actions, final Platform platform) {

    }

    @Override
    public void accept(final SeeDetails seeDetails,
                       final ArrayList<Action> actions, final Platform platform) {
        seeDetails.visit(this, actions, platform);
    }

    @Override
    public void accept(final Movies movies,
                       final ArrayList<Action> actions, final Platform platform) {
        movies.visit(this, actions, platform);
    }

    @Override
    public void visit(final HomepageNotAuthentified homepageNotAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {
        platform.getPagesStack().clear();
        homepageNotAuthentified.returnToHomepageNotAuthentified(actions, platform);
    }
}
