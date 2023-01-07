package visitor.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageNotAuthentified;

import java.util.ArrayList;

public interface VisitorLogout {
    /**
     *
     * @param homepageNotAuthentified not authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void visit(HomepageNotAuthentified homepageNotAuthentified,
               ArrayList<Action> actions, Platform platform);
}
