import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import inputinfo.Data;
import outputinfo.OutputConsumer;
import outputinfo.OutputData;
import outputinfo.OutputDistributor;
import outputinfo.OutputFactory;
import outputinfo.OutputProducer;
import positions.Consumer;
import positions.Distributor;
import positions.Producer;

import java.io.File;
import java.util.ArrayList;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        System.out.println(args[0]);

        final ObjectMapper objectMappper = new ObjectMapper();
        //Data data = Data.getInstance();
        //Data data = objectMappper.readValue(new File(args[0]), data.getClass());
        final Data data = objectMappper.readValue(new File(args[0]), Data.class);

        final BaseCenter baseCenter = BaseCenter.getInstance();
        baseCenter.constructor(data);

        baseCenter.firstRound();
        baseCenter.theGame();

        // construirea listelor de output pe baza celor rezolvate
        final ArrayList<OutputConsumer> consumers = new ArrayList<>();
        final ArrayList<OutputDistributor> distributors = new ArrayList<>();
        final ArrayList<OutputProducer> producers = new ArrayList<>();

        final OutputFactory generator = new OutputFactory();

        for (final Consumer consumer : baseCenter.getConsumers().values()) {
            consumers.add((OutputConsumer) generator.createMember(
                    "consumer", consumer, null, null));
        }
        for (final Distributor distributor : baseCenter.getDistributors().values()) {
            distributors.add((OutputDistributor) generator.createMember(
                    "distributor", null, distributor, null));
        }
        for (final Producer producer : baseCenter.getProducers().values()) {
            producers.add((OutputProducer) generator.createMember(
                    "producer", null, null, producer));
        }

        final ObjectWriter writer = objectMappper.writer(new DefaultPrettyPrinter());
        final OutputData outputData = new OutputData(consumers, distributors, producers);
        final File outputFile = new File(args[1]);
        writer.writeValue(outputFile, outputData);
    }
}
