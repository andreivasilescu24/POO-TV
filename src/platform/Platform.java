package platform;

import input.data.*;

import pages.HomepageNotAuthentified;
import pages.HomepageAuthentified;
import pages.Movies;
import pages.Register;
import pages.Login;
import pages.Logout;
import pages.SeeDetails;
import pages.Upgrades;

import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedHashMap;

public final class Platform {
    private Input inputData = new Input();
    private HomepageNotAuthentified homepageNotAuthentified = HomepageNotAuthentified.getInstance();
    private HomepageAuthentified homepageAuthentified = HomepageAuthentified.getInstance();
    private Login login = Login.getInstance();
    private Logout logout = Logout.getInstance();
    private Movies movies = Movies.getInstance();
    private Register register = Register.getInstance();
    private SeeDetails seeDetails = SeeDetails.getInstance();
    private Upgrades upgrades = Upgrades.getInstance();
    private ArrayNode output;
    private String error;
    private ArrayList<Movie> currentMoviesList = new ArrayList<>();
    private ArrayList<Movie> emptyMovies = new ArrayList<>();
    private ArrayList<String> pagesStack = new ArrayList<>();
    private static Platform platformInstance = null;

    public Platform() {

    }

    public static Platform getPlatformInstance() {
        if (platformInstance == null) {
            platformInstance = new Platform();
        }
        return platformInstance;
    }


    /**
     * this method throws an error tot the output, changing the "error"
     * field to "Error" value
     */
    public void throwError() {
        setError("Error");
        output.addObject().put("error", error)
                .putPOJO("currentMoviesList", emptyMovies)
                .putPOJO("currentUser", null);
    }

    /**
     * this method initializes the permissions list for every type of
     * page. Basically each page has stored in this list, every type of page
     * that can be accessed directly from it.
     */
    public void initPermissions() {
        homepageNotAuthentified.getPermissions().add("login");
        homepageNotAuthentified.getPermissions().add("register");

        homepageAuthentified.getPermissions().add("movies");
        homepageAuthentified.getPermissions().add("upgrades");
        homepageAuthentified.getPermissions().add("logout");

        movies.getPermissions().add("see details");
        movies.getPermissions().add("logout");
        movies.getPermissions().add("movies");

        seeDetails.getPermissions().add("movies");
        seeDetails.getPermissions().add("upgrades");
        seeDetails.getPermissions().add("logout");
        seeDetails.getPermissions().add("see details");

        upgrades.getPermissions().add("movies");
        upgrades.getPermissions().add("logout");

    }

    /**
     * @param actions action array given as input
     *                <p>
     *                this method updates the actions array, by removing the first action
     *                in the array, in the moment of the function call, to be able to read
     *                the next command, correctly.
     */
    public void updateActions(final ArrayList<Action> actions) {
        ArrayList<Action> actionsAux = new ArrayList<>();
        actionsAux.addAll(actions);
        actionsAux.remove(0);
        actions.clear();
        actions.addAll(actionsAux);
    }

    /**
     * @param movieDatabase movies which the user has access to
     * @param currentUser   current user of the platform
     * @return the notification which contains the correct recommendation based on the
     * presented algorithm
     */
    public Notification getRecommendation(final ArrayList<Movie> movieDatabase,
                                          final User currentUser) {
        Notification notification = new Notification();
        notification.setMessage("Recommendation");

        if (currentUser.getLikedMovies().size() == 0) {
            notification.setMovieName("No recommendation");
            return notification;
        } else {
            TreeMap<String, Integer> treeMap = new TreeMap<>();
            LinkedHashMap<String, Integer> sortedHashMap = new LinkedHashMap<>();
            ArrayList<Integer> valueList = new ArrayList<>();

            for (Movie movie : currentUser.getLikedMovies()) {
                for (String genre : movie.getGenres()) {
                    Integer previous = treeMap.put(genre, 1);
                    if (previous != null) {
                        treeMap.put(genre, treeMap.get(genre) + previous);
                    }
                }
            }

            for (Map.Entry<String, Integer> element : treeMap.entrySet()) {
                valueList.add(element.getValue());
            }
            Collections.sort(valueList, Collections.reverseOrder());

            for (Integer value : valueList) {
                for (Map.Entry<String, Integer> element : treeMap.entrySet()) {
                    if (element.getValue().equals(value)) {
                        sortedHashMap.put(element.getKey(), value);
                    }
                }
            }

            ArrayList<Movie> userMovies = new ArrayList<>();
            userMovies.addAll(movieDatabase);

            for (Movie movie : userMovies) {
                movie.setByLikes(true);
            }

            Collections.sort(userMovies);

            for (Movie movie : userMovies) {
                movie.setByLikes(false);
            }

            ArrayList<Movie> unwatchedMovies = new ArrayList<>();
            boolean watched = false;

            for (Movie movie : userMovies) {
                watched = false;
                for (Movie watchedMovie : currentUser.getWatchedMovies()) {
                    if (movie.getName().equals(watchedMovie.getName())) {
                        watched = true;
                        break;
                    }
                }

                if (!watched) {
                    unwatchedMovies.add(movie);
                }
            }

            if (unwatchedMovies.size() == 0) {
                notification.setMovieName("No recommendation");
                return notification;
            } else {
                String recommendedMovieName = null;
                boolean existsRecommendedMovie = false;

                for (Map.Entry<String, Integer> element : sortedHashMap.entrySet()) {
                    String actualGenre = element.getKey();
                    existsRecommendedMovie = false;

                    for (Movie movie : unwatchedMovies) {
                        if (movie.getGenres().contains(actualGenre)) {
                            recommendedMovieName = movie.getName();
                            existsRecommendedMovie = true;
                            break;
                        }
                    }

                    if (existsRecommendedMovie) {
                        notification.setMovieName(recommendedMovieName);
                        break;
                    }
                }

                if (!existsRecommendedMovie) {
                    notification.setMovieName("No recommendation");
                }

                return notification;
            }
        }


    }

