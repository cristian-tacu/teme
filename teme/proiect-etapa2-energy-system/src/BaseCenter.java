import dealing.Contract;
import dealing.EnergyDealer;
import dealing.MonthlyStat;
import inputinfo.Data;
import inputinfo.DistributorChange;
import inputinfo.MonthlyUpdate;
import inputinfo.ProducerChange;
import positions.Consumer;
import positions.Distributor;
import positions.Producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public final class BaseCenter extends Observable {
    private static BaseCenter baseCenter;
    private int numberOfTurns;
    private Map<Integer, Consumer> consumers;
    private Map<Integer, Distributor> distributors;
    private Map<Integer, Producer> producers;
    private ArrayList<MonthlyUpdate> monthlyUpdates;
    private Distributor bestOption;

    private BaseCenter() {

    }

    /**
     * construirea lui baseCenter
     */
    public void constructor(final Data data) {
        consumers = consumerMapConstructor(data.getInitialData().getConsumers());
        distributors = distributorMapConstructor(data.getInitialData().getDistributors());
        producers = producerMapConstructor(data.getInitialData().getProducers());
        monthlyUpdates = data.getMonthlyUpdates();
        numberOfTurns = data.getNumberOfTurns();
    }

    /**
     * pentru singleton
     */
    public static BaseCenter getInstance() {
        if (baseCenter == null) {
            baseCenter = new BaseCenter();
        }

        return baseCenter;
    }

    public  int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public Map<Integer, Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(final Map<Integer, Consumer> consumers) {
        this.consumers = consumers;
    }

    public Map<Integer, Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final Map<Integer, Distributor> distributors) {
        this.distributors = distributors;
    }

    public Map<Integer, Producer> getProducers() {
        return producers;
    }

    public void setProducers(Map<Integer, Producer> producers) {
        this.producers = producers;
    }

    public ArrayList<MonthlyUpdate> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final ArrayList<MonthlyUpdate> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }

    public Distributor getBestOption() {
        return bestOption;
    }

    public void setBestOption(final Distributor bestOption) {
        this.bestOption = bestOption;
    }

    /**
     * construirea mapei de consumatori pe baza listei
     * @param consumersList lista de consumatori initiali
     * @return noua mapa de consumatori
     */
    public static Map<Integer, Consumer> consumerMapConstructor(
            final ArrayList<Consumer> consumersList) {
        final Map<Integer, Consumer> consumerMap = new HashMap<>();
        for (final Consumer consumer : consumersList) {
            consumerMap.put(consumer.getId(), consumer);
        }
        return consumerMap;
    }

    /**
     * construirea mapei de distribuitori
     * @param distributorList lista de distribuitori initiali
     * @return noua mapa de distribuitori
     */
    public static Map<Integer, Distributor> distributorMapConstructor(
            final ArrayList<Distributor> distributorList) {
        final Map<Integer, Distributor> distributorMap = new HashMap<>();
        for (final Distributor distributor : distributorList) {
            distributorMap.put(distributor.getId(), distributor);
        }
        return distributorMap;
    }

    /**
     * construirea mapei de producatori
     * @param producerList lista de producatori initiali
     * @return noua mapa de producatori
     */
    public static Map<Integer, Producer> producerMapConstructor(
            final ArrayList<Producer> producerList) {
        final Map<Integer, Producer> producerMap = new HashMap<>();
        for (final Producer producer : producerList) {
            producerMap.put(producer.getId(), producer);
        }
        return producerMap;
    }

    /**
     * cauta cel mai ieftin distribuitor
     */
    public void getBestOffert() {
        Distributor bestDistributor = null;
        for (final Distributor distributor : distributors.values()) {
            if (!distributor.getIsBankrupt()) {    // doar daca nu e eliminat
                if (bestDistributor == null
                        || (distributor.getContractPrice() < bestDistributor.getContractPrice())) {
                    bestDistributor = distributor;
                }
            }
        }
        bestOption = bestDistributor;
    }

    /**
     * seteaza pentru fiecare producator tipul de energie (regenerabil A, sau nu B)
     * seteaza pentru runda initiala vechea cantitate oferita (egala cu cea actuala)
     */
    public void producerEnergyCode() {
        for (Producer producer : producers.values()) {
            producer.setEnergyCode();
            // in prima luna, vechia si actuala cantitate de energie oferita sunt la fel
            producer.setOldEnergyPerDistributor(producer.getEnergyPerDistributor());
        }
    }

    /**
     * alegerea contractelor si actualizarea facturilor de luna trecuta
     */
    public void contractPick() {
        for (final Consumer consumer : consumers.values()) {
            // daca a terminat contractul sau a falimentat distribuitorul
            if ((consumer.getContractLenght() == 0
                    || distributors.get(consumer.getDistributorId()).getIsBankrupt())
                    && !consumer.getIsBankrupt()) {
                //int id = consumer.getDistributorId();
                // actualizam vechiul distribuitor (id, eventuala datorie)
                consumer.setOldDistributorId(consumer.getDistributorId());
                consumer.setOldDue(consumer.getDue());
                // setam informatiile despre noul contract (id, plata si durata)
                consumer.setDistributorId(bestOption.getId());
                consumer.setDue(bestOption.getContractPrice());
                consumer.setContractLenght(bestOption.getContractLength());
                // adaugam contractul
                distributors.get(bestOption.getId()).insertContract(consumer.getId());

                // daca nu se schimba distribuitorul
            } else if (consumer.getContractLenght() != 0
                    && !consumer.getIsBankrupt()) {
                // actualizam informatiile despre distribuitorul de luna trecuta
                consumer.setOldDistributorId(consumer.getDistributorId());
                consumer.setOldDue(consumer.getDue());
            }
        }
    }

    /**
     * calcularea fatcturilor distribuitorilor
     */
    public void contractCost() {
        for (final Distributor distributor : distributors.values()) {
            distributor.getPriceOfContract();
        }
    }

    /**
     * primirea salariului de catre consumatori
     */
    public void getConsumersVenue() {
        for (final Consumer consumer : consumers.values()) {
            if (!consumer.getIsBankrupt()) {
                consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            }
        }
    }

    /**
     * adauga noii consumatori
     * @param newConsumers este array-ul de noi consumatori din input
     */
    public void insertNewConsumers(final ArrayList<Consumer> newConsumers) {
        for (final Consumer newConsumer : newConsumers) {
            consumers.put(newConsumer.getId(), newConsumer);
        }
    }

    /**
     * actualizarea costurilor
     * @param distributorChanges este array-ul de schimbari al costurilor din input
     */
    public void changeDistributorsCost(final ArrayList<DistributorChange> distributorChanges) {
        for (final DistributorChange distributorChange : distributorChanges) {
            final int infCost = distributorChange.getInfrastructureCost();
            distributors.get(distributorChange.getId()).costsUpdate(infCost);
        }
    }

    /**
     * actualizarea energiei distribuite de producatori
     * @param producerChanges este lista de schimbari pentru energia distribuita
     */
    public void changeProducerDistribution(final ArrayList<ProducerChange> producerChanges) {
        for (ProducerChange producerChange : producerChanges) {
            Producer producer = producers.get(producerChange.getId());
            final int energyDistributed = producerChange.getEnergyPerDistributor();
            // actualizeaza actuala energie distribuita cu cea nou primita
            producer.energyUpdate(energyDistributed);
        }
    }

    /**
     * creeaza baza de date a distribuitorilor pe baza month (monthlyStats)
     * @param month luna la care s-a ajuns
     */
    public void newMonthlyStatsForProducers(int month) {
        for (Producer producer : producers.values()) {
            ArrayList<MonthlyStat> monthlyStat = producer.getMonthlyStats();
            // actualizeaza vechea energie distrtibuita
            producer.setOldEnergyPerDistributor(producer.getEnergyPerDistributor());
                // in prima luna listele producatorilor sunt goale
            if (month == 0) {
                monthlyStat.add(new MonthlyStat(month));

                // se copiaza listele producatorilor din luna precedenta
            } else {
                monthlyStat.add(new MonthlyStat(
                        month, monthlyStat.get(month - 1).getDistributorsIds()));
            }
        }
    }

    /**
     * alegerea producatorilor in prima luna
     * @param month luna 0
     */
    public void firstRoundProdcerPick(int month) {
        for (Distributor distributor : distributors.values()) {
            addObserver(new EnergyDealer(distributor, producers, month));
            setChanged();
            notifyObservers();
            deleteObservers();
        }
    }

    /**
     * alegerea producatorilor de catre distribuitori
     */
    public void producerPick(int month) {
        // se verifica la fiecare distribuitor daca vreun producator a schimbat energia oferita
        for (Distributor distributor : distributors.values()) {
            if (!distributor.getIsBankrupt()) {
                for (Producer producer : distributor.getProducers()) {
                    if (producer.getEnergyPerDistributor()
                            != producer.getOldEnergyPerDistributor()) {
                        removeDistributorFromProducer(distributor, month);
                        addObserver(new EnergyDealer(distributor, producers, month));
                        setChanged();
                        notifyObservers();
                        deleteObservers();
                        break;
                    }
                }
            }
        }
    }

    /**
     * se elimina distribuitorii din listele producatorilor ce schimba energia
     * se elibereaza total lista de producatori a acelui distribuitor
     * @param distributor -> distribuitorul ce schimba producatorii
     * @param month -> luna actuala
     */
    public void removeDistributorFromProducer(Distributor distributor, int month) {
        for (Producer producer : distributor.getProducers()) {
            // se ia fiecare producator din lista distribuitorului si se elimina
            // id-ul distribuitorului din listele producatorului (relatie many to many)
            ArrayList<Integer> distributorList
                    = producer.getMonthlyStats().get(month).getDistributorsIds();
            distributorList.remove((Object) distributor.getId());
            producer.setNumberOfDistributor(producer.getNumberOfDistributor() - 1);
        }
        distributor.getProducers().clear(); // sterg producatorii din lista distribuitorului
    }

    /**
     * excluderea contractului unui consumator de la un anumit distribuitor
     * @param consumer consumatorul
     * @param distributorId id-ul distribuitorului consumatorului
     * @param oldDistributorId distribuitorul de luna trecuta
     */
    public void excludeFromContract(final Consumer consumer, final int distributorId,
                                    final int oldDistributorId) {
        final Distributor distributor = distributors.get(distributorId);
        final Distributor oldDistributor = distributors.get(oldDistributorId);
        // daca l-a schimbat intre timp, amandoi elimina consumatorul
        if (distributorId != oldDistributorId && oldDistributor != null) {
            oldDistributor.getContracts().remove(consumer.getId());
        }
        // daca e acelasi distribuitor, elimina doar de la acela
        distributor.getContracts().remove(consumer.getId());
    }

    /**
     * platirea costurilor de catre distribuitori
     */
    public void distributorPayment() {
        for (final Distributor distributor : distributors.values()) {
            if (!distributor.getIsBankrupt()) {
                distributor.budgetUpdate();
            }
            if (distributor.getBudget() < 0) {
                distributor.setIsBankrupt(true);
            }
        }
    }

    /**
     * platirea facturii si intrarea banilor in contul distribuitorului
     */
    public void billPayment() {
        for (final Consumer consumer : consumers.values()) {
            final int budget = consumer.getBudget();
            final int due = consumer.getDue();
            final int oldDue = consumer.getOldDue();
            // distribuitorul platit
            final Distributor distributor = distributors.get(consumer.getDistributorId());
            final Distributor oldDistributor = distributors.get(consumer.getOldDistributorId());
            final double debt = 1.2;
            final var bill = Math.round(Math.floor(debt * oldDue));

                // daca are bani (se aduna si la distribuitor)
            if (budget >= due && !consumer.isRestant()) {
                consumer.setBudget(budget - due);
                distributor.setBudget(distributor.getBudget() + due);

                // daca are bani pentru factura curenta si vechiul distribuitor a falimentat
            } else if (budget >= due && oldDistributor.getIsBankrupt()
                    && !consumer.getIsBankrupt()) {
                consumer.setBudget(budget - due);
                distributor.setBudget(distributor.getBudget() + due);

                // daca are bani si e restant si nu schimba distributorul
            } else if (budget >= (due + bill) && consumer.isRestant()
                    && oldDistributor.equals(distributor)
                    && !consumer.getIsBankrupt()) {
                    // i se scad banii din buget
                consumer.setBudget(budget - (int) bill - due);
                    // se aduna factura veche la vechiul distribuitor
                oldDistributor.setBudget(distributor.getBudget() + (int) bill);
                    // se aduna factura curenta la distribuitorul curent
                distributor.setBudget(oldDistributor.getBudget() + due);
                consumer.setRestant(false);

                // daca are bani, e restant si a schimbat distributorul
            } else if (budget >= bill && consumer.isRestant()
                    && oldDistributor != distributor
                    && !consumer.getIsBankrupt()) {
                // i se scad banii din buget (doar datoria)
                consumer.setBudget(budget - (int) bill);
                // se aduna factura veche la vechiul distribuitor
                oldDistributor.setBudget(distributor.getBudget() + (int) bill);

                // daca n-are bani ramane dator
            } else if (budget < due && !consumer.isRestant()) {
                consumer.setRestant(true);

                // daca e dator si a doua oara => faliment
            } else if (budget < due + bill) {
                consumer.setBankrupt(true);
            }
            // a mai trecut o luna (se scade o luna de la distribuitor si consumator)
            consumer.setContractLenght(consumer.getContractLenght() - 1);
            final Contract contract = distributor.getContracts().get(consumer.getId());
            if (contract != null) { // daca nu a fost scos
                contract.setRemainedContractMonths(contract.getRemainedContractMonths() - 1);
            }
        }
    }

    /**
     * eliminarea consumatorilor falimentati
     */
    public void removeBankruptConsumers() {
        for (final Consumer consumer : consumers.values()) {
            if (consumer.getIsBankrupt()
                    && consumer.getDistributorId() >= 0) {
                excludeFromContract(consumer, consumer.getDistributorId(),
                        consumer.getOldDistributorId());
            }
        }
    }

    /**
     * eliminarea contractelor terminate
     */
    public void removeFinishContract() {
        for (final Consumer consumer : consumers.values()) {
            if (consumer.getContractLenght() == 0
                    && consumer.getDistributorId() >= 0) {
                excludeFromContract(consumer, consumer.getDistributorId(),
                        consumer.getOldDistributorId());
            }
        }
    }

    /**
     * Se elimina datele despre distribuitori din runda initiala, a producatorilor
     * Se creaza montlyStats si pentru prima luna, cu scopul de a avea pentru
     * prima luna din joc datele pentru alegerea producatorilor
     */
    public void removeExtraMonthlyStat() {
        for (Producer producer : producers.values()) {
            producer.getMonthlyStats().remove(0);
        }
    }

    /**
     * se realizeaza prima runda
     */
    public void firstRound() {
        producerEnergyCode();

        newMonthlyStatsForProducers(0);
        firstRoundProdcerPick(0);

        contractCost();
        getBestOffert();
        getConsumersVenue();
        contractPick();
        billPayment();
        distributorPayment();
    }

    /**
     * parcurgerea lunilor
     */
    public void theGame() {
        for (int i = 0; i < numberOfTurns; i++) {
            final ArrayList<Consumer> newGameConsumers
                    = monthlyUpdates.get(i).getNewConsumers();
            final ArrayList<DistributorChange> costChanges
                    = monthlyUpdates.get(i).getDistributorChanges();
            final ArrayList<ProducerChange> energyChanges
                    = monthlyUpdates.get(i).getProducerChanges();

            insertNewConsumers(newGameConsumers);
            changeDistributorsCost(costChanges);
            removeBankruptConsumers();
            contractCost();
            removeFinishContract();
            getBestOffert();
            getConsumersVenue();
            contractPick();
            billPayment();
            distributorPayment();

            newMonthlyStatsForProducers(i + 1);
            changeProducerDistribution(energyChanges);
            producerPick(i + 1);
        }
        removeBankruptConsumers();
        removeExtraMonthlyStat();

    }

}
