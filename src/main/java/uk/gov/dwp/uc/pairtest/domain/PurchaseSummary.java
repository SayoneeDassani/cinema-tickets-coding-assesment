package uk.gov.dwp.uc.pairtest.domain;

public final class PurchaseSummary {
    private final int adults;
    private final int children;
    private final int infants;
    private final int seats;
    private final int amount;

    public PurchaseSummary(int adults, int children, int infants, int seats, int amount) {
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.seats = seats;
        this.amount = amount;
    }

    public int adults()  { return adults; }
    public int children(){ return children; }
    public int infants() { return infants; }
    public int seats()   { return seats; }
    public int amount()  { return amount; }
}