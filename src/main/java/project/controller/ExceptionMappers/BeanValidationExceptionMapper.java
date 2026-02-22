package project.controller.ExceptionMappers;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import project.adapter.in.web.Utils.Code;
import project.adapter.in.web.Utils.ErrorResponseDto;
import project.adapter.in.web.Utils.Link;

import java.util.List;

@Provider
public class BeanValidationExceptionMapper
        implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException ex) {

        var body = new ErrorResponseDto();
        body.setStatus(400);
        body.setCode(Code.DTO_VALIDATION_ERROR);
        body.setMessage(ex.getMessage());

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " : " + v.getMessage())
                .toList();

        body.setLinks(List.of(
                new Link(uriInfo.getBaseUri() + "personal-betting-system", "home", "GET")
        ));

        return Response.status(400).entity(body).build();
    }
}