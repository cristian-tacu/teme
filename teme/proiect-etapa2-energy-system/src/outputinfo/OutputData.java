package outputinfo;

import java.util.ArrayList;

public final class OutputData {
    private ArrayList<OutputConsumer> consumers;
    private ArrayList<OutputDistributor> distributors;
    private ArrayList<OutputProducer> energyProducers;

    public OutputData(final ArrayList<OutputConsumer> consumers,
                      final ArrayList<OutputDistributor> distributors,
                      final ArrayList<OutputProducer> energyProducers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.energyProducers = energyProducers;
    }

    public ArrayList<OutputConsumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(final ArrayList<OutputConsumer> consumers) {
        this.consumers = consumers;
    }

    public ArrayList<OutputDistributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final ArrayList<OutputDistributor> distributors) {
        this.distributors = distributors;
    }

    public ArrayList<OutputProducer> getEnergyProducers() {
        return energyProducers;
    }

    public void setEnergyProducers(ArrayList<OutputProducer> energyProducers) {
        this.energyProducers = energyProducers;
    }
}
