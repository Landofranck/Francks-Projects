package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.CreateReducerDto;
import project.adapter.in.web.IdDto;
import project.adapter.in.web.Reducer.ReducerServiceAdapter;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    ReducerServiceAdapter adapter;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReducer(CreateReducerDto Dto){
        adapter.createReducer(Dto);
        return Response.noContent().build();
    }

    @GET
    @Path("/{ReducerId}")
    public Response getReducer(@PathParam("ReducerId") Long id){
        var reducer=adapter.loadReducer(id);
        return Response.ok().entity(reducer).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id, IdDto MatchId){
        var out=adapter.addMatchToReducer(id, MatchId);
        return Response.ok().entity(out).build();
    }
    @PUT
    @Path("/{ReducerId}")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id){
        var out=adapter.getComputeCombinations(id);
        return Response.ok().entity(out).build();
    }
}
