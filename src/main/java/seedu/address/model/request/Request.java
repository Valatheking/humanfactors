package seedu.address.model.request;

import java.time.LocalDate;
/**
 * Request class to get requested info such as duty dates and nric
 */
public class Request {
    private String nric;
    private LocalDate allocatedDate;
    private LocalDate requestedDate;

    public Request(String nric, LocalDate allocatedDate, LocalDate requestedDate) {
        this.nric = nric;
        this.allocatedDate = allocatedDate;
        this.requestedDate = requestedDate;
    }

    public String getNric() {
        return nric;
    }

    public LocalDate getAllocatedDate() {
        return allocatedDate;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }
}
