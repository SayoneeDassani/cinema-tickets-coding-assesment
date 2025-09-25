import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.TicketService;
import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketPaymentService payment;
    private SeatReservationService seats;
    private TicketService service;

    @BeforeEach
    void setUp() {
        payment = mock(TicketPaymentService.class);
        seats   = mock(SeatReservationService.class);
        
        service = new TicketServiceImpl(payment, seats);
    }

    

    @Test
    void rejects_whenNoAdultWithChildren() {
        var c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        assertThrows(InvalidPurchaseException.class,
            () -> service.purchaseTickets(1L, c));
        verifyNoInteractions(payment, seats);
    }

    @Test
    void rejects_whenMoreThan25Tickets() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);
        assertThrows(InvalidPurchaseException.class,
            () -> service.purchaseTickets(1L, a));
        verifyNoInteractions(payment, seats);
    }

    @Test
    void rejects_whenAccountIsInvalid() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class,
            () -> service.purchaseTickets(0L, a));
        verifyNoInteractions(payment, seats);
    }


    @Test
    void purchase_succeeds_forAdultChildInfant() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        var c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        var i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        service.purchaseTickets(999L, a, c, i);

        verify(payment).makePayment(999L, 65); 
        verify(seats).reserveSeat(999L, 3);    
        verifyNoMoreInteractions(payment, seats);
    }

    @Test
    void purchase_succeeds_forAdultWithInfantsOnly() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        var i = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);

        service.purchaseTickets(123L, a, i);

        verify(payment).makePayment(123L, 25); 
        verify(seats).reserveSeat(123L, 1);   
        verifyNoMoreInteractions(payment, seats);
    }

    @Test
    void purchase_allowsExactly25Tickets() {
        var a = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        var c = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 15);

        assertDoesNotThrow(() -> service.purchaseTickets(42L, a, c));

        int expectedAmount = (10 * 25) + (15 * 15);
        int expectedSeats  = 25;

        verify(payment).makePayment(42L, expectedAmount);
        verify(seats).reserveSeat(42L, expectedSeats);
    }
}
