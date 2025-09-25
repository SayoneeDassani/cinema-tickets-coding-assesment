import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.PurchaseRequestValidator;
import uk.gov.dwp.uc.pairtest.domain.PurchaseSummary;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseRequestValidatorTest {

    @Test
    void rejectsInvalidAccountId() {
        var s = new PurchaseSummary(1, 0, 0, 1, 25);
        assertThrows(InvalidPurchaseException.class,
            () -> PurchaseRequestValidator.validate(0L, s));
    }

    @Test
    void rejectsWhenNoTickets() {
        var s = new PurchaseSummary(0, 0, 0, 0, 0);
        assertThrows(InvalidPurchaseException.class,
            () -> PurchaseRequestValidator.validate(1L, s));
    }

    @Test
    void rejectsWhenTooManyTickets() {
        var s = new PurchaseSummary(26, 0, 0, 26, 650);
        assertThrows(InvalidPurchaseException.class,
            () -> PurchaseRequestValidator.validate(1L, s));
    }

    @Test
    void rejectsChildrenWithoutAdults() {
        var s = new PurchaseSummary(0, 3, 0, 3, 45);
        assertThrows(InvalidPurchaseException.class,
            () -> PurchaseRequestValidator.validate(1L, s));
    }

    @Test
    void rejectsInfantsWithoutAdults() {
        var s = new PurchaseSummary(0, 0, 2, 0, 0);
        assertThrows(InvalidPurchaseException.class,
            () -> PurchaseRequestValidator.validate(1L, s));
    }

    @Test
    void acceptsValidRequest() {
        var s = new PurchaseSummary(2, 1, 1, 3, 65);
        assertDoesNotThrow(() -> PurchaseRequestValidator.validate(123L, s));
    }
}
