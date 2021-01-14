package entities;

import positions.Producer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Comparators {
    public static final class ProducerChainedComparator implements Comparator<Producer> {

        private final List<Comparator<Producer>> listComparators;

        @SafeVarargs
        public ProducerChainedComparator(final Comparator<Producer>... comparators) {
            this.listComparators = Arrays.asList(comparators);
        }

        @Override
        public int compare(final Producer emp1, final Producer emp2) {
            for (Comparator<Producer> comparator : listComparators) {
                int result = comparator.compare(emp1, emp2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

    public static final class DescendComparatorQuantity implements Comparator<Producer> {
        /**
         * comparare in functie de cantitate
         */
        public int compare(final Producer p1, final Producer p2) {
            float producer1 = p1.getEnergyPerDistributor();
            float producer2 = p2.getEnergyPerDistributor();
            return (Double.compare(producer2, producer1));
        }
    }

    public static final class AscendComparatorPrice implements Comparator<Producer> {

        /**
         * comparare in functie de pret
         */
        public int compare(final Producer p1, final Producer p2) {
            double producer1 = p1.getPriceKW();
            double producer2 = p2.getPriceKW();
            return (Double.compare(producer1, producer2));
        }
    }

    public static final class AscendSortGreen implements Comparator<Producer> {
        /**
         * comparare in functie de modul de productie (regenerabil sau nu)
         */
        public int compare(final Producer p1, final Producer p2) {
            return p1.getEnergyCode().compareTo(p2.getEnergyCode());
        }
    }

    public static final class AscendComparatorId implements Comparator<Producer> {

        /**
         * comparare in functie de Id
         */
        public int compare(final Producer p1, final Producer p2) {
            float producer1 = p1.getId();
            float producer2 = p2.getId();
            return (Double.compare(producer1, producer2));
        }
    }
}
