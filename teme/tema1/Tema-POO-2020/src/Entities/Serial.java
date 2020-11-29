package Entities;

import java.util.ArrayList;

public class Serial extends Video{
    public int numberOfSeasons;
    public ArrayList<Season> seasons;
    double serialRating;

    public int getTotalDuration() {
        int duration = 0;
        for(Season season : seasons) {
            duration += season.duration;
        }
        return totalDuration = duration;
    }

    public Serial(String title, int year, ArrayList<String> cast,
                 ArrayList<String> genre, int numberOfSeasons,
                  ArrayList<entertainment.Season> seasons) {
        super(title, year, cast, genre);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = getSeasons(seasons);
        this.totalDuration = getTotalDuration();
       // this.totalDuration =
    }

    public ArrayList<Season> getSeasons(ArrayList<entertainment.Season> inputSeasons) {
        ArrayList<Season> mySeasons = new ArrayList<>();
        for (entertainment.Season season : inputSeasons) {
            mySeasons.add(new Season(season.getCurrentSeason(), season.getDuration(),
                    season.getRatings()));             // constructorul pentru sezon
        }
        return mySeasons;
    }

    public double setUserRating(int season, double rating){
        if(seasons.get(season).rating == 0) {
            this.seasons.get(season).rating = rating;
        }    // ratingul sezonului
        else {
            return -1;
        }
        return 0;
    }
    public void setRating(int seasonNumber, double serialRating, double seasonRating) {
        Season mySeason = this.seasons.get(seasonNumber);
        mySeason.seasonRatings.add(seasonRating);

        mySeason.generalRating = 0;    // resetam mediile rating-urilor
        totalRating = 0;

        for (Double aDouble : mySeason.seasonRatings) { // actualizarea rating
            mySeason.generalRating += aDouble;          // pentru sezon
        }
        mySeason.generalRating /= mySeason.seasonRatings.size();
        for (Season season : seasons) {                 // actualizare rating
            totalRating += season.generalRating;        // pentru serial
        }
        this.totalRating /= seasons.size();
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.title + " " + " year= "
                + super.year+ " cast {"
                + super.cast + " }\n" + " genres {"
                + super.genres + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
