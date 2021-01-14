package outputinfo;

import positions.Consumer;

public final class OutputConsumer implements OutputMember {
    private int id;
    private boolean isBankrupt;
    private int budget;

    public OutputConsumer(final Consumer consumer) {
        this.id = consumer.getId();
        this.isBankrupt = consumer.getIsBankrupt();
        this.budget = consumer.getBudget();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(final int budget) {
        this.budget = budget;
    }
}
