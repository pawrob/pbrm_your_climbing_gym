package pl.ftims.ias.perfectbeta.exceptions;

public class RatingNotFoundException extends AbstractAppException {
    public static final String RATING_WITH_ID_NOT_EXISTS = "Rating with identifier: %s does not exist";

    private RatingNotFoundException(String message) {
        super(message);
    }

    public static RatingNotFoundException createRatingWithProvidedIdNotFoundException(long id) {
        return new RatingNotFoundException(String.format(RATING_WITH_ID_NOT_EXISTS, id));
    }
}