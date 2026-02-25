package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.adapter.in.web.Reducer.*;
import project.adapter.in.web.Reducer.ReducerDto.UpdateReducerStakeDto;
import project.adapter.in.web.Utils.IdDto;
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
    @Inject
    MatchResource resource;

    @GET
    public Response getAll(@QueryParam("brokerType") BrokerType broker) {
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
        reducer.links().add(getAllMatchesLink(id, reducer.broker()));
        addLinkToSlip(reducer);
        addLinksToMatches(reducer);
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
        addLinksToMatches(out);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/compute")
    public Response compute(@PathParam("ReducerId") Long id, @Valid ComputeDto specifications) {
        var out = adapter.getComputeCombinations(id, specifications);
        addLinksToMatches(out);
        addLinkToSlip(out);
        out.links().add(getAllMatchesLink(id, out.broker()));
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
    public Response placeBet(@PathParam("ReducerId") Long id,@QueryParam("slip_index")Integer index, @Valid ReducerPlaceBetDto Dto) {
        var out = adapter.placeReducerBet(id, index,Dto);
        addLinkToSlip(out);
        addLinksToMatches(out);
        out.links().addAll(baseLinks(id));
        return Response.status(Response.Status.CREATED).entity(out).build();
    }

    @PUT
    @Path("/{ReducerId}/refresh")
    public Response refresh(@PathParam("ReducerId") Long id) {
        var out = adapter.refreshReducer(id);
        out.links().add(getAllMatchesLink(id, out.broker()));
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Path("/{ReducerId}/update_stake")
    public Response updateReducerStakeAndBonus(@PathParam("ReducerId") Long id, @Valid UpdateReducerStakeDto dto) {
        adapter.updateStake(id, dto);
        var out = new ArrayList<>(baseLinks(id));
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

    private Link placeBetLink(Long reducerId,Integer index) {
        return linkFactory("/" + reducerId + "/place_bet?slip_index="+ index, "place bet", "POST");
    }

    private Link refreshLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/refresh", "refresh reducer", "PUT");
    }

    private Link upDateStakeLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/update_stake", "update reducer stake ", "PUT");
    }

    private Link getAllMatchesLink(Long reducerId, BrokerType brokerType) {
        return resource.getAllMatchesForBrokerTypeLink(brokerType, null, null);
    }


    private List<Link> baseLinks(Long reducerId) {
        List<Link> out = new ArrayList<>();
        out.add(getReducerLink(reducerId));
        out.add(addMatchLink(reducerId));
        out.add(deleteMatchLink(reducerId));
        out.add(computeLink(reducerId));
        out.add(refreshLink(reducerId));
        out.add(getAllReducersLink());
        out.add(upDateStakeLink(reducerId));
        return out;
    }

    private void addLinksToMatches(ReadReducerDto reducer) {
        for (ReadMatchDto m : reducer.betMatchDtos()) {
            m.getLinks().add(resource.toMatchLink(m.getId()));
        }
    }

    private void addLinkToSlip(ReadReducerDto reducer) {
        int i=0;
        for (ReducerSlipDto slip : reducer.slips()) {
            slip.getLinks().add(placeBetLink(reducer.id(),i));
            i++;
        }
    }
}
