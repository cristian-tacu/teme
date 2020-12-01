package entities;

import java.util.ArrayList;

public final class Serial extends Video {
    private final int numberOfSeasons;
    private ArrayList<Season> seasons = new ArrayList<>();

    public int getVideoTotalDuration() {
        int duration = 0;
        for (Season season : seasons) {
            duration += season.getDuration();
        }
        return setTotalDuration(duration);
    }

    public Serial(final String title, final int year, final ArrayList<String> cast,
                  final ArrayList<String> genre, final int numberOfSeasons,
                  final ArrayList<entertainment.Season> seasons, final String videoType) {
        super(title, year, cast, genre);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = getSeasons(seasons);
        this.setTotalDuration(getVideoTotalDuration());
        this.setVideoType(videoType);
    }

    private Serial(final Video video) {
        super(video);
        for (Season season : ((Serial) video).seasons) {
            this.seasons.add(new Season(season));
        }
        this.numberOfSeasons = ((Serial) video).numberOfSeasons;
    }

    /**
     *
     * @return
     */
    public Video clone() {
        return new Serial(this);
    }


    private ArrayList<Season> getSeasons(
            final ArrayList<entertainment.Season> inputSeasons) {
        ArrayList<Season> mySeasons = new ArrayList<>();
        for (entertainment.Season season : inputSeasons) {
            // constructorul pentru sezon
            mySeasons.add(new Season(season.getCurrentSeason(),
                    season.getDuration(),
                    season.getRatings()));
        }
        return mySeasons;
    }

    /**
     *
     * @param season
     * @param rating
     * @return
     */
    public double setUserRating(final int season, final double rating) {

        if (seasons.get(season).getRating() == 0) {
            this.seasons.get(season).setRating(rating); // ratingul sezonului
        } else {
            return -1;
        }
        return 0;
    }

    /**
     *
     * @param seasonNumber
     * @param serialRating
     * @param seasonRating
     */
    public void setRating(final int seasonNumber, final double serialRating,
                          final double seasonRating) {
        Season mySeason = this.seasons.get(seasonNumber);
        mySeason.getSeasonRatings().add(seasonRating);

        mySeason.setGeneralRating(0);    // resetam mediile rating-urilor
        setTotalRating(0);

        for (Double aDouble : mySeason.getSeasonRatings()) { // actualizarea rating
            mySeason.setGeneralRating(mySeason.getGeneralRating() + aDouble); // pentru sezon
        }
        mySeason.setGeneralRating(mySeason.getGeneralRating()
                / mySeason.getSeasonRatings().size());
        for (Season season : seasons) {                 // actualizare rating
            setTotalRating(getTotalRating() + season.getGeneralRating());        // pentru serial
        }
        this.setTotalRating(this.getTotalRating() / seasons.size());
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
