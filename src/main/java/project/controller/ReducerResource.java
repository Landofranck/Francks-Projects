package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.CreateReducerDto;
import project.adapter.in.web.IdDto;
import project.adapter.in.web.Reducer.ComputeDto;
import project.adapter.in.web.Reducer.ReducerServiceAdapter;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    ReducerServiceAdapter adapter;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReducer(CreateReducerDto Dto){
        var id=adapter.createReducer(Dto);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @GET
    @Path("/{ReducerId}")
    public Response getReducer(@PathParam("ReducerId") Long id){
        var reducer=adapter.loadReducer(id);
        return Response.ok().entity(reducer).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/matches")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id, IdDto MatchId){
        var out=adapter.addMatchToReducer(id, MatchId);
        return Response.ok().entity(out).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/compute")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id, ComputeDto specifications){
        var out=adapter.getComputeCombinations(id, specifications);
        return Response.ok().entity(out).build();
    }
}
