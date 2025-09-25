package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    
    private final TicketPaymentService paymentService;
    private final SeatReservationService seatService;

    public TicketServiceImpl() {
        this.paymentService = new TicketPaymentServiceImpl();
        this.seatService    = new SeatReservationServiceImpl();
    }
    public TicketServiceImpl(TicketPaymentService paymentService,
                    SeatReservationService seatService) {
    this.paymentService = paymentService;
    this.seatService = seatService;
}

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (accountId == null) {
            throw new InvalidPurchaseException("Account id is required");
        }

        PurchaseSummary summary = summarize(ticketTypeRequests);

        validate(accountId, summary);

        pay(accountId, summary.amount());

        reserve(accountId, summary.seats());
    }

    private PurchaseSummary summarize(TicketTypeRequest... requests) {
        int adults = 0, children = 0, infants = 0;

        if (requests != null) {
            for (TicketTypeRequest r : requests) {
                if (r == null) continue;
                int n = r.getNoOfTickets();
                switch (r.getTicketType()) {
                    case ADULT  -> adults  += n;
                    case CHILD  -> children+= n;
                    case INFANT -> infants += n;
                }
            }
        }

        int seats  = adults + children;
        int amount = (adults * 25) + (children * 15);
        return new PurchaseSummary(adults, children, infants, seats, amount);
    }

    private void validate(long accountId, PurchaseSummary s) {
        final int MAX_TICKETS = 25;

        if (accountId <= 0) {
            throw new InvalidPurchaseException("Account id must be > 0");
        }

        int total = s.adults() + s.children() + s.infants();
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

    private void pay(long accountId, int amountPounds) {
        if (amountPounds > 0) {
            paymentService.makePayment(accountId, amountPounds);
        }
    }
    private void reserve(long accountId, int seats) {
        if (seats > 0) {
            seatService.reserveSeat(accountId, seats);
        }
    }

}
