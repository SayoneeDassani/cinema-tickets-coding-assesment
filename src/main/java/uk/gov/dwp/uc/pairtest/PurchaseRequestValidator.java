package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public final class PurchaseRequestValidator {

    private static final int MAX_TICKETS = 25;

    private PurchaseRequestValidator() { }

    public static void validate(long accountId, PurchaseSummary s) {
        if (accountId <= 0) {
            throw new InvalidPurchaseException("Account id must be > 0");
        }

        int total = PurchaseCalculator.totalTickets(s);
        if (total <= 0) {
            throw new InvalidPurchaseException("At least one ticket must be purchased");
        }
        if (total > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than " + MAX_TICKETS + " tickets");
        }
        if (s.adults() == 0 && (s.children() > 0 || s.infants() > 0)) {
            throw new InvalidPurchaseException("Child/Infant tickets require at least one Adult ticket");
        }
    }
}
