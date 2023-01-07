package visitor.interfaces;

import platform.Platform;
import input.data.Action;
import pages.Login;
import pages.Register;

import java.util.ArrayList;

public interface VisitorHomepageNotAuthentified {
    /**
     *
     * @param login login page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Login login, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param register register page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Register register, ArrayList<Action> actions, Platform platform);
}
