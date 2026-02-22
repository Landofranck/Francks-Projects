package project.controller.ExceptionMappers;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import project.adapter.in.web.Utils.Code;
import project.adapter.in.web.Utils.ErrorResponseDto;
import project.adapter.in.web.Utils.Link;

import java.util.List;

@Provider
public class FallbackExceptionMapper implements ExceptionMapper<Throwable> {
    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {
        var body = new ErrorResponseDto();
        body.setStatus((ex.getClass().toString().equals("class java.lang.NullPointerException"))? 403:500);
        body.setCode(Code.INTERNAL_ERROR);
        body.setMessage(ex.getClass()+": "+ex.getMessage());

        body.setLinks(List.of(
                new Link(uriInfo.getBaseUri() + "personal-betting-system", "home", "GET")
        ));

        return Response.status(body.getStatus()).entity(body).type(MediaType.APPLICATION_JSON).build();
    }
}
