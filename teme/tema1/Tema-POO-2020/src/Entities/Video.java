package Entities;

        import java.util.ArrayList;

public abstract class Video {
    String title;
    String videoType;
    int year;
    ArrayList<String> cast;
    ArrayList<String> genres;
    ArrayList<Double> ratings;
    double totalRating;
    int totalDuration;
    int contorView = 0;
    int contorFavorite = 0;

    public Video(String title, int year, ArrayList<String> cast, ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.ratings = new ArrayList<>();
    }

    public abstract void setRating(int c, double rating, double rating2);
    public abstract double setUserRating(int c, double rating);
    public abstract int getTotalDuration();
}
