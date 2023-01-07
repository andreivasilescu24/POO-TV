package visitor.interfaces;

import platform.Platform;
import input.data.Action;
import pages.Logout;
import pages.Movies;
import pages.Upgrades;

import java.util.ArrayList;

public interface VisitorHomepageAuthentified {
    /**
     *
     * @param movies movies page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Movies movies, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param upgrades upgrades page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Upgrades upgrades, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param logout logout page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(Logout logout, ArrayList<Action> actions, Platform platform);
}
