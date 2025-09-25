package uk.gov.dwp.uc.pairtest;

import java.util.Scanner;

import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

final class Main {

    private Main() {}

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        TicketService service = new TicketServiceImpl();

        System.out.println("=== Cinema Tickets CLI ===");

        long accountId = promptLong(in, "Account ID (> 0)", 123L);
        int adults     = promptInt(in, "Number of ADULT tickets", 2);
        int children   = promptInt(in, "Number of CHILD tickets", 1);
        int infants    = promptInt(in, "Number of INFANT tickets", 1);

        var reqAdult  = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, adults);
        var reqChild  = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, children);
        var reqInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, infants);

        try {
            PurchaseSummary summary = PurchaseCalculator.summarize(reqAdult, reqChild, reqInfant);

            System.out.printf("Preview → amount £%d, seats %d%n", summary.amount(), summary.seats());

            service.purchaseTickets(accountId, reqAdult, reqChild, reqInfant);

            System.out.printf("Purchase complete! Charged £%d, reserved %d seats.%n",
                    summary.amount(), summary.seats());

        } catch (InvalidPurchaseException e) {
            System.out.println("Aww snap! Purchase failed: " + e.getMessage());
        }
    }

    private static long promptLong(Scanner in, String label, long defaultVal) {
        System.out.printf("%s [%d]: ", label, defaultVal);
        String s = in.nextLine().trim();
        return s.isEmpty() ? defaultVal : Long.parseLong(s);
    }

    private static int promptInt(Scanner in, String label, int defaultVal) {
        System.out.printf("%s [%d]: ", label, defaultVal);
        String s = in.nextLine().trim();
        return s.isEmpty() ? defaultVal : Integer.parseInt(s);
    }
}
