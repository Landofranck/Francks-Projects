package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.MatchDto;

import java.util.List;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource {

    @Inject
    BettingServiceAdapter serviceAdapter;

    @GET
    public List<MatchDto> getAll() {

        return serviceAdapter.getAllMatches();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(MatchDto dto) {
        Long id = serviceAdapter.createMatch(dto);
        return Response.status(201).entity(java.util.Map.of("id", id)).build();
    }
}

