package pl.ftims.ias.perfectbeta.mos.services;

import org.springframework.http.ResponseEntity;
import pl.ftims.ias.perfectbeta.dto.routes_dtos.RouteDTO;
import pl.ftims.ias.perfectbeta.entities.RouteEntity;
import pl.ftims.ias.perfectbeta.exceptions.AbstractAppException;

public interface RouteServiceLocal {

    RouteEntity addRoute(RouteDTO wallDTO) throws AbstractAppException;

    ResponseEntity removeRoute(Long gymId, Long routeId) throws AbstractAppException;

    RouteEntity editRouteDetails(Long gymId, Long routeId, RouteDTO routeDTO) throws AbstractAppException;
}
