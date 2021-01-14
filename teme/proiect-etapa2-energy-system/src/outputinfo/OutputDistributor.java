package outputinfo;

import dealing.Contract;
import positions.Distributor;
import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;

public final class OutputDistributor implements OutputMember {
    private int id;
    private int energyNeededKW;
    private int contractCost;
    private int budget;
    private EnergyChoiceStrategyType producerStrategy;
    private boolean isBankrupt;
    private ArrayList<Contract> contracts;

    public OutputDistributor(final Distributor distributor) {
        this.id = distributor.getId();
        this.energyNeededKW = distributor.getEnergyNeededKW();
        this.contractCost = distributor.getContractPrice();
        this.budget = distributor.getBudget();
        this.producerStrategy = distributor.getProducerStrategy();
        this.isBankrupt = distributor.getIsBankrupt();
        this.contracts = new ArrayList<>(distributor.getContracts().values());
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public ArrayList<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final ArrayList<Contract> contracts) {
        this.contracts = contracts;
    }

    public int getContractCost() {
        return contractCost;
    }

    public void setContractCost(int contractCost) {
        this.contractCost = contractCost;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(EnergyChoiceStrategyType producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }
}
