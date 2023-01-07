package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.Login;
import pages.Logout;
import pages.Register;

import java.util.ArrayList;

public interface VisitableHomepageNotAuthentified {
    /**
     *
     * @param logout logout page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Logout logout, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param login login page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Login login, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param register register page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Register register, ArrayList<Action> actions, Platform platform);
}
