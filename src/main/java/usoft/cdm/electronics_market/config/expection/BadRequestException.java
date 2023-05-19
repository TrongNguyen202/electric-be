package usoft.cdm.electronics_market.config.expection;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {

        super(message);
    }
}
