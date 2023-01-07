package visitor.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageAuthentified;
import pages.HomepageNotAuthentified;

import java.util.ArrayList;

public interface VisitorRegister {
    /**
     *
     * @param homepageAuthentified authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(HomepageAuthentified homepageAuthentified,
               ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param homepageNotAuthentified not authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(HomepageNotAuthentified homepageNotAuthentified,
               ArrayList<Action> actions, Platform platform);
}
