package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.IdDto;
import project.adapter.in.web.MatchDto;
import project.adapter.in.web.Reducer.UpdateMatchDto;

import java.util.List;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource {

    @Inject
    BettingServiceAdapter serviceAdapter;

    @GET
    public Response getAll() {
        var out = serviceAdapter.getAllMatches();
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("{MatchId}")
    public Response getAll(@PathParam("MatchId") Long id) {
        var out = serviceAdapter.getMatchByid(id);
        return Response.ok().entity(out).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid MatchDto dto) {
        Long id = serviceAdapter.createMatch(dto);
        return Response.status(201).entity(java.util.Map.of("id", id)).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete_match")
    public Response deleteMatchFromSystem(@Valid IdDto matchId) {
        serviceAdapter.deleteMatchFromSystem(matchId);
        return Response.noContent().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateMatch")
    public Response updateMatch(@Valid UpdateMatchDto match) {
            serviceAdapter.updateMatch(match.id(), match);
            return Response.noContent().build();

    }
}

