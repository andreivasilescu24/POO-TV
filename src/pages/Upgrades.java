package pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import platform.Platform;
import input.data.Action;
import input.data.User;
import visitable.interfaces.VisitableUpgrades;
import visitor.interfaces.VisitorUpgrades;

import java.util.ArrayList;

public final class Upgrades extends GeneralPage
        implements VisitableUpgrades, VisitorUpgrades {
    @JsonIgnore
    private static Upgrades instance = null;

    private Upgrades() {

    }

    public static Upgrades getInstance() {
        if (instance == null) {
            instance = new Upgrades();
        }
        return instance;
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     *                 <p>
     *                 interpretor for the actions given as input when the user is on upgrades page
     */
    public void upgradesActionInterpretor(final ArrayList<Action> actions,
                                          final Platform platform) {
        platform.updateActions(actions);

        if (actions.size() == 0) {
            platform.handleEmptyActions();
        } else {
            Action actualAction = actions.get(0);

            if (actualAction.getType().equals("on page")) {
                if (actualAction.getFeature().equals("buy tokens")) {
                    int nrTokens = Integer.parseInt(actualAction.getCount());
                    int balance = Integer.parseInt(getCurrentUser().getCredentials().getBalance());

                    balance -= nrTokens;
                    User currentUser = getCurrentUser();
                    User updatedUser = new User(currentUser.getCredentials(),
                            currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
                            currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
                            currentUser.getLikedMovies(), currentUser.getRatedMovies(),
                            currentUser.getNotifications(), currentUser.getSubscribedGenres());

                    updatedUser.setTokensCount(nrTokens);
                    updatedUser.getCredentials().setBalance(Integer.toString(balance));

                    platform.updateUser(updatedUser);

                    returnToUpgradesPage(actions, platform);
                }

                if (actualAction.getFeature().equals("buy premium account")) {
                    final int costPremiumAccount = 10;
                    int nrTokens = getCurrentUser().getTokensCount();

                    nrTokens -= costPremiumAccount;

                    User currentUser = getCurrentUser();
                    User updatedUser = new User(currentUser.getCredentials(),
                            currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
                            currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
                            currentUser.getLikedMovies(), currentUser.getRatedMovies(),
                            currentUser.getNotifications(), currentUser.getSubscribedGenres());

                    updatedUser.setTokensCount(nrTokens);
                    updatedUser.getCredentials().setAccountType("premium");

                    platform.updateUser(updatedUser);

                    returnToUpgradesPage(actions, platform);
                }
            } else if (actualAction.getType().equals("change page")) {
                String pageToChange = actualAction.getPage();
                if (getPermissions().contains(pageToChange)) {
                    if (actualAction.getPage().equals("movies")) {
                        platform.getMovies().accept(this, actions, platform);
                    } else if(actualAction.getPage().equals("logout")) {
                        platform.getLogout().accept(this, actions, platform);
                    }
                } else {
                    platform.throwError();
                    platform.setError(null);
                    returnToUpgradesPage(actions, platform);
                }
            }
        }
    }

    /**
     * @param actions  action array given as input
     * @param platform the platform which contains all
     *                 the possible pages
     *                 <p>
     *                 this method goes back to interpreting actions for the upgrades page
     */
    public void returnToUpgradesPage(final ArrayList<Action> actions, final Platform platform) {
        upgradesActionInterpretor(actions, platform);
    }

    @Override
    public void accept(final HomepageAuthentified homepageAuthentified,
                       final ArrayList<Action> actions, final Platform platform) {
        homepageAuthentified.visit(this, actions, platform);
    }

    @Override
    public void accept(final SeeDetails seeDetails,
                       final ArrayList<Action> actions, final Platform platform) {
        seeDetails.visit(this, actions, platform);
    }

    @Override
    public void visit(final Movies movies,
                      final ArrayList<Action> actions, final Platform platform) {
        movies.moviesActionInterpretor(actions, platform);
    }

    @Override
    public void visit(final Logout logout,
                      final ArrayList<Action> actions, final Platform platform) {
        platform.updateActions(actions);
        platform.getPagesStack().add("Upgrades");
        logout.logout(actions, platform);
    }

    @Override
    public void visit(final HomepageAuthentified homepageAuthentified,
                      final ArrayList<Action> actions, final Platform platform) {

    }
}
