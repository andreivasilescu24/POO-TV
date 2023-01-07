package platform;

import input.data.*;

import pages.*;

import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

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

    private Platform() {

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
//        if (actions.size() == 0) {
//            User currentUser = getHomepageAuthentified().getCurrentUser();
//            ArrayList<Notification> notificationArrayList = new ArrayList<>();
//
//            if (endNotification == 0 && currentUser != null && currentUser.getCredentials().getAccountType().equals("premium")) {
//                if (currentUser.getLikedMovies().size() == 0) {
//                    Notification notification = new Notification();
//                    notification.setMovieName("No recommendation");
//                    notification.setMessage("Recommendation");
//
//                    notificationArrayList.addAll(currentUser.getNotifications());
//                    notificationArrayList.add(notification);
//                }
//
//                User updatedUser = new User(currentUser.getCredentials(),
//                        currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
//                        currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
//                        currentUser.getLikedMovies(), currentUser.getRatedMovies(), notificationArrayList);
//
//                updateUser(updatedUser);
//
//                output.addObject().putPOJO("error", null)
//                        .putPOJO("currentMoviesList", null)
//                        .putPOJO("currentUser", getHomepageAuthentified().getCurrentUser());
//
//                endNotification = 1;
//            }
//            return;
//        }

        ArrayList<Action> actionsAux = new ArrayList<>();
        actionsAux.addAll(actions);
        actionsAux.remove(0);
        actions.clear();
        actions.addAll(actionsAux);
    }

    public void handleEmptyActions() {
        User currentUser = getHomepageAuthentified().getCurrentUser();
        ArrayList<Notification> notificationArrayList = new ArrayList<>();

        if (currentUser != null && currentUser.getCredentials().getAccountType().equals("premium")) {

            if (currentUser.getLikedMovies().size() == 0) {
                Notification notification = new Notification();
                notification.setMovieName("No recommendation");
                notification.setMessage("Recommendation");

                notificationArrayList.addAll(currentUser.getNotifications());
                notificationArrayList.add(notification);

            }


            User updatedUser = new User(currentUser.getCredentials(),
                    currentUser.getTokensCount(), currentUser.getNumFreePremiumMovies(),
                    currentUser.getPurchasedMovies(), currentUser.getWatchedMovies(),
                    currentUser.getLikedMovies(), currentUser.getRatedMovies(),
                    notificationArrayList, currentUser.getSubscribedGenres());

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

    public void updateArrayOfMovies(final ArrayList<Movie> movieDatabase, final ArrayList<Movie> movieArray) {
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

    public boolean databaseAdd(ArrayList<Action> actions) {
        Movie addedMovie = actions.get(0).getAddedMovie();
        ArrayList<Movie> movieDatabase = inputData.getMovies();

        for (Movie movie : movieDatabase) {
            if (movie.getName().equals(addedMovie.getName())) {
                throwError();
                return false;
            }
        }

        inputData.getMovies().add(addedMovie);
        homepageAuthentified.addPermittedMovies(movieDatabase, this);
        return true;
    }

    public void databaseDelete(ArrayList<Action> actions) {
        String deletedMovie = actions.get(0).getDeletedMovie();
        ArrayList<Movie> movieDatabase = inputData.getMovies();
        boolean exists = false;

        for (Movie movie : movieDatabase) {
            if (movie.getName().equals(deletedMovie)) {
                exists = true;
                break;
            }
        }

        if (exists == false) {
            throwError();
            return;
        }

        movieDatabase.removeIf(movie -> movie.getName().equals(deletedMovie));
        homepageAuthentified.addPermittedMovies(movieDatabase, this);
    }

    public void notifyAdd(ArrayList<Action> actions) {
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

                    User updatedUser = new User(user.getCredentials(),
                            user.getTokensCount(), user.getNumFreePremiumMovies(),
                            user.getPurchasedMovies(), user.getWatchedMovies(),
                            user.getLikedMovies(), user.getRatedMovies(),
                            notificationArrayList, user.getSubscribedGenres());

                    if (getHomepageAuthentified().getCurrentUser().getCredentials()
                            .getName().equals(updatedUser.getCredentials().getName())
                            && getHomepageAuthentified().getCurrentUser().getCredentials()
                            .getPassword().equals(updatedUser.getCredentials().getPassword())) {

                        updateUser(updatedUser);
                    }

                    newUserDatabase.add(updatedUser);
                }
            }
        }

        for (User user : newUserDatabase) {
            inputData.getUsers().removeIf(searchUser -> searchUser.getCredentials().getName()
                    .equals(user.getCredentials().getName()) && searchUser.getCredentials()
                    .getPassword().equals(user.getCredentials().getPassword()));

            inputData.getUsers().add(user);
        }

    }

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

    public void setPagesStack(ArrayList<String> pagesStack) {
        this.pagesStack = pagesStack;
    }
}