    /**
     * this function is executed when all the actions have been executed to give the user
     * a recommendation
     */
    public void handleEmptyActions() {
        User currentUser = getHomepageAuthentified().getCurrentUser();
        ArrayList<Notification> notificationArrayList = new ArrayList<>();

        ArrayList<Movie> movieDatabase = inputData.getMovies();
        getHomepageAuthentified().addPermittedMovies(movieDatabase, this);

        if (currentUser != null
                && currentUser.getCredentials().getAccountType().equals("premium")) {

            Notification notification = getRecommendation(getCurrentMoviesList(), currentUser);

            notificationArrayList.addAll(currentUser.getNotifications());
            notificationArrayList.add(notification);

            User updatedUser = new User.Builder(currentUser.getCredentials())
                    .tokensCount(currentUser.getTokensCount())
                    .numFreePremiumMovies(currentUser.getNumFreePremiumMovies())
                    .purchasedMovies(currentUser.getPurchasedMovies())
                    .watchedMovies(currentUser.getWatchedMovies())
                    .likedMovies(currentUser.getLikedMovies())
                    .ratedMovies(currentUser.getRatedMovies())
                    .notifications(notificationArrayList)
                    .subscribedGenres(currentUser.getSubscribedGenres())
                    .build();

            updateUser(updatedUser);

            output.addObject().putPOJO("error", null)
                    .putPOJO("currentMoviesList", null)
                    .putPOJO("currentUser", getHomepageAuthentified().getCurrentUser());

        }
    }

    /**
     * @param user a new current user
     *             <p>
     *             this method updates the current user on the pages of the platform
     */
    public void updateUser(final User user) {
        register.setCurrentUser(user);
        login.setCurrentUser(user);
        homepageAuthentified.setCurrentUser(user);
        movies.setCurrentUser(user);
        upgrades.setCurrentUser(user);
        seeDetails.setCurrentUser(user);

        inputData.getUsers().removeIf(searchUser -> searchUser.getCredentials().getName()
                .equals(user.getCredentials().getName()) && searchUser.getCredentials()
                .getPassword().equals(user.getCredentials().getPassword()));

        inputData.getUsers().add(user);

    }

    /**
     * @param movieDatabase database of movies
     * @param movieArray    specific array
     *                      updates the movies in the array if some changes
     *                      were made in the database
     */
    public void updateArrayOfMovies(final ArrayList<Movie> movieDatabase,
                                    final ArrayList<Movie> movieArray) {
        for (Movie movie : movieDatabase) {
            int index = 0;
            for (Movie searchMovie : movieArray) {
                if (searchMovie.getName().equals(movie.getName())) {
                    break;
                }
                index++;
            }

            if (movieArray.size() != 0 && index != movieArray.size()) {
                movieArray.set(index, movie);
            }
        }
    }

