package entities;
public final class MyGenre {
    private final String type;
    private final int count;

    public MyGenre(final String type) {
        this.type = type;
        this.count = 0;
    }
    public MyGenre(final String type, final int count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}
