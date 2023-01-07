package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import input.data.Movie;
import platform.Platform;
import input.data.Action;
import input.data.Credentials;
import input.data.User;
import visitable.interfaces.VisitableLogin;
import visitor.interfaces.VisitorLogin;

import java.util.ArrayList;

public final class Login extends GeneralPage implements VisitableLogin, VisitorLogin {
    @JsonIgnore
    private static Login instance = null;

    private Login() {

    }

    public static Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }

    /**
     * @param actions  action array in the input
     * @param platform the platform which contains all the possible
     *                 pages returns to the homepage not authentified page
     *                 <p>
     *                 interpretor for the actions given as input when the user is on login page
     */
    public void loginActionInterpretor(final ArrayList<Action> actions, final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);
            if (actualAction.getType().equals("change page")) {
                platform.updateActions(actions);
                platform.throwError();
                platform.setError(null);
                platform.getHomepageNotAuthentified().
                        returnToHomepageNotAuthentified(actions, platform);
            } else if (actualAction.getType().equals("on page")) {
                if (actualAction.getFeature().equals("login")) {
                    boolean isLoginSuccessful = login(actualAction.getCredentials(), platform);

                    if (isLoginSuccessful) {
                        platform.getHomepageAuthentified().accept(this, actions, platform);
                    } else {
                        platform.updateActions(actions);
                        platform.throwError();
                        platform.setError(null);
                        platform.getHomepageNotAuthentified().
                                returnToHomepageNotAuthentified(actions, platform);
                    }
                } else {
                    platform.updateActions(actions);
                    platform.throwError();
                    platform.setError(null);
                    platform.getHomepageNotAuthentified().
                            returnToHomepageNotAuthentified(actions, platform);
                }
            }
        }
    }

    /**
     * @param credentials the credentials of the user who wants to login
     * @param platform    the platform which contains all the possible
     *                    pages returns to the homepage not authentified page
     * @return true - if login is successful
     * false - if login is not successful
     */
    public boolean login(final Credentials credentials, final Platform platform) {
        for (User user : platform.getInputData().getUsers()) {
            if (user.getCredentials().getName().equals(credentials.getName())
                    && user.getCredentials().getPassword().equals(credentials.getPassword())) {

                this.setCurrentUser(user);
                return true;
            }
        }

        return false;
    }

    @Override
    public void accept(final HomepageNotAuthentified homepageNotAuthentified,
                       final ArrayList<Action> actions, final Platform platform) {
        homepageNotAuthentified.visit(this, actions, platform);
    }

    @Override
    public void visit(final HomepageAuthentified homepageAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {
        homepageAuthentified.homepageAuthentifiedActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final HomepageNotAuthentified homepageNotAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {

    }
}
