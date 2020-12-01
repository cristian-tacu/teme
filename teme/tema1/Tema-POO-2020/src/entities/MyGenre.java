package Entities;
public class MyGenre {
    String type;
    int count;

    public MyGenre(String type) {
        this.type = type;
        this.count = 0;
    }
    public MyGenre(String type, int count) {
        this.type = type;
        this.count = count;
    }
}
