package positions;

public final class Consumer {
    private int id;
    private int initialBudget;
    private int budget;
    private int monthlyIncome;
    private boolean isBankrupt;
    private boolean restant;
    private int distributorId = -1;
    private int oldDistributorId = -1;
    private int due;
    private int oldDue;
    private int contractLenght = 0;

    public Consumer() {
    }

    @Override
    public String toString() {
        return "Consumer{"
                + "id=" + id
                + ", budget=" + budget
                + ", distributorId=" + distributorId
                + ", due=" + due
                + ", contractLenght=" + contractLenght
                + ", bankrupt=" + isBankrupt
                + ", restant=" + restant

                + '}';
    }

    public Consumer(final int id, final int budget, final int monthlyIncome) {
        this.id = id;
        this.initialBudget = budget;
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
        this.isBankrupt = false;
        this.restant = false;
    }

    /**
     * setarea bugetului
     */
    public void setInitialBudget(final int initialBudget) {
        this.budget = initialBudget;
        this.initialBudget = initialBudget;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public boolean isRestant() {
        return restant;
    }

    public void setRestant(final boolean restant) {
        this.restant = restant;
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

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getInitialBudget() {
        return initialBudget;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(final int distributorId) {
        this.distributorId = distributorId;
    }

    public int getOldDistributorId() {
        return oldDistributorId;
    }

    public void setOldDistributorId(final int oldDistributorId) {
        this.oldDistributorId = oldDistributorId;
    }

    public int getDue() {
        return due;
    }

    public void setDue(final int due) {
        this.due = due;
    }

    public int getOldDue() {
        return oldDue;
    }

    public void setOldDue(final int oldDue) {
        this.oldDue = oldDue;
    }

    public int getContractLenght() {
        return contractLenght;
    }

    public void setContractLenght(final int contractLenght) {
        this.contractLenght = contractLenght;
    }
}
