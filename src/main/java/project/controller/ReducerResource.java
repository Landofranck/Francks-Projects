package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.CreateReducerDto;
import project.adapter.in.web.TransactionDTO.ReadReducerDto;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    BettingServiceAdapter adapter;
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
}
