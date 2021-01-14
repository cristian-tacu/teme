package dealing;

import positions.Distributor;
import positions.Producer;
import strategies.PickStrategy;
import strategies.StrategyFactory;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public final class EnergyDealer implements Observer {
    private int month;
    private Distributor distributor;
    private Map<Integer, Producer> producers;
    private StrategyFactory generator = new StrategyFactory();

    public EnergyDealer(Distributor distributor, Map<Integer,
            Producer> producers, int month) {
        this.distributor = distributor;
        this.producers = producers;
        this.month = month;
    }

    @Override
    public void update(Observable o, Object arg) {
        PickStrategy pickStrategy = getGenerator().createStrategy(
                distributor.getProducerStrategy(), distributor, producers, month);
        pickStrategy.pickBestProducers();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Map<Integer, Producer> getProducers() {
        return producers;
    }

    public void setProducers(Map<Integer, Producer> producers) {
        this.producers = producers;
    }

    public StrategyFactory getGenerator() {
        return generator;
    }
}
