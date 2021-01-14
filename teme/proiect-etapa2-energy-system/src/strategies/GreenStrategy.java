package strategies;

import entities.Comparators;
import positions.Distributor;
import positions.Producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class GreenStrategy implements PickStrategy {

    private Distributor distributor;
    private Map<Integer, Producer> producers;
    private int month;

    GreenStrategy(Distributor distributor, Map<Integer, Producer> producers, int month) {
        this.distributor = distributor;
        this.producers = producers;
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * construieste o lista pe baza mapei
     * @return lista de producatori
     */
    public ArrayList<Producer> listGenerate() {
        Collection<Producer> values = producers.values();
        return new ArrayList<>(values);
    }

    @Override
    public void pickBestProducers() {
        int sum = 0;
        ArrayList<Producer> producersList = listGenerate();
        // se sorteaza dupa Green, Price, Quntity si ID
        producersList.sort(new Comparators.ProducerChainedComparator(
                new Comparators.AscendSortGreen(),
                new Comparators.AscendComparatorPrice(),
                new Comparators.DescendComparatorQuantity(),
                new Comparators.AscendComparatorId()));

        for (Producer producer : producersList) {
            // daca are cata energie ii trebuie
            if (sum >= distributor.getEnergyNeededKW()) {
                break;
            }
            if (producer.getNumberOfDistributor() < producer.getMaxDistributors()) {
                sum += producer.getEnergyPerDistributor();
                // se adauga in lista de distribuitori a producatorului (monthlyStats)
                // si in lista de producatori a distribuitorului
                Producer mapProducer = producers.get(producer.getId());
                mapProducer.getMonthlyStats().get(month)
                        .getDistributorsIds().add(distributor.getId());
                Collections.sort(mapProducer.getMonthlyStats().get(month).getDistributorsIds());

                distributor.getProducers().add(producer);
                producer.setNumberOfDistributor(producer.getNumberOfDistributor() + 1);
            }
        }
    }
}
