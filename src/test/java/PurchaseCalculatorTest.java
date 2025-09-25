import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.PurchaseCalculator;
import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseCalculatorTest {

    @Test
    void summarizesCountsSeatsAndAmount() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        var c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        var i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        PurchaseSummary s = PurchaseCalculator.summarize(a, c, i);

        assertEquals(2, s.adults());
        assertEquals(1, s.children());
        assertEquals(1, s.infants());
        assertEquals(3, s.seats());
        assertEquals(65, s.amount()); 
    }

    @Test
    void handlesEmptyRequestsSafely() {
        PurchaseSummary s = PurchaseCalculator.summarize();
        assertEquals(0, s.adults());
        assertEquals(0, s.children());
        assertEquals(0, s.infants());
        assertEquals(0, s.seats());
        assertEquals(0, s.amount());
    }
}
