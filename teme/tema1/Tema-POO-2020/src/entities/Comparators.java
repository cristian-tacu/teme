package entities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class Comparators {
    public static final class AscendComparatorAverage implements Comparator<Actor> {

        public int compare(final Actor a1, final Actor a2) {  // pentru query AVERAGE
            double actor1 = a1.getAverage();
            double actor2 = a2.getAverage();
            return (Double.compare(actor1, actor2));
        }
    }

    static final class ActorChainedComparator implements Comparator<Actor> {   // pentru Actori

        private final List<Comparator<Actor>> listComparators;

        @SafeVarargs
        ActorChainedComparator(final Comparator<Actor>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        @Override
        public int compare(final Actor emp1, final Actor emp2) {
            for (Comparator<Actor> comparator : listComparators) {
                int result = comparator.compare(emp1, emp2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

   public static final class DescendComparatorAverage implements Comparator<Actor> {

        public int compare(final Actor a1, final Actor a2) {   // pentru query AVERAGE
            float actor1 = a2.getAverage();
            float actor2 = a1.getAverage();
            return (Double.compare(actor1, actor2));
        }
    }

    static final class AscendComparatorAwards implements Comparator<Actor> {

        public int compare(final Actor a1, final Actor a2) { // pentru query AWARDS
            double actor1 = a1.getAwardsCount();
            double actor2 = a2.getAwardsCount();
            return (Double.compare(actor1, actor2));
        }
    }

    static final class DescendComparatorAwards implements Comparator<Actor> {

        public int compare(final Actor a1, final Actor a2) {  // pentru query AWARDS
            double actor1 = a2.getAwardsCount();
            double actor2 = a1.getAwardsCount();
            return (Double.compare(actor1, actor2));
        }
    }
    // alfabetic dupa actori
    static final class AscendSortByName implements Comparator<Actor> {
        public int compare(final Actor a1, final Actor a2) {
            return a1.getName().compareTo(a2.getName());
        }
    }
    // invers alfabetic dupa actori
    static final class DescendSortByName implements Comparator<Actor> {
        public int compare(final Actor a1, final Actor a2) {
            return a2.getName().compareTo(a1.getName());
        }
    }

    static final class AscendComparatorRating implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query RATING
            double video1 = v1.getTotalRating();
            double video2 = v2.getTotalRating();
            return (Double.compare(video1, video2));
        }
    }

    static final class DescendComparatorRating implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query RATING
            double video1 = v1.getTotalRating();
            double video2 = v2.getTotalRating();
            return (Double.compare(video2, video1));
        }
    }

    static final class AscendComparatorFavorite implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query FAVORITE
            int video1 = v1.getContorFavorite();
            int video2 = v2.getContorFavorite();
            return (Double.compare(video1, video2));
        }
    }

    static final class DescendComparatorFavorite implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query FAVORITE
            int video1 = v1.getContorFavorite();
            int video2 = v2.getContorFavorite();
            return (Double.compare(video2, video1));
        }
    }

    static final class AscendComparatorLongest implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query LONGEST
            int video1 = v1.getVideoTotalDuration();
            int video2 = v2.getVideoTotalDuration();
            return (Integer.compare(video1, video2));
        }
    }

    static final class DescendComparatorLongest implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query LONGEST
            int video1 = v1.getVideoTotalDuration();
            int video2 = v2.getVideoTotalDuration();
            return (Integer.compare(video2, video1));
        }
    }

    static final class AscendComparatorViewed implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query MOST_VIEWED
            double video1 = v1.getContorView();
            double video2 = v2.getContorView();
            return (Double.compare(video1, video2));
        }
    }

    static final class DescendComparatorViewed implements Comparator<Video> {

        public int compare(final Video v1, final Video v2) { // pentru query MOST_VIEWED
            int video1 = v1.getContorView();
            int video2 = v2.getContorView();
            return (Double.compare(video2, video1));
        }
    }
    // dupa numarul de rating dat crescator
    static final class AscendComparatorUser implements Comparator<User> {

        public int compare(final User u1, final User u2) { // pentru USER query
            double user1 = u1.getRatingCount();
            double user2 = u2.getRatingCount();
            return (Double.compare(user1, user2));
        }
    }
    // dupa numarul de rating dat descrescator
    static final class DescendComparatorUser implements Comparator<User> {

        public int compare(final User u1, final User u2) { // pentru USER query
            int user1 = u1.getRatingCount();
            int user2 = u2.getRatingCount();
            return (Double.compare(user2, user1));
        }
    }
    // pentru sortarea Users cu mai multe criterii
    static final class UserChainedComparator implements Comparator<User> {

        private final List<Comparator<User>> listComparators;

        @SafeVarargs
        UserChainedComparator(final Comparator<User>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        @Override
        public int compare(final User emp1, final User emp2) {
            for (Comparator<User> comparator : listComparators) {
                int result = comparator.compare(emp1, emp2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }
    // USER alfabetic dupa numele user-ului
    static final class AscendSortByUserName implements Comparator<User> {
        public int compare(final User a1, final User a2) {
            return a1.getUsername().compareTo(a2.getUsername());
        }
    }
    // USER invers alfabetic dupa numele user-ului
    static final class DescendSortByUserName implements Comparator<User> {
        public int compare(final User a1, final User a2) {
            return a2.getUsername().compareTo(a1.getUsername());
        }
    }

    // VIDEO alfabetic dupa titlu
    static final class AscendSortByTitle implements Comparator<Video> {
        public int compare(final Video v1, final Video v2) {
            return v1.getTitle().compareTo(v2.getTitle());
        }
    }
    // VIDEO invers alfabetic dupa titlu
    static final class DescendSortByTitle implements Comparator<Video> {
        public int compare(final Video v1, final Video v2) {
            return v2.getTitle().compareTo(v1.getTitle());
        }
    }
    // pentru sortarea Videos dupa mai multe criterii
    static final class VideoChainedComparator implements Comparator<Video> {

        private final List<Comparator<Video>> listComparators;

        @SafeVarargs
        VideoChainedComparator(final Comparator<Video>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        @Override
        public int compare(final Video emp1, final Video emp2) {
            for (Comparator<Video> comparator : listComparators) {
                int result = comparator.compare(emp1, emp2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }
    // pentru Genre in functie de popularitatea genului
    static final class DescendComparatorGenre implements Comparator<MyGenre> {

        public int compare(final MyGenre g1, final MyGenre g2) {
            int genre1 = g1.getCount();
            int genre2 = g2.getCount();
            return (Integer.compare(genre2, genre1));
        }
    }
}