    /**
     * @param movieToReplace old version of the movie
     * @param updatedMovie   new version of the same movie (updated)
     *                       <p>
     *                       this method updates the movie in the "currentMovieList" and database
     *                       it's called when a change is made to the movie (ex: increasing the
     *                       number of ratings). The new movie is created with a copy
     *                       of the old version of the movie plus the updated fields and is inserted
     *                       in the arrays of movies
     */
    public void updateMovieList(final Movie movieToReplace, final Movie updatedMovie) {
        ArrayList<Movie> moviesList = getCurrentMoviesList();
        ArrayList<Movie> movieDatabase = inputData.getMovies();

        int index = 0;

        for (Movie movie : moviesList) {
            if (movie.getName().equals(movieToReplace.getName())) {
                break;
            }
            index++;
        }
        moviesList.set(index, updatedMovie);

        index = 0;

        for (Movie movie : movieDatabase) {
            if (movie.getName().equals(movieToReplace.getName())) {
                break;
            }
            index++;
        }
        movieDatabase.set(index, updatedMovie);

    }

    /**
     * @param actions array of actions given as input
     * @return true - if the movie was added in the database successfully
     * false - otherwise
     */
    public boolean databaseAdd(final ArrayList<Action> actions) {
        Movie addedMovie = actions.get(0).getAddedMovie();
        ArrayList<Movie> movieDatabase = inputData.getMovies();

        for (Movie movie : movieDatabase) {
            if (movie.getName().equals(addedMovie.getName())) {
                throwError();
                setError(null);
                return false;
            }
        }

        inputData.getMovies().add(addedMovie);
        homepageAuthentified.addPermittedMovies(movieDatabase, this);
        return true;
    }

