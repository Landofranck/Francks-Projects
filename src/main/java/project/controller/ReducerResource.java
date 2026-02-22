package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.Reducer.CreateReducerDto;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.Reducer.ComputeDto;
import project.adapter.in.web.Reducer.ReducerPlaceBetDto;
import project.adapter.in.web.Reducer.ReducerServiceAdapter;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.BrokerType;

import java.util.ArrayList;
import java.util.List;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    ReducerServiceAdapter adapter;
    @Inject
    UriInfo uriInfo;

    @GET
    public Response getAll(@QueryParam("brokerType")BrokerType broker) {
        var out = adapter.getAllReducers(broker); // if exists
        out.links().add(createReducerLink());
        out.links().add(dispatcherLink());
        return Response.ok(out).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReducer(@Valid CreateReducerDto dto) {
        var id = adapter.createReducer(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllReducersLink());
        out.add(getReducerLink(id));
        out.add(addMatchLink(id));
        out.add(dispatcherLink());

        return Response.status(Response.Status.CREATED).entity(out).build();
    }

    @GET
    @Path("/{ReducerId}")
    public Response getReducer(@PathParam("ReducerId") Long id) {
        var reducer = adapter.loadReducer(id);
        reducer.links().addAll(baseLinks(id));
        return Response.ok(reducer).build();
    }

    @DELETE
    @Path("/{ReducerId}/delete")
    public Response deleteReducer(@PathParam("ReducerId") Long id) {
        adapter.deleteReducer(id);
        List<Link> out = new ArrayList<>();
        out.add(getAllReducersLink());
        out.add(createReducerLink());
        return Response.noContent().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/matches")
    public Response addMatchToReducer(@PathParam("ReducerId") Long id, @Valid IdDto MatchId) {
        var out = adapter.addMatchToReducer(id, MatchId);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/compute")
    public Response compute(@PathParam("ReducerId") Long id, @Valid ComputeDto specifications) {
        var out = adapter.getComputeCombinations(id, specifications);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/delete_match")
    public Response deleteMatchFromReducer(@PathParam("ReducerId") Long id, @Valid IdDto matchId) {
        adapter.deletMatchFromReducer(id, matchId);
        List<Link> out = new ArrayList<>(baseLinks(id));
        out.add(dispatcherLink());
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/place_bet")
    public Response placeBet(@PathParam("ReducerId") Long id, @Valid ReducerPlaceBetDto Dto) {
        var out = adapter.placeReducerBet(id, Dto);
        out.links().addAll(baseLinks(id));
        return Response.status(Response.Status.CREATED).entity(out).build();
    }

    @PUT
    @Path("/{ReducerId}/refresh")
    public Response refresh(@PathParam("ReducerId") Long id) {
        var out = adapter.refreshReducer(id);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    // ---------------- links ----------------

    private Link linkFactory(String baseEnding, String rel, String type) {
        // Matches other resources: prefix the resource path here
        return new Link(uriInfo.getBaseUri() + "reducer" + baseEnding, rel, type);
    }

    private Link dispatcherLink() {
        return new Link(uriInfo.getBaseUri() + "personal-betting-system", "dispatcher", "GET");
    }

    public Link getAllReducersLink() {
        return linkFactory("", "get all reducers", "GET");
    }

    private Link createReducerLink() {
        return linkFactory("", "create reducer", "POST");
    }

    private Link getReducerLink(Long reducerId) {
        return linkFactory("/" + reducerId, "get reducer", "GET");
    }

    private Link deleteReducerLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/delete", "delete reducer", "DELETE");
    }

    private Link addMatchLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/matches", "add match to reducer", "PUT");
    }

    private Link deleteMatchLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/delete_match", "delete match from reducer", "DELETE");
    }

    private Link computeLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/compute", "compute combinations", "PUT");
    }

    private Link placeBetLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/place_bet", "place bet", "POST");
    }

    private Link refreshLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/refresh", "refresh reducer", "PUT");
    }

    private List<Link> baseLinks(Long reducerId) {
        List<Link> out = new ArrayList<>();
        out.add(getReducerLink(reducerId));
        out.add(addMatchLink(reducerId));
        out.add(deleteMatchLink(reducerId));
        out.add(computeLink(reducerId));
        out.add(placeBetLink(reducerId));
        out.add(refreshLink(reducerId));
        out.add(getAllReducersLink());
        return out;
    }
}
