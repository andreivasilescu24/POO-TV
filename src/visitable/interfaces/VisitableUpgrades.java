package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.HomepageAuthentified;
import pages.SeeDetails;

import java.util.ArrayList;

public interface VisitableUpgrades {
    /**
     *
     * @param homepageAuthentified authentified homepage
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(HomepageAuthentified homepageAuthentified,
                ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param seeDetails see details page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(SeeDetails seeDetails, ArrayList<Action> actions, Platform platform);
}
