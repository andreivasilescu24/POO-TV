package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageNotAuthentified;

import java.util.ArrayList;

public interface VisitableRegister {
    /**
     *
     * @param homepageNotAuthentified not authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(HomepageNotAuthentified homepageNotAuthentified,
                ArrayList<Action> actions, Platform platform);
}
