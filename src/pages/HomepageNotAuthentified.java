package pages;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import platform.Platform;
import input.data.Action;
import visitable.interfaces.VisitableHomepageNotAuthentified;
import visitor.interfaces.VisitorHomepageNotAuthentified;

public final class HomepageNotAuthentified extends GeneralPage
        implements VisitableHomepageNotAuthentified, VisitorHomepageNotAuthentified {
    @JsonIgnore
    private static HomepageNotAuthentified instance = null;

    private HomepageNotAuthentified() {

    }

    public static HomepageNotAuthentified getInstance() {
        if (instance == null) {
            instance = new HomepageNotAuthentified();
        }
        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     */
    public void homepageNotAuthentifiedActionInterpretor(final ArrayList<Action> actions,
                                                         final Platform platform) {
        if (actions.size() == 0) {
            return;
        }

        Action actualAction = actions.get(0);
        if (actualAction.getType().equals("change page")) {
            String pageToChange = actualAction.getPage();
            if (this.getPermissions().contains(pageToChange)) {
                if (pageToChange.equals("login")) {
                    Login login = platform.getLogin();
                    login.accept(this, actions, platform);
                } else if (pageToChange.equals("register")) {
                    Register register = platform.getRegister();
                    register.accept(this, actions, platform);
                }
            } else {
                platform.updateActions(actions);
                platform.throwError();
                platform.setError(null);
                returnToHomepageNotAuthentified(actions, platform);
            }
        } else {
            platform.updateActions(actions);
            platform.throwError();
            platform.setError(null);
            returnToHomepageNotAuthentified(actions, platform);
        }
    }

    @Override
    public void visit(final Login login, final ArrayList<Action> actions,
                      final Platform platform) {
        login.loginActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final Register register, final ArrayList<Action> actions,
                      final Platform platform) {
        register.registerActionInterpretor(actions, platform);
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all the possible pages returns to the homepage
     *                 not authentified page
     */
    public void returnToHomepageNotAuthentified(final ArrayList<Action> actions,
                                                final Platform platform) {
        homepageNotAuthentifiedActionInterpretor(actions, platform);
    }

    @Override
    public void accept(final Logout logout, final ArrayList<Action> actions,
                       final Platform platform) {
        logout.visit(this, actions, platform);
    }

    @Override
    public void accept(final Login login, final ArrayList<Action> actions,
                       final Platform platform) {

    }

    @Override
    public void accept(final Register register, final ArrayList<Action> actions,
                       final Platform platform) {

    }

}
