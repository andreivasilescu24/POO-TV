package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import platform.Platform;
import input.data.Action;
import input.data.Credentials;
import input.data.User;
import visitable.interfaces.VisitableRegister;
import visitor.interfaces.VisitorRegister;

import java.util.ArrayList;

public final class Register extends GeneralPage
        implements VisitorRegister, VisitableRegister {
    @JsonIgnore
    private static Register instance = null;

    private Register() {

    }

    public static Register getInstance() {
        if (instance == null) {
            instance = new Register();
        }

        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     *                 <p>
     *                 interpretor for the actions given as input when the user is on register page
     */
    public void registerActionInterpretor(final ArrayList<Action> actions,
                                          final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);
            if (actualAction.getType().equals("change page")) {
                platform.updateActions(actions);
                platform.throwError();
                platform.setError(null);
                // return to not authentified homepage
                platform.getHomepageNotAuthentified()
                        .returnToHomepageNotAuthentified(actions, platform);
            } else if (actualAction.getType().equals("on page")) {
                if (actualAction.getFeature().equals("register")) {
                    boolean isRegisterSuccessful = register(actualAction.getCredentials(), platform);
                    if (isRegisterSuccessful) {
                        platform.getHomepageAuthentified().accept(this, actions, platform);
                    } else {
                        platform.updateActions(actions);
                        platform.throwError();
                        platform.setError(null);
                        platform.getHomepageNotAuthentified()
                                .returnToHomepageNotAuthentified(actions, platform);
                    }

                } else {
                    platform.updateActions(actions);
                    platform.throwError();
                    platform.setError(null);
                    platform.getHomepageNotAuthentified()
                            .returnToHomepageNotAuthentified(actions, platform);
                }
            }
        }
    }

    boolean register(final Credentials credentials, final Platform platform) {
        for (User user : platform.getInputData().getUsers()) {
            if (user.getCredentials().getName().equals(credentials.getName())) {
                return false;
            }
        }
        User userAdded = new User();
        userAdded.setCredentials(credentials);
        this.setCurrentUser(userAdded);

        platform.getInputData().getUsers().add(userAdded);
        return true;
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
