package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;


public final class PurchaseCalculator {

    private PurchaseCalculator() { }

    public static PurchaseSummary summarize(TicketTypeRequest... requests) {
        int adults = 0, children = 0, infants = 0;

        if (requests != null) {
            for (TicketTypeRequest r : requests) {
                if (r == null) continue; // avoid NPE
                int n = r.getNoOfTickets();
                switch (r.getTicketType()) {
                    case ADULT  -> adults  += n;
                    case CHILD  -> children+= n;
                    case INFANT -> infants += n;
                }
            }
        }

        int seats  = adults + children;        // infants do not need seats
        int amount = adults * 25 + children * 15;
        return new PurchaseSummary(adults, children, infants, seats, amount);
    }

    public static int totalTickets(PurchaseSummary s) {
        return s.adults() + s.children() + s.infants();
    }
}
