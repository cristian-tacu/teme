package outputinfo;

import dealing.MonthlyStat;
import positions.Producer;

import java.util.ArrayList;

public final class OutputProducer implements OutputMember {
    private int id;
    private int maxDistributors;
    private double priceKW;
    private String energyType;
    private int energyPerDistributor;
    private ArrayList<MonthlyStat> monthlyStats;

    public OutputProducer(final Producer producer) {
        this.id = producer.getId();
        this.maxDistributors = producer.getMaxDistributors();
        this.priceKW = producer.getPriceKW();
        this.energyType = producer.getEnergyType();
        this.energyPerDistributor = producer.getEnergyPerDistributor();
        this.monthlyStats = producer.getMonthlyStats();
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

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
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

    public ArrayList<MonthlyStat> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(ArrayList<MonthlyStat> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }
}
