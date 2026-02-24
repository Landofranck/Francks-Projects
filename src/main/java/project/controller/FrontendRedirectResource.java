package project.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;

/**
 * Convenience redirect so "http://localhost:8080/bettingsystems" works without needing a trailing slash.
 */
@Path("/bettingsystems")
public class FrontendRedirectResource {

    @GET
    public Response redirect() {
        return Response.temporaryRedirect(URI.create("/bettingsystems/")).build();
    }
}
