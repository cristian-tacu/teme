package outputinfo;

import positions.Consumer;
import positions.Distributor;
import positions.Producer;

public class OutputFactory {
    /**
     * creaza un consumer sau distribuitor
     * @param listType tipul instantei
     * @param consumer consumatorul folosit in constructor
     * @param distributor distribuitorul folosit in constructor
     */
    public OutputMember createMember(final String listType,
                                     final Consumer consumer,
                                     final Distributor distributor,
                                     final Producer producer) {
        switch (listType) {
            case "consumer":
                return new OutputConsumer(consumer);
            case "distributor":
                return new OutputDistributor(distributor);
            case "producer":
                return new OutputProducer(producer);
            default:
                throw new IllegalStateException("Unexpected value: " + listType);
        }
    }
}
