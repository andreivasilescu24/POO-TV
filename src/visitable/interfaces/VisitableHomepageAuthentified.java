package visitable.interfaces;

import platform.Platform;
import input.data.Action;
import pages.Login;
import pages.Register;
import pages.SeeDetails;
import pages.Movies;
import pages.Upgrades;

import java.util.ArrayList;

public interface VisitableHomepageAuthentified {
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

    /**
     *
     * @param seeDetails see details page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(SeeDetails seeDetails, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param movies movies page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Movies movies, ArrayList<Action> actions, Platform platform);

    /**
     *
     * @param upgrades upgrades page
     * @param actions action array given as input
     * @param platform the platform which contains all the possible pages
     */
    void accept(Upgrades upgrades, ArrayList<Action> actions, Platform platform);
}
