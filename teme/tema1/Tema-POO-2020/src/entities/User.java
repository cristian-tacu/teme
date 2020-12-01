package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public final class User {
    private int ratingCount = 0;
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final Map<String, Video> viewedVideos;
    private ArrayList<Video> favoriteVideos = new ArrayList<>();
    private final ArrayList<Video> videos = new ArrayList<>();

    public User(final String username, final String subscriptionType, final Map<String,
                Integer> history, final ArrayList<Video> favoriteVideos,
                final ArrayList<Video> videos) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.setFavoriteVideos(favoriteVideos);
        this.viewedVideos = new HashMap<>();
        for (Video video : videos) {
            assert false;
            this.getVideos().add(video.clone());
        }
    }

    public User(final User user) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.setFavoriteVideos(user.getFavoriteVideos());
        this.viewedVideos = user.getViewedVideos();
        for (Video video : user.getVideos()) {
            assert false;
            this.getVideos().add(video.clone());
        }
    }

    // constructia mapei de genuri din array
    private Map<String, Integer> setGenreMap(final ArrayList<String> list) {
        Map<String, Integer> genreMap = new HashMap<>();
        for (String genreString : list) {
            //myGenre newGenre = new myGenre(genreString);
            genreMap.put(genreString, 0);
        }
        return genreMap;
    }

    /**
     *
     * @return
     */
    public Map<String, Integer> genreListConstruction() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("TV Movie",
                "Drama", "Fantasy", "Comedy", "Family", "War", "Sci-Fi & Fantasy",
                "Crime", "Animation", "Science Fiction", "Action", "Horror",
                "Mystery", "Western", "Adventure", "Action & Adventure", "Romance",
                "Thriller", "Kids", "History"));
        return setGenreMap(list);
    }

    // punem din history in viewVideos si
    // actualizam contorView al filmului pwntru user

    /**
     *
     * @param map
     */
    public void setUserView(final Map<String, Integer> map) {
        for (String title : map.keySet()) {
            for (Video video : this.getVideos()) {
                if (video.getTitle().equals(title)) {         // cauta dupa titlu
                    video.setContorView(video.getContorView() // de cate ori a vazut user
                            + map.get(title));
                    this.getViewedVideos().put(video.getTitle(), video); // adaugam in viewed
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "UserInputData{" + "username='"
                + getUsername() + '\'' + ", subscriptionType='"
                + getSubscriptionType() + '\'' + ", history="
                + getHistory() + ", favoriteMovies="
                + getFavoriteVideos() + '}';
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(final int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public Map<String, Video> getViewedVideos() {
        return viewedVideos;
    }

    public ArrayList<Video> getFavoriteVideos() {
        return favoriteVideos;
    }

    public void setFavoriteVideos(final ArrayList<Video> favoriteVideos) {
        this.favoriteVideos = favoriteVideos;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }
}
