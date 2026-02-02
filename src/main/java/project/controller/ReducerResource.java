package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.Reducer.CreateReducerDto;
import project.adapter.in.web.IdDto;
import project.adapter.in.web.Reducer.ComputeDto;
import project.adapter.in.web.Reducer.ReducerPlaceBetDto;
import project.adapter.in.web.Reducer.ReducerServiceAdapter;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    ReducerServiceAdapter adapter;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReducer(@Valid CreateReducerDto Dto){
        var id=adapter.createReducer(Dto);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @GET
    @Path("/{ReducerId}")
    public Response getReducer(@PathParam("ReducerId") Long id){
        var reducer=adapter.loadReducer(id);
        return Response.ok().entity(reducer).build();
    }
    @DELETE
    @Path("/{ReducerId}/delete")
    public Response deleteReducer(@PathParam("ReducerId") Long id){
       adapter.deleteReducer(id);
       return Response.noContent().build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/matches")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id,@Valid IdDto MatchId){
        var out=adapter.addMatchToReducer(id, MatchId);
        return Response.ok().entity(out).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/compute")
    public Response compute(@PathParam("ReducerId") Long id,@Valid ComputeDto specifications){
        var out=adapter.getComputeCombinations(id, specifications);
        return Response.ok().entity(out).build();
    }
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/delete_match")
    public Response deleteMatchFromReducer(@PathParam("ReducerId") Long id,@Valid IdDto matchId){
        adapter.deletMatchFromReducer(id, matchId);
        return Response.noContent().build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/place_bet")
    public Response placeBet(@PathParam("ReducerId") Long id,@Valid ReducerPlaceBetDto Dto){
        var out=adapter.placeReducerBet(id,Dto);
        return Response.status(Response.Status.CREATED).entity(out).build();
    }
    @PUT
    @Path("/{ReducerId}/refresh")
    public Response refresh(@PathParam("ReducerId") Long id){
        var out=adapter.refreshReducer(id);
        return Response.ok().entity(out).build();
    }
}
