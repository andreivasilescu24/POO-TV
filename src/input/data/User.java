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

    public static class Builder {
        private Credentials credentials;
        private int tokensCount;
        private int numFreePremiumMovies;
        private ArrayList<Movie> purchasedMovies = new ArrayList<>();
        private ArrayList<Movie> watchedMovies = new ArrayList<>();
        private ArrayList<Movie> likedMovies = new ArrayList<>();
        private ArrayList<Movie> ratedMovies = new ArrayList<>();
        private ArrayList<Notification> notifications = new ArrayList<>();
        private ArrayList<String> subscribedGenres = new ArrayList<>();

        public Builder (Credentials credentials) {
            this.credentials = new Credentials(credentials);
        }
        public Builder tokensCount(int tokensCount) {
            this.tokensCount = tokensCount;
            return this;
        }
        public Builder numFreePremiumMovies(int numFreePremiumMovies) {
            this.numFreePremiumMovies = numFreePremiumMovies;
            return this;
        }
        public Builder purchasedMovies(ArrayList<Movie> purchasedMovies) {
            this.purchasedMovies.addAll(purchasedMovies);
            return this;
        }
        public Builder watchedMovies(ArrayList<Movie> watchedMovies) {
            this.watchedMovies.addAll(watchedMovies);
            return this;
        }
        public Builder likedMovies(ArrayList<Movie> likedMovies) {
            this.likedMovies.addAll(likedMovies);
            return this;
        }
        public Builder ratedMovies(ArrayList<Movie> ratedMovies) {
            this.ratedMovies.addAll(ratedMovies);
            return this;
        }
        public Builder notifications(ArrayList<Notification> notifications) {
            this.notifications.addAll(notifications);
            return this;
        }
        public Builder subscribedGenres(ArrayList<String> subscribedGenres) {
            this.subscribedGenres.addAll(subscribedGenres);
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

    private User(Builder builder) {
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

    public User(final Credentials credentials, final int tokensCount,
                final int numFreePremiumMovies, final ArrayList<Movie> purchasedMovies,
                final ArrayList<Movie> watchedMovies, final ArrayList<Movie> likedMovies,
                final ArrayList<Movie> ratedMovies, final ArrayList<Notification> notifications,
                final ArrayList<String> subscribedGenres) {

        this.credentials = new Credentials(credentials);
        this.tokensCount = tokensCount;
        this.numFreePremiumMovies = numFreePremiumMovies;
        this.purchasedMovies.addAll(purchasedMovies);
        this.watchedMovies.addAll(watchedMovies);
        this.likedMovies.addAll(likedMovies);
        this.ratedMovies.addAll(ratedMovies);
        this.notifications.addAll(notifications);
        this.subscribedGenres.addAll(subscribedGenres);
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

    public void setSubscribedGenres(ArrayList<String> subscribedGenres) {
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

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}