    /**
     * @param actions array of actions given as input
     *                deletes the movie from the database
     *                deletes all the appearances of the specified movie from all the users and arrays
     *                and also notifies the users that have purchased the movie about the deletion
     */
    public void databaseDelete(final ArrayList<Action> actions) {
        String deletedMovie = actions.get(0).getDeletedMovie();
        ArrayList<Movie> movieDatabase = inputData.getMovies();
        boolean exists = false;

        for (Movie movie : movieDatabase) {
            if (movie.getName().equals(deletedMovie)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            throwError();
            setError(null);
            return;
        }

        movieDatabase.removeIf(movie -> movie.getName().equals(deletedMovie));

        ArrayList<User> newUserDatabase = new ArrayList<>();

        for (User user : getInputData().getUsers()) {
            boolean wasPurchased = false;

            ArrayList<Movie> purchasedMovies = new ArrayList<>();
            ArrayList<Movie> watchedMovies = new ArrayList<>();
            ArrayList<Movie> likedMovies = new ArrayList<>();
            ArrayList<Movie> ratedMovies = new ArrayList<>();

            purchasedMovies.addAll(user.getPurchasedMovies());
            watchedMovies.addAll(user.getWatchedMovies());
            likedMovies.addAll(user.getLikedMovies());
            ratedMovies.addAll(user.getRatedMovies());

            wasPurchased = purchasedMovies.removeIf(movie -> movie.getName().equals(deletedMovie));

            if (wasPurchased) {
                boolean wasWatched = false;
                wasWatched = watchedMovies.removeIf(movie -> movie.getName().equals(deletedMovie));

                if (wasWatched) {
                    likedMovies.removeIf(movie -> movie.getName().equals(deletedMovie));
                    ratedMovies.removeIf(movie -> movie.getName().equals(deletedMovie));
                }

                Notification notification = new Notification();
                notification.setMessage("DELETE");
                notification.setMovieName(deletedMovie);

                ArrayList<Notification> notificationArrayList = new ArrayList<>();
                notificationArrayList.addAll(user.getNotifications());
                notificationArrayList.add(notification);

                User updatedUser = new User.Builder(user.getCredentials())
                        .tokensCount(user.getTokensCount())
                        .numFreePremiumMovies(user.getNumFreePremiumMovies())
                        .purchasedMovies(purchasedMovies)
                        .watchedMovies(watchedMovies)
                        .likedMovies(likedMovies)
                        .ratedMovies(ratedMovies)
                        .notifications(notificationArrayList)
                        .subscribedGenres(user.getSubscribedGenres())
                        .build();

                if (updatedUser.getCredentials().getAccountType().equals("premium")) {
                    updatedUser.setNumFreePremiumMovies(updatedUser.getNumFreePremiumMovies() + 1);
                } else {
                    updatedUser.setTokensCount(updatedUser.getTokensCount() + 2);
                }

                newUserDatabase.add(updatedUser);
            }
        }

        for (User user : newUserDatabase) {
            if (getHomepageAuthentified().getCurrentUser().getCredentials()
                    .getName().equals(user.getCredentials().getName())
                    && getHomepageAuthentified().getCurrentUser().getCredentials()
                    .getPassword().equals(user.getCredentials().getPassword())) {

                updateUser(user);
            } else {
                inputData.getUsers().removeIf(searchUser -> searchUser.getCredentials().getName()
                        .equals(user.getCredentials().getName()) && searchUser.getCredentials()
                        .getPassword().equals(user.getCredentials().getPassword()));

                inputData.getUsers().add(user);
            }
        }

        homepageAuthentified.addPermittedMovies(movieDatabase, this);
    }

    /**
     * @param actions array of actions given as input
     *                notifies all the users when a movie is added, if the
     *                user subscribed to a genre that the movie contains
     */
    public void notifyAdd(final ArrayList<Action> actions) {
        Movie movie = actions.get(0).getAddedMovie();
        String addedMovieName = movie.getName();

        ArrayList<User> newUserDatabase = new ArrayList<>();

        ArrayList<User> userDatabase = getInputData().getUsers();
        boolean getNotified = false;

        for (User user : userDatabase) {
            getNotified = false;

            if (!movie.getCountriesBanned().contains(user.getCredentials().getCountry())) {
                for (String genre : movie.getGenres()) {
                    if (user.getSubscribedGenres().contains(genre)) {
                        getNotified = true;
                        break;
                    }
                }

                if (getNotified) {
                    Notification addNotification = new Notification();
                    addNotification.setMovieName(movie.getName());
                    addNotification.setMessage("ADD");

                    ArrayList notificationArrayList = new ArrayList<>();
                    notificationArrayList.addAll(user.getNotifications());
                    notificationArrayList.add(addNotification);

                    User updatedUser = new User.Builder(user.getCredentials())
                            .tokensCount(user.getTokensCount())
                            .numFreePremiumMovies(user.getNumFreePremiumMovies())
                            .purchasedMovies(user.getPurchasedMovies())
                            .watchedMovies(user.getWatchedMovies())
                            .likedMovies(user.getLikedMovies())
                            .ratedMovies(user.getRatedMovies())
                            .notifications(notificationArrayList)
                            .subscribedGenres(user.getSubscribedGenres())
                            .build();

                    newUserDatabase.add(updatedUser);
                }
            }
        }

        for (User user : newUserDatabase) {
            if (getHomepageAuthentified().getCurrentUser().getCredentials()
                    .getName().equals(user.getCredentials().getName())
                    && getHomepageAuthentified().getCurrentUser().getCredentials()
                    .getPassword().equals(user.getCredentials().getPassword())) {

                updateUser(user);
            } else {
                inputData.getUsers().removeIf(searchUser -> searchUser.getCredentials().getName()
                        .equals(user.getCredentials().getName()) && searchUser.getCredentials()
                        .getPassword().equals(user.getCredentials().getPassword()));

                inputData.getUsers().add(user);
            }

        }

    }

    /**
     * platform is initialized by starting interpreting actions from the HomepageNotAuthentified page
     */
    public void initPlatform() {
        homepageNotAuthentified.returnToHomepageNotAuthentified(inputData.getActions(), this);
    }

    public Input getInputData() {
        return inputData;
    }

    public HomepageNotAuthentified getHomepageNotAuthentified() {
        return homepageNotAuthentified;
    }

    public HomepageAuthentified getHomepageAuthentified() {
        return homepageAuthentified;
    }

    public Login getLogin() {
        return login;
    }

    public Logout getLogout() {
        return logout;
    }

    public Movies getMovies() {
        return movies;
    }

    public Register getRegister() {
        return register;
    }

    public SeeDetails getSeeDetails() {
        return seeDetails;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public String getError() {
        return error;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public ArrayList<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public ArrayList<Movie> getEmptyMovies() {
        return emptyMovies;
    }

    public ArrayList<String> getPagesStack() {
        return pagesStack;
    }

    public void setInputData(final Input inputData) {
        this.inputData = inputData;
    }

    public void setHomepageNotAuthentified(final HomepageNotAuthentified homepageNotAuthentified) {
        this.homepageNotAuthentified = homepageNotAuthentified;
    }

    public void setHomepageAuthentified(final HomepageAuthentified homepageAuthentified) {
        this.homepageAuthentified = homepageAuthentified;
    }

    public void setLogin(final Login login) {
        this.login = login;
    }

    public void setLogout(final Logout logout) {
        this.logout = logout;
    }

    public void setMovies(final Movies movies) {
        this.movies = movies;
    }

    public void setRegister(final Register register) {
        this.register = register;
    }

    public void setSeeDetails(final SeeDetails seeDetails) {
        this.seeDetails = seeDetails;
    }

    public void setUpgrades(final Upgrades upgrades) {
        this.upgrades = upgrades;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    public void setCurrentMoviesList(final ArrayList<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    public void setEmptyMovies(final ArrayList<Movie> emptyMovies) {
        this.emptyMovies = emptyMovies;
    }

    public void setPagesStack(final ArrayList<String> pagesStack) {
        this.pagesStack = pagesStack;
    }
}
