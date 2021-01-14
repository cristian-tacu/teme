package positions;

import dealing.MonthlyStat;

import java.util.ArrayList;

public final class Producer {
    private int id;
    private String energyType;
    private String energyCode;
    private int maxDistributors;
    private int numberOfDistributor = 0;
    private double priceKW;
    private int energyPerDistributor;
    private int oldEnergyPerDistributor;
    private ArrayList<MonthlyStat> monthlyStats = new ArrayList<>();

    public Producer() {

    }

    /**
     * se face update la energia distribuita
     * @param newEnergyPerDistributor pentru update-ul energiei
     */
    public void energyUpdate(final int newEnergyPerDistributor) {
        this.energyPerDistributor = newEnergyPerDistributor;
    }

    /**
     * seteaza daca energia este regenerabila
     * A este regenerabila, B nu
     */
    public void setEnergyCode() {
        if (this.energyType.equals("WIND")
                || this.energyType.equals("SOLAR")
                || this.energyType.equals("HYDRO")) {
            energyCode = "A";
        } else {
            energyCode = "B";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public String getEnergyCode() {
        return energyCode;
    }

    public void setEnergyCode(String energyCode) {
        this.energyCode = energyCode;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public int getNumberOfDistributor() {
        return numberOfDistributor;
    }

    public void setNumberOfDistributor(int numberOfDistributor) {
        this.numberOfDistributor = numberOfDistributor;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(double priceKW) {
        this.priceKW = priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public int getOldEnergyPerDistributor() {
        return oldEnergyPerDistributor;
    }

    public void setOldEnergyPerDistributor(int oldEnergyPerDistributor) {
        this.oldEnergyPerDistributor = oldEnergyPerDistributor;
    }

    public ArrayList<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(ArrayList<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }


    @Override
    public String toString() {
        return "Producer{"
                + "id=" + id
                + ", energyType='" + energyType + '\''
                + ", energyCode='" + energyCode + '\''
                + ", maxDistributors=" + maxDistributors
                + ", numberOfDistributor=" + numberOfDistributor
                + ", priceKW=" + priceKW
                + ", energyPerDistributor=" + energyPerDistributor
                + ", oldEnergyPerDistributor=" + oldEnergyPerDistributor
                + ", monthlyStats=" + monthlyStats
                + '}';
    }
}
