package dealing;

import java.util.ArrayList;

public final class MonthlyStat {
    private int month;
    private ArrayList<Integer> distributorsIds;

    public MonthlyStat(int month, ArrayList<Integer> oldList) {
        this.month = month;
        this.distributorsIds = new ArrayList<>(oldList);
    }

    public MonthlyStat(int month) {
        this.month = month;
        this.distributorsIds = new ArrayList<>();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<Integer> getDistributorsIds() {
        return distributorsIds;
    }

    public void setDistributorsIds(ArrayList<Integer> distributorsIds) {
        this.distributorsIds = distributorsIds;
    }
}
