package entities;

import fileio.*;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Action {
    private final ArrayList<Video> videos;
    private final ArrayList<Actor> actors;
    private final ArrayList<User> users;
    private final Writer fileWriter;
    private final JSONArray output;

    public Action(final Input input, final Writer fileWriter, final JSONArray output) {
        this.videos = constructVideoArray(input.getMovies(), input.getSerials());
        this.actors = constructActorsArray(input.getActors());
        this.users = constructUsersArray(input.getUsers());
        this.setInitialFavoriteCount();
        this.fileWriter = fileWriter;
        this.output = output;
    }

    // creaza pe baza MovieInput si SerialInput campul videos
    private ArrayList<Video> constructVideoArray(final List<MovieInputData> movieInput,
                                                 final List<SerialInputData> serialInput) {
        ArrayList<Video> myVideos = new ArrayList<>();
        for (MovieInputData movie : movieInput) {            // adaugarea movies
            myVideos.add(new Movie(movie.getTitle(), movie.getYear(),
                    movie.getCast(), movie.getGenres(),
                    movie.getDuration(), "movies"));
        }
        for (SerialInputData serial : serialInput) {            // adaugarea serials
            myVideos.add(new Serial(serial.getTitle(), serial.getYear(),
                    serial.getCast(), serial.getGenres(),
                    serial.getNumberSeason(), serial.getSeasons(),
                    "shows"));
        }
        return myVideos;
    }
    // construieste lista de videouri din filmografia unui autor
    // se afla aici deoarece are nevoie de lista de videouri
    private ArrayList<Video> constructFilmography(
            final ArrayList<String> inputFilmography) {
        ArrayList<Video> actorVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if (inputFilmography.contains(video.getTitle())) {
                actorVideos.add(video);
            }
        }
        return actorVideos;
    }
    // construieste array-ul de actori pe baza celui din input
    private ArrayList<Actor> constructActorsArray(final List<ActorInputData> actorInput) {
        ArrayList<Actor> myActors = new ArrayList<>();
        for (ActorInputData actor : actorInput) {
            myActors.add(new Actor(actor.getName(),
                    actor.getCareerDescription(),
                    constructFilmography(actor.getFilmography()),
                    actor.getAwards()));
        }
        return myActors;
    }

    // construieste lista de videouri din lista de favorite data
    // se afla aici deoarece are nevoie de lista de videouri
    private ArrayList<Video> constructFavorite(
            final ArrayList<String> inputFavorite) {
        ArrayList<Video> favoriteVideos = new ArrayList<>();
        for (Video video : videos) {    // cauta titlul in filmography
            if (inputFavorite.contains(video.getTitle())) {
                favoriteVideos.add(video);
            }
        }
        return favoriteVideos;
    }

    // construieste array-ul de useri pe baza celui din input
    private ArrayList<User> constructUsersArray(final List<UserInputData> usersInput) {
        ArrayList<User> myUsers = new ArrayList<>();
        for (UserInputData user : usersInput) {
            myUsers.add(new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), constructFavorite(user.getFavoriteMovies()),
                    this.videos));
            int currentIndex = myUsers.size() - 1;   // se actualizeaza si viewed
            myUsers.get(currentIndex).setUserView(myUsers.get(currentIndex).getHistory());
            // actualizam si in lista generala de videos, numarul de view
            for (String title : myUsers.get(currentIndex).getHistory().keySet()) {
                for (Video video : this.videos) {   // pentru fiecare video din general
                    if (video.getTitle().equals(title)) {  // creste cu numarul din history
                        video.setContorView(video.getContorView()
                                + myUsers.get(currentIndex).getHistory().get(title));
                        break;
                    }
                }
            }
        }
        return myUsers;
    }

    // calculeaza de cate ori e favorit pentru toti utilizatorii
    private void setInitialFavoriteCount() {
        for (User user : this.users) {      //sa nu razi ca vezi 3 for :))
            for (Video favorVideo : user.getFavoriteVideos()) {
                for (Video video : this.videos) {
                    if (video.getTitle().equals(favorVideo.getTitle())) {
                        // cate persoane au acel video favorit
                        video.setContorFavorite(video.getContorFavorite() + 1);
                        break;
                    }
                }
            }
        }
    }
    private User getUser(final String userName) {
        User myUser;
        for (User user : this.users) {
            if (userName.equals(user.getUsername())) {
                myUser = user;
                return myUser;
            }
        }
        return null;
    }
    private void write(final int id, final String title, final String finish, final String text) {
        try {
           this.output.add(fileWriter.writeFile(id, finish
                    + title + text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printer(final int number, final ArrayList<Video> myVideos, final int id) {
        StringBuilder list = new StringBuilder("Query result: [");
        for (int i = 0; i < number - 1; i++) {
            list.append(myVideos.get(i).getTitle()).append(", ");
        }
        if (number == 0) {         // daca lista e goala
            list.append("]");
        } else {
            list.append(myVideos.get(number - 1).getTitle()).append("]");
        }
        write(id, "", list.toString(), "");
    }

    // commands implement
    // ---------------------setFavorite----------------------

    /**
     *
     * @param title
     * @param userName
     * @param id
     */
    public void setFavorite(final String title, final String userName, final int id) {
        User myUser = this.getUser(userName);
        for (Video video : myUser.getVideos()) {    // cauta in videourile userului
            if (video.getTitle().equals(title)) { // cauta dupa titlu
                if (myUser.getViewedVideos().containsKey(video.getTitle())) { // daca e vazut
                    if (!myUser.getFavoriteVideos().contains(video)) {
                        myUser.getFavoriteVideos().add(video);
                        this.write(id, title, "success -> ", " was added as favourite");
                        break;
                    } else {  // eroare, e deja; return
                        this.write(id, title, "error -> ", " is already in favourite list");
                        return;
                    }
                } else { // nu l-a vazut
                    this.write(id, title, "error -> ", " is not seen");
                    return;
                }
            }
        }
        for (Video video : videos) {
            if (video.getTitle().equals(title)) {
                // cate persoane au acel video favorit
                video.setContorFavorite(video.getContorFavorite() + 1);
                return;
            }
        }

    }

    // ---------------------setView----------------------

    /**
     *
     * @param title
     * @param userName
     * @param id
     */
    public void setView(final String title, final String userName, final int id) {
        User myUser = this.getUser(userName);
        for (Video video : myUser.getVideos()) {
            if (video.getTitle().equals(title)) { // cauta dupa titlu
                video.setContorView(video.getContorView() + 1);        // de cate ori a vazut user
                myUser.getViewedVideos().put(video.getTitle(), video);
                this.write(id, title, "success -> ",
                        " was viewed with total views of " + video.getContorView());
                break;
            }
        }
        for (Video video : videos) {     // actualizez si in videos (general)
            if (video.getTitle().equals(title)) {
                video.setContorView(video.getContorView() + 1); // cate persoane au acel video vazut
                return;
            }
        }
    }

    // ---------------------setRating----------------------

    /**
     *
     * @param title
     * @param rating
     * @param season
     * @param userName
     * @param id
     */
    public void setRating(final String title, final double rating,
                          int season, final String userName, final int id) {
        season--;
        User myUser = this.getUser(userName);
        for (Video video : myUser.getVideos()) { // cauta dupa titlu in lista lui user
            if (video.getTitle().equals(title)) {
                if (myUser.getViewedVideos().containsKey(video.getTitle())) { // daca e vazut
                    double userRating = video.setUserRating(season, rating); // ratingul userului
                    if (userRating == -1) {
                        // rating a fost dat deja
                        this.write(id, title, "error -> ", " has been already rated");
                        return;
                    }
                    this.write(id, title, "success -> ", " was rated with "
                            + rating + " by " + myUser.getUsername());
                    // incrementeaza numarul de ratings dat
                    myUser.setRatingCount(myUser.getRatingCount() + 1);
                    for (Video generalVideo : videos) { // cauta dupa titlu in lista generala
                        if (generalVideo.getTitle().equals(title)) {
                            generalVideo.setRating(season, userRating, rating);
                        } // actualizeaza ratingul general
                    }
                } else { // nu l-a vazut
                    this.write(id, title, "error -> ", " is not seen");
                }
            }
        }
    }

    // actor queries
    // ---------------------Average----------------------

    /**
     *
     * @param number
     * @param type
     * @param id
     */
    public void average(int number, final String type, final int id) {
        ArrayList<Actor> myActors = new ArrayList<>(this.actors);
        myActors.removeIf(v -> v.getAverage() == 0);
        if (type.equals("asc")) { // sorteaza dupa media ratingurilor
            myActors.sort(new Comparators.ActorChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorAverage(),  // rating
                    new Comparators.AscendSortByName()));     // titlu
        } else {                  // descrescator
            myActors.sort(new Comparators.ActorChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorAverage(),  // rating
                    new Comparators.DescendSortByName()));
        }
        StringBuilder actorsList = new StringBuilder("Query result: [");
        number = Math.min(number, myActors.size());
        for (int i = 0; i < number - 1; i++) {
            actorsList.append(myActors.get(i).getName()).append(", ");
        }
        if (number == 0) {    // daca lista e goala
            actorsList.append("]");
        } else {
            actorsList.append(myActors.get(number - 1).getName()).append("]");
        }
        write(id, "", actorsList.toString(), "");
    }

    // ---------------------Awards----------------------

    /**
     *
     * @param awards
     * @param type
     * @param id
     * @return
     */
    public ArrayList<Actor> awards(final List<String> awards, final String type, final int id) {
        boolean ok;
        ArrayList<Actor> awardActors = new ArrayList<>(); // cream un array
        for (Actor actor : actors) {
            ok = false;
            for (String award : awards) {
                if (!actor.getAwards().containsKey(award)) {
                    ok = true;
                    break; // daca unul din awards j nu se gaseste in actorul i
                }
            }
            if (ok) {
                continue;
            }
            awardActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            awardActors.sort(new Comparators.ActorChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorAwards(),  // rating
                    new Comparators.AscendSortByName())); // crescator
        } else {
            awardActors.sort(new Comparators.ActorChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorAwards(),  // rating
                    new Comparators.DescendSortByName())); // descrescator
        }
        StringBuilder awardsList = new StringBuilder("Query result: [");
        for (int i = 0; i < awardActors.size() - 1; i++) {
            awardsList.append(awardActors.get(i).getName()).append(", ");
        }
        if (awardActors.size() == 0) { // daca lista e goala
            awardsList.append("]");
        } else {
            awardsList.append(awardActors.get(awardActors.size() - 1).getName()).append("]");
        }
        write(id, "", awardsList.toString(), "");
        return awardActors;
    }

    // ---------------------FilterDiscription----------------------

    /**
     *
     * @param keywords
     * @param type
     * @param id
     */
    public void filterDescription(final List<String> keywords, final String type, final int id) {
        boolean ok;
        ArrayList<Actor> keyActors = new ArrayList<>(); // cream un array
        for (Actor actor : actors) {
            ok = false; // resetam verificatorul
            for (String keyword : keywords) {
                String regex = "(?s)(?i)(.*)\\b" + keyword + "\\b(.*)";
                if (!actor.getCareerDescription().matches(regex)) {
                    ok = true; // pentru a iesi din for-uri
                    break; // daca unul din keywords j nu se gaseste in actorul i
                }
            }
            if (ok) {
                continue; // daca nu are un word trece mai departe
            }
            keyActors.add(actor); // daca nu se opreste inseamna ca exista toate
        }
        if (type.equals("asc")) {
            keyActors.sort(new Comparators.AscendSortByName()); // crescator
        } else {
            keyActors.sort(new Comparators.DescendSortByName()); // descrescator
        }
        StringBuilder keyList = new StringBuilder("Query result: [");
        for (int i = 0; i < keyActors.size() - 1; i++) {
            keyList.append(keyActors.get(i).getName()).append(", ");
        }
        if (keyActors.size() == 0) {    // daca lista e goala
            keyList.append("]");
        } else {
            keyList.append(keyActors.get(keyActors.size() - 1).getName()).append("]");
        }
        write(id, "", keyList.toString(), "");
    }

    // ------------------------video queries--------------------------
    // ---------------------Rating----------------------

    /**
     *
     * @param type
     * @param year
     * @param number
     * @param genre
     * @param videotype
     * @param id
     */
    public void rating(final String type, final int year, int number,
                       final String genre, final String videotype,
                       final int id) {
        ArrayList<Video> ratingVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {     // daca e film
            if (video.getVideoType().equals(videotype)
                    && video.getGenres().contains(genre)
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.getYear() == year)
                    && video.getTotalRating() != 0) {     // cuprinde acel gen
                ratingVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            ratingVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorRating(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            ratingVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorRating(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, ratingVideos.size());
        printer(number, ratingVideos, id);
    }

    // ---------------------Favorite----------------------

    /**
     *
     * @param type
     * @param year
     * @param number
     * @param genre
     * @param videoType
     * @param id
     */
    public void favorite(final String type, final int year, int number,
                         final String genre, final String videoType, final int id) {
        ArrayList<Video> favoriteVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {           // pentru fiecare video
            if (video.getVideoType().equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.getGenres().contains(genre))     // cuprinde acel gen
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.getYear() == year)
                    && video.getContorFavorite() != 0) {     // e la favorite o data
                favoriteVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            favoriteVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorFavorite(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            favoriteVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorFavorite(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, favoriteVideos.size());
        printer(number, favoriteVideos, id);
    }

    // ---------------------Longest----------------------

    /**
     *
     * @param type
     * @param year
     * @param number
     * @param genre
     * @param videoType
     * @param id
     */
    public void longest(final String type, final int year, int number,
                        final String genre, final String videoType, final int id) {
        ArrayList<Video> longestVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) {
            if (video.getVideoType().equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.getGenres().contains(genre))
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.getYear() == year)) {
                longestVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            longestVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorLongest(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            longestVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorLongest(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, longestVideos.size());
        printer(number, longestVideos, id);
    }

    // ---------------------MostViewed----------------------

    /**
     *
     * @param type
     * @param year
     * @param number
     * @param genre
     * @param videoType
     * @param id
     */
    public void mostViewed(final String type, final int year, int number,
                           final String genre, final String videoType, final int id) {
        ArrayList<Video> mostViewedVideos = new ArrayList<>(); // cream un array
        for (Video video : videos) { // pentru fiecare video
            // daca e din acel an, are acel gen, movie/serial (objectType)
            if (video.getVideoType().equals(videoType)
                    // daca genre e null nu se ia in seama la criteriu
                    && (genre == null || video.getGenres().contains(genre))
                    // daca anul e null nu se ia in seama la criteriu
                    && (year == 0 || video.getYear() == year)
                    && video.getContorView() != 0) { // daca e vazut
                mostViewedVideos.add(video); // si e din acel an => adauga in lista
            }
        }
        if (type.equals("asc")) {
            // comparare dupa 2 criterii
            mostViewedVideos.sort(new Comparators.VideoChainedComparator(
                    new Comparators.AscendComparatorViewed(),  // rating
                    new Comparators.AscendSortByTitle())); // crescator
        } else {
            // comparare dupa 2 criterii
            mostViewedVideos.sort(new Comparators.VideoChainedComparator(
                    new Comparators.DescendComparatorViewed(),  // rating
                    new Comparators.DescendSortByTitle())); // descrescator
        }
        number = Math.min(number, mostViewedVideos.size());
        printer(number, mostViewedVideos, id);

    }

    // user query
    // ---------------------NumberOfRating----------------------

    /**
     *
     * @param type
     * @param number
     * @param id
     */
    public void numberOfRating(final String type, int number, final int id) {
        ArrayList<User> ratingUsers = new ArrayList<>(this.users); // cream un array
        for (User user : this.users) {
            ratingUsers.add(new User(user));
        }
        ratingUsers.removeIf(v -> v.getRatingCount() == 0);
        if (type.equals("asc")) {
            ratingUsers.sort(new Comparators.UserChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorUser(),  // rating
                    new Comparators.AscendSortByUserName())); // crescator
        } else {
            ratingUsers.sort(new Comparators.UserChainedComparator(// comparare dupa 2 criterii
                    new Comparators.DescendComparatorUser(),  // rating
                    new Comparators.DescendSortByUserName())); // descrescator
        }
        StringBuilder viewedList = new StringBuilder("Query result: [");
        number = Math.min(number, ratingUsers.size());
        for (int i = 0; i < number - 1; i++) {
            viewedList.append(ratingUsers.get(i).getUsername()).append(", ");
        }
        if (number == 0) {             // daca lista e goala
            viewedList.append("]");
        } else {
            viewedList.append(ratingUsers.get(number - 1).getUsername()).append("]");
        }
        write(id, "", viewedList.toString(), "");
    }

    // premium recomandation
    //------------------PremiumFavorite------------------

    /**
     *
     * @param userName
     * @param id
     */
    public void premiumFavorite(final String userName, final int id) {
        User user = this.getUser(userName);
        if (user.getSubscriptionType().equals("PREMIUM")) {  // daca e premium
            ArrayList<Video> favoriteVideo = new ArrayList<>(); // cream un array
            for (Video userVideo : user.getVideos()) {
                if (userVideo.getContorView() == 0) { // daca nu e vazut
                    for (Video video : videos) { // il ia din lista generala
                        if (video.getTitle().equals(userVideo.getTitle())) {
                            favoriteVideo.add(video);
                        }
                    }
                }
            }
            favoriteVideo.sort(new Comparators.DescendComparatorFavorite()); // descrescator
            if (favoriteVideo.size() != 0) {
                write(id, "", "FavoriteRecommendation result: ", favoriteVideo.get(0).getTitle());
            } else {
                write(id, "", "FavoriteRecommendation cannot be applied!", "");
            }
            return;
        } else {
            write(id, "", "FavoriteRecommendation cannot be applied!", "");
        }
        return;
    }
    // -----------------PremiumSearch------------------

    /**
     *
     * @param userName
     * @param genre
     * @param id
     */
    public void premiumSearch(final String userName, final String genre, final int id) {
        User user = this.getUser(userName);
        if (user.getSubscriptionType().equals("PREMIUM")) {
            ArrayList<Video> unseenVideos = new ArrayList<>(); // cream un array
            for (Video userVideo : user.getVideos()) {  // se cauta in viewed fiecare video
                if (userVideo.getContorView() == 0 // daca nu e vazut
                        && userVideo.getGenres().contains(genre)) { // si are acel gen
                    for (Video video : videos) { // il ia din lista generala
                        if (userVideo.getTitle().equals(video.getTitle())) {
                            unseenVideos.add(video);
                        }
                    }
                }
            }
            unseenVideos.sort(new Comparators.VideoChainedComparator(// comparare dupa 2 criterii
                    new Comparators.AscendComparatorRating(),  // rating
                    new Comparators.AscendSortByTitle()));     // titlu
            StringBuilder searchList = new StringBuilder("SearchRecommendation result: [");
            for (int i = 0; i < unseenVideos.size() - 1; i++) {
                searchList.append(unseenVideos.get(i).getTitle()).append(", ");
            }
            if (unseenVideos.size() == 0) {   // daca lista e goala
                write(id, "", "SearchRecommendation cannot be applied!", "");
                return;
            } else {
                searchList.append(unseenVideos.get(unseenVideos.size() - 1).getTitle()).append("]");
            }
            write(id, "", searchList.toString(), "");
        } else {
            write(id, "", "SearchRecommendation cannot be applied!", "");
        }
    }
    // -----------------PremiumPopular------------------

    /**
     *
     * @param userName
     * @param id
     */
    public void premiumPopular(final String userName, final int id) {
        User user = this.getUser(userName);
        if (user.getSubscriptionType().equals("PREMIUM")) {
            Map<String, Integer> genresMap = user.genreListConstruction(); // lista de genuri
            for (Video video : videos) { // cauta in toate videourile
                for (String eachGenre : video.getGenres()) {
                    if (genresMap.containsKey(eachGenre)) {  // pentru acel gen
                        // creste numarul genului cu numarul de vizualizari ale videoului
                        genresMap.put(eachGenre, genresMap.get(eachGenre) + video.getContorView());
                    }
                }
            }
            // cream o lista pentru a sorta in functie de countGenre
            ArrayList<MyGenre> genreList = new ArrayList<>();
            for (String key : genresMap.keySet()) {
                MyGenre genreElement = new MyGenre(key, genresMap.get(key));
                genreList.add(genreElement);
            }
            genreList.sort(new Comparators.DescendComparatorGenre());   // sortarea descrescatoare

            for (MyGenre genre : genreList) {              // pentru fiecare gen
                for (Video video : user.getVideos()) {              // pentru fiecare video
                    // daca nu e vazut si e de acel gen
                    if (video.getContorView() == 0              // daca nu e vazut
                            && video.getGenres().contains(genre.getType())) { // si are genul
                        write(id, "", "PopularRecommendation result: ", video.getTitle());
                        return;
                    }
                }
            }
            write(id, "", "PopularRecommendation cannot be applied!", "");
        } else {
            write(id, "", "PopularRecommendation cannot be applied!", "");
        }
    }
    // -----------------Standard------------------

    /**
     *
     * @param userName
     * @param id
     */
    public void standard(final String userName, final int id) {
        User user = this.getUser(userName);
        for (Video userVideo : user.getVideos()) {  // se cauta in user.videos
            if (userVideo.getContorView() == 0) { // daca nu e vazut
                write(id, "", "StandardRecommendation result: ", userVideo.getTitle());
                return;
            }
        }
        write(id, "", "StandardRecommendation cannot be applied!", "");
    }
    // -----------------BestUnseen------------------

    /**
     *
     * @param userName
     * @param id
     */
    public void bestUnseen(final String userName, final int id) {
        User user = this.getUser(userName);
        ArrayList<Video> bestUnseenVideos = new ArrayList<>(); // cream un array
        for (Video userVideo : user.getVideos()) {  // se cauta in user.videos fiecare video
            if (userVideo.getContorView() == 0) { // daca nu e vazut
                for (Video video : videos) { // il ia din lista generala
                    if (userVideo.getTitle().equals(video.getTitle())) {
                        bestUnseenVideos.add(video);
                    }
                }
            }
        }
        bestUnseenVideos.sort(new Comparators.DescendComparatorRating());
        if (bestUnseenVideos.size() != 0) {
            write(id, "", "BestRatedUnseenRecommendation result: ",
                    bestUnseenVideos.get(0).getTitle());
        } else {
            write(id, "", "BestRatedUnseenRecommendation cannot be applied!", "");
        }
    }

}
