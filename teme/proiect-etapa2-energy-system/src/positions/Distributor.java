package positions;

import dealing.Contract;
import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Distributor {
    private int id;
    private int contractLength;
    private int budget;
    private int infrastructureCost;
    private int productionCost;
    private int initialBudget;
    private int initialInfrastructureCost;
    private boolean isBankrupt;
    private int contractPrice;
    private Map<Integer, Contract> contracts = new LinkedHashMap<>();
    private final double percentage = 0.2;
    private int energyNeededKW;
    private EnergyChoiceStrategyType producerStrategy;
    private ArrayList<Producer> producers = new ArrayList<>();


    public Distributor() {

    }

    public Distributor(final int id, final int contractLenght, final int budget,
                       final int infrastructureCost, final int productionCost) {
        this.id = id;
        this.contractLength = contractLenght;
        this.initialBudget = budget;
        this.budget = budget;
        this.initialInfrastructureCost = infrastructureCost;
        this.infrastructureCost = infrastructureCost;
        this.productionCost = productionCost;
        this.isBankrupt = false;
    }

    /**
     * se face update la costuri
     */
    public void costsUpdate(final int newInfrastructureCost) {
        this.infrastructureCost = newInfrastructureCost;
    }

    /**
     * se calculeaza costul de productie
     */
    public void productionCostUpdate() {
        double sum = 0;
        final double ten = 10;
        for (Producer producer : producers) {
            sum += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }
        productionCost = (int) Math.round(Math.floor(sum / ten));
    }

    /**
     * calcularea pretului contractului
     */
    public void getPriceOfContract() {
        productionCostUpdate();  // de VERIFICAT
        if (this.contracts.size() != 0) {
            contractPrice = (int) Math.round(
                    Math.floor((double) this.infrastructureCost / this.contracts.size())
                    + this.productionCost + extractProfite());
        } else {
            contractPrice = this.infrastructureCost
                    + this.productionCost + extractProfite();
        }
    }

    /**
     * calcularea profitului
     */
    public int extractProfite() {
        return (int) Math.round(Math.floor(percentage * productionCost));
    }

    /**
     * bugetul = costuri + profit
     */
    public void budgetUpdate() {
        this.budget -= (infrastructureCost
                + productionCost * contracts.size());
    }

    /**
     * adaugare contract
     */
    public void insertContract(final int consumerId) {
        contracts.put(consumerId, new Contract(consumerId, contractPrice, contractLength));
    }



    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getContractPrice() {
        return contractPrice;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setIsBankrupt(final boolean bankrupt) {
        this.isBankrupt = bankrupt;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    /**
     */
    public void setInitialBudget(final int initialBudget) {
        this.budget = initialBudget;
        this.initialBudget = initialBudget;
    }

    public int getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    /**
     */
    public void setInitialInfrastructureCost(final int initialInfrastructureCost) {
        this.infrastructureCost = initialInfrastructureCost;
        this.initialInfrastructureCost = initialInfrastructureCost;
    }

    public Map<Integer, Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final Map<Integer, Contract> contracts) {
        this.contracts = contracts;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public ArrayList<Producer> getProducers() {
        return producers;
    }

    public void setProducers(ArrayList<Producer> producers) {
        this.producers = producers;
    }

    @Override
    public String toString() {
        return "Distributor{"
                + "id=" + id
                + ", energyNeeded=" + energyNeededKW
                + ", budget=" + budget
                + '}';
    }
}
