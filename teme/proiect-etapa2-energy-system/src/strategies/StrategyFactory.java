package strategies;

import positions.Distributor;
import positions.Producer;

import java.util.Map;

public final class StrategyFactory {
    /**
     * creeaza o noua strategie pe baza tipului de strategie al distribuitorului
     * @param strategyType tipul strategiei
     * @param distributor distribuitorul ce va schimba producatorii
     * @param producers lista tuturor producatorilor
     * @param month luna din joc
     * @return noua strategie
     */
    public PickStrategy createStrategy(EnergyChoiceStrategyType strategyType,
                                       Distributor distributor,
                                       Map<Integer, Producer> producers, int month) {
        return switch (strategyType) {
            case GREEN -> new GreenStrategy(distributor, producers, month);
            case PRICE -> new PriceStrategy(distributor, producers, month);
            case QUANTITY -> new QuantityStrategy(distributor, producers, month);
        };
    }
}
