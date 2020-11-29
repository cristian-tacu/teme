package Entities;

import java.util.ArrayList;

public class Movie extends Video{
    double rating;

    public Movie(String title, int year, ArrayList<String> cast,
                 ArrayList<String> genre, int duration) {
        super(title, year, cast, genre);
        this.totalDuration = duration;
    }

    public void setRating(int redundant, double rating, double redundant1) {
        this.ratings.add(rating);
        for (Double aDouble : ratings) {
            this.totalRating += aDouble;
        }
        this.totalRating /= ratings.size();
    }
    public double setUserRating(int c, double rating) {
        if (this.rating == 0) {
            this.rating = rating;
        }
        return this.rating;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.title + "year= "
                + super.year + "duration= "
                + totalDuration + "cast {"
                + super.cast + " }\n"
                + "genres {" + super.genres + " }\n ";
    }
}
