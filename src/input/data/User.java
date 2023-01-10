package input.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public final class User {
    @JsonIgnore
    private final int startingPremiumMoviesNumber = 15;
    private Credentials credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies = new ArrayList<>();
    private ArrayList<Movie> watchedMovies = new ArrayList<>();
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<Movie> ratedMovies = new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();
    @JsonIgnore
    private ArrayList<String> subscribedGenres = new ArrayList<>();

    public static final class Builder {
        private Credentials credentials;
        private int tokensCount;
        private int numFreePremiumMovies;
        private ArrayList<Movie> purchasedMovies = new ArrayList<>();
        private ArrayList<Movie> watchedMovies = new ArrayList<>();
        private ArrayList<Movie> likedMovies = new ArrayList<>();
        private ArrayList<Movie> ratedMovies = new ArrayList<>();
        private ArrayList<Notification> notifications = new ArrayList<>();
        private ArrayList<String> subscribedGenres = new ArrayList<>();

        public Builder(final Credentials credentials) {
            this.credentials = new Credentials(credentials);
        }

        /**
         *
         * @param tokensNumber the value which will be assigned to the Builder's
         *                     "tokensCount" field
         * @return the Builder object with the "tokensCount" field set
         */
        public Builder tokensCount(final int tokensNumber) {
            tokensCount = tokensNumber;
            return this;
        }

        /**
         *
         * @param numberFreePremiumMovies the value which
         *                                will be assigned to the Builder's
         *                                numFreePremiumMovies field
         * @return the Builder object with the "numFreePremiumMovies" field set
         */
        public Builder numFreePremiumMovies(final int numberFreePremiumMovies) {
            numFreePremiumMovies = numberFreePremiumMovies;
            return this;
        }

        /**
         *
         * @param userPurchasedMovies the array of purchasedMovies which will be copied
         *                            into the "purchasedMovies" array, which is a field
         *                            of this class
         * @return the Builder object with the "purchasedMovies" field set
         */
        public Builder purchasedMovies(final ArrayList<Movie> userPurchasedMovies) {
            purchasedMovies.addAll(userPurchasedMovies);
            return this;
        }

        /**
         *
         * @param userWatchedMovies the array of watchedMovies which will be copied
         *                          into the "watchedMovies" array, which is a field
         *                          of this class
         * @return the Builder object with the "watchedMovies" field set
         */
        public Builder watchedMovies(final ArrayList<Movie> userWatchedMovies) {
            watchedMovies.addAll(userWatchedMovies);
            return this;
        }

        /**
         *
         * @param userLikedMovies the array of likedMovies which will be copied
         *                        into the "likedMovies" array, which is a field
         *                        of this class
         * @return the Builder object with the "likedMovies" field set
         */
        public Builder likedMovies(final ArrayList<Movie> userLikedMovies) {
            likedMovies.addAll(userLikedMovies);
            return this;
        }

        /**
         *
         * @param userRatedMovies the array of ratedMovies which will be copied
         *                        into the "ratedMovies" array, which is a field
         *                        of this class
         * @return the Builder object with the "ratedMovies" field set
         */
        public Builder ratedMovies(final ArrayList<Movie> userRatedMovies) {
            ratedMovies.addAll(userRatedMovies);
            return this;
        }

        /**
         *
         * @param userNotifications the array of notifications which will be copied
         *                          into the "notifications" array, which is a field
         *                          of this class
         * @return the Builder object with the "notifications" field set
         */
        public Builder notifications(final ArrayList<Notification> userNotifications) {
            notifications.addAll(userNotifications);
            return this;
        }

        /**
         *
         * @param userSubscribedGenres
         * @return
         */
        public Builder subscribedGenres(final ArrayList<String> userSubscribedGenres) {
            subscribedGenres.addAll(userSubscribedGenres);
            return this;
        }

        /**
         *
         * @return new built user based on the fields of the Builder object
         */
        public User build() {
            return new User(this);
        }

    }

    private User(final Builder builder) {
        this.credentials = builder.credentials;
        this.tokensCount = builder.tokensCount;
        this.numFreePremiumMovies = builder.numFreePremiumMovies;
        this.purchasedMovies = builder.purchasedMovies;
        this.watchedMovies = builder.watchedMovies;
        this.likedMovies = builder.likedMovies;
        this.ratedMovies = builder.ratedMovies;
        this.notifications = builder.notifications;
        this.subscribedGenres = builder.subscribedGenres;
    }

    public User() {
        numFreePremiumMovies = startingPremiumMoviesNumber;
    }

    public ArrayList<String> getSubscribedGenres() {
        return subscribedGenres;
    }

    public int getTokensCount() {
        return tokensCount;
    }

    public int getStartingPremiumMoviesNumber() {
        return startingPremiumMoviesNumber;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public void setSubscribedGenres(final ArrayList<String> subscribedGenres) {
        this.subscribedGenres = subscribedGenres;
    }

    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public void setNotifications(final ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}
