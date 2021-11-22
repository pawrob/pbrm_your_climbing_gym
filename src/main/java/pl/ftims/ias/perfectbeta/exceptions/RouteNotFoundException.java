package pl.ftims.ias.perfectbeta.exceptions;

public class RouteNotFoundException extends AbstractAppException {
    public static final String ROUTE_WITH_ID_NOT_EXISTS = "Route with identifier: %s does not exist";

    private RouteNotFoundException(String message) {
        super(message);
    }

    public static RouteNotFoundException createRouteWithProvidedIdNotFoundException(long id) {
        return new RouteNotFoundException(String.format(ROUTE_WITH_ID_NOT_EXISTS, id));
    }
}
