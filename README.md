# cinema-tickets-coding-assesment

## 1. Introduction  

The task was to implement the `TicketService` so that ticket purchases follow the specified business rules for Adult, Child, and Infant tickets. Invalid requests should be rejected, while valid ones must trigger the provided payment and seat reservation services.  

I structured the code to keep the logic clean, testable, and easy to extend. To make it easier to try out, I also added a simple console runner (`Main.java`) that allows the user to enter ticket numbers interactively.

---

## 2. Project Structure

**Main code (`src/main/java/uk/gov/dwp/uc/pairtest/`):**
- **TicketService** – given interface (unchanged).  
- **TicketServiceImpl** – my implementation; only one public method (`purchaseTickets`).  
- **PurchaseCalculator** – calculates totals (adults, children, infants, cost, seats).  
- **PurchaseRequestValidator** – enforces the business rules (e.g. max 25 tickets sold , adult required with children).  
- **PurchaseSummary** – immutable record carrying the computed totals.  
- **InvalidPurchaseException** – thrown when rules are broken.  
- **Main.java** – a small console program for interactive testing.  

**Tests (`src/test/java/`):**
- **PurchaseCalculatorTest** – tests the maths (amounts and seats).  
- **PurchaseRequestValidatorTest** – validates business rules.  
- **TicketServiceImplTest** – interaction tests with Mockito to confirm correct payment and seat reservation calls.

---

## 3. Business Use Case / Rules

- **Ticket Types & Prices**  
  - Adult = £25  
  - Child = £15  
  - Infant = £0 (no seat, sits on Adult’s lap)  

- **Rules**  
  - At least one Adult is required if Children or Infants are present.  
  - Maximum of 25 tickets per transaction.  
  - Account ID must be greater than zero.  
  - Payment is for Adults + Children only.  
  - Seats are reserved for Adults + Children only.  

Any invalid request results in an `InvalidPurchaseException`.

---

## 4. How to Run

You can test the system in two ways:

- **Interactive mode (`Main.java`)**  
  Run `Main.java` from your IDE (VS Code, IntelliJ, Eclipse).  
  It will ask for:  
  - Account ID  
  - Number of Adult, Child, and Infant tickets  

  It will then calculate the totals, perform validation, and either confirm the booking or reject it with an error.

- **Automated unit tests**  
  Run the JUnit tests from your IDE or via Maven:

  ```bash
  mvn clean test
  ```

---

## 5. Unit Test Runs

The project uses **JUnit 5** and **Mockito**.  
Tests cover:
- Calculation of totals.  
- Validation of business rules (valid + invalid cases).  
- Correct interactions with the payment and seat services.  

---

## 6. Detailed Unit Test Coverage

| Test Class                   | Test Name                                | What It Verifies                                                                 |
|-------------------------------|------------------------------------------|---------------------------------------------------------------------------------|
| **PurchaseCalculatorTest**    | calculatesTotalsCorrectly                | 2 Adults + 1 Child + 1 Infant → £65, 3 seats                                   |
|                               | returnsZeroForEmptyRequest               | No tickets = £0, 0 seats                                                        |
|                               | ignoresNullRequests                      | Null inputs are safely ignored                                                  |
|                               | onlyInfantsNoAdults                      | Infants only = £0, 0 seats                                                      |
| **PurchaseRequestValidatorTest** | rejectsInvalidAccountId                | Account ID ≤ 0 is invalid                                                       |
|                               | rejectsZeroTickets                       | Zero tickets not allowed                                                        |
|                               | rejectsMoreThan25Tickets                 | Over 25 tickets not allowed                                                     |
|                               | rejectsChildrenWithoutAdult              | Child tickets require at least one Adult                                        |
|                               | rejectsInfantsWithoutAdult               | Infant tickets require at least one Adult                                       |
|                               | allowsExactly25Tickets                   | 25 tickets is valid (boundary case)                                             |
| **TicketServiceImplTest**     | purchaseSucceedsForAdultChildInfant      | Payment £65, Seats 3 (with 2 Adults + 1 Child + 1 Infant)                       |
|                               | purchaseSucceedsForAdultWithInfantsOnly  | Payment £25, Seats 1 (with 1 Adult + 2 Infants)                                 |
|                               | purchaseRejectsWhenNoAdultWithChildren   | Throws exception, no payment or seat calls                                      |
|                               | purchaseRejectsWhenMoreThan25Tickets     | Throws exception, no external service calls                                     |
|                               | purchaseRejectsWhenAccountInvalid        | Throws exception, no external service calls                                     |
|                               | purchaseAllowsExactly25Tickets           | Valid: correct payment and seat reservation for boundary case                   |

---

## 7. Assumptions

- All account IDs greater than zero are valid and have sufficient funds.  
- The third-party services (`TicketPaymentService`, `SeatReservationService`) always succeed.  
- `TicketTypeRequest` is treated as immutable (constructed once and not modified).  


---

## Closing Note
The code is structured to be straightforward to read and easy to test.  
- You can play with `Main.java` for quick manual checks.  
- The unit tests give full coverage of the business rules and verify that the correct amounts and seat reservations are made.  

