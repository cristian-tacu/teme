package Entities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Comparators {
    public static class AscendComparatorAverage implements Comparator<Actor> {

        public int compare(Actor a1, Actor a2) {
            double actor1 = a1.getAverage();
            double actor2 = a2.getAverage();
            return (Double.compare(actor1, actor2));
        }
    }

   public static class DescendComparatorAverage implements Comparator<Actor> {

        public int compare(Actor a1, Actor a2) {
            float actor1 = a2.getAverage();
            float actor2 = a1.getAverage();
            return (Double.compare(actor1, actor2));
        }
    }

    static class AscendComparatorAwards implements Comparator<Actor> {

        public int compare(Actor a1, Actor a2) {
            double actor1 = a1.awards.size();
            double actor2 = a2.awards.size();
            return (Double.compare(actor1, actor2));
        }
    }

    static class DescendComparatorAwards implements Comparator<Actor> {

        public int compare(Actor a1, Actor a2) {
            double actor1 = a2.awards.size();
            double actor2 = a1.awards.size();
            return (Double.compare(actor1, actor2));
        }
    }

    static class AscendSortByName implements Comparator<Actor> {
        public int compare(Actor a1, Actor a2) {
            return a1.name.compareTo(a2.name);
        }
    }

    static class DescendSortByName implements Comparator<Actor> {
        public int compare(Actor a1, Actor a2) {
            return a2.name.compareTo(a1.name);
        }
    }

    static class AscendComparatorRating implements Comparator<Video> {        // Rating

        public int compare(Video v1, Video v2) {
            double video1 = v1.totalRating;
            double video2 = v2.totalRating;
            return (Double.compare(video1, video2));
        }
    }

    static class DescendComparatorRating implements Comparator<Video> {       // Rating

        public int compare(Video v1, Video v2) {
            double video1 = v1.totalRating;
            double video2 = v2.totalRating;
            return (Double.compare(video2, video1));
        }
    }

    static class AscendComparatorFavorite implements Comparator<Video> {       // Favorite

        public int compare(Video v1, Video v2) {
            int video1 = v1.contorFavorite;
            int video2 = v2.contorFavorite;
            return (Double.compare(video1, video2));
        }
    }

    static class DescendComparatorFavorite implements Comparator<Video> {      // Favorite

        public int compare(Video v1, Video v2) {
            int video1 = v1.contorFavorite;
            int video2 = v2.contorFavorite;
            return (Double.compare(video2, video1));
        }
    }

    static class AscendComparatorLongest implements Comparator<Video> {        // Longest

        public int compare(Video v1, Video v2) {
            int video1 = v1.getTotalDuration();
            int video2 = v2.getTotalDuration();
            return (Integer.compare(video1, video2));
        }
    }

    static class DescendComparatorLongest implements Comparator<Video> {       // Longest

        public int compare(Video v1, Video v2) {
            int video1 = v1.getTotalDuration();
            int video2 = v2.getTotalDuration();
            return (Integer.compare(video2, video1));
        }
    }

    static class AscendComparatorViewed implements Comparator<Video> {

        public int compare(Video v1, Video v2) {
            double video1 = v1.contorView;
            double video2 = v2.contorView;
            return (Double.compare(video1, video2));
        }
    }

    static class DescendComparatorViewed implements Comparator<Video> {

        public int compare(Video v1, Video v2) {
            int video1 = v1.contorView;
            int video2 = v2.contorView;
            return (Double.compare(video2, video1));
        }
    }

    static class AscendComparatorUser implements Comparator<User> {

        public int compare(User u1, User u2) {
            double user1 = u1.ratingCount;
            double user2 = u2.ratingCount;
            return (Double.compare(user1, user2));
        }
    }

    static class DescendComparatorUser implements Comparator<User> {

        public int compare(User u1, User u2) {
            int user1 = u1.ratingCount;
            int user2 = u2.ratingCount;
            return (Double.compare(user2, user1));
        }
    }

    static class AscendSortByTitle implements Comparator<Video> {
        public int compare(Video v1, Video v2) {
            return v1.title.compareTo(v2.title);
        }
    }

    static class VideoChainedComparator implements Comparator<Video> {      // pentru Search

        private final List<Comparator<Video>> listComparators;

        @SafeVarargs
        public VideoChainedComparator(Comparator<Video>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        @Override
        public int compare(Video emp1, Video emp2) {
            for (Comparator<Video> comparator : listComparators) {
                int result = comparator.compare(emp1, emp2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

    static class DescendComparatorGenre implements Comparator<myGenre> {  // pentru Genre

        public int compare(myGenre g1, myGenre g2) {
            int genre1 = g1.count;
            int genre2 = g2.count;
            return (Integer.compare(genre2, genre1));
        }
    }
}
