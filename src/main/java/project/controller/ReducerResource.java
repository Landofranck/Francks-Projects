package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.IndexDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.adapter.in.web.Reducer.*;
import project.adapter.in.web.Reducer.ReducerDto.*;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.Utils.Link;
import project.domain.model.Enums.BrokerType;

import java.util.ArrayList;
import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntity_.id;

@Path("/reducer")
@Produces(MediaType.APPLICATION_JSON)
public class ReducerResource {
    @Inject
    ReducerServiceAdapter adapter;
    @Inject
    UriInfo uriInfo;
    @Inject
    MatchResource resource;
    Boolean systemTime;

    @GET
    public Response getAll(@QueryParam("brokerType") BrokerType broker) {
        var out = adapter.getAllReducers(broker);
        setReducerLinks(out.allReducers());//
        out.links().add(createReducerLink());
        out.links().add(dispatcherLink());
        out.links().add(creatReducerSummaryLink());
        out.links().add(getAllReducerSummaryLink());

        return Response.ok().entity(out).build();
    }

    private void setReducerLinks(List<ReadReducerDto> list) {
        for (ReadReducerDto dto : list) {
            dto.links().add(getReducerLink(dto.id()));
            dto.links().add(deleteReducerLink(dto.id()));
        }
    }

    @GET
    @Path("/reducer_summary")
    public Response getAllReducerSummary() {
        var out = adapter.getAllReducerSummaries();
        setSummaryLinks(out.reducerDtos());
        out.links().add(creatReducerSummaryLink());
        out.links().add(createReducerLink());
        out.links().add(getAllReducersLink());
        return Response.ok().entity(out).build();
    }

    private void setSummaryLinks(List<ReadReducerSummaryDto> list) {
        for (ReadReducerSummaryDto dto : list) {
            dto.addLink(toReducerSummaryLink(dto.getReducerSummaryId()));
            dto.addLink(deleteReducerSummaryLink(dto.getReducerSummaryId()));
        }
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
        reducer.links().add(shuffleMatcheLink(id));
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
        out.links().add(shuffleMatcheLink(id));
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
        out.links().add(shuffleMatcheLink(id));
        return Response.ok().entity(out).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/delete_match")
    public Response deleteMatchFromReducer(@PathParam("ReducerId") Long id, @Valid IdDto matchId) {
        adapter.deletMatchFromReducer(id, matchId);
        List<Link> out = new ArrayList<>(baseLinks(id));
        out.add(dispatcherLink());
        out.add(shuffleMatcheLink(id));
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/place_bet")
    public Response placeBet(@PathParam("ReducerId") Long id, @QueryParam("slip_index") Integer index, @Valid ReducerPlaceBetDto Dto) {
        var out = adapter.placeReducerBet(id, index, Dto);
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
        out.links().add(shuffleMatcheLink(id));

        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{ReducerId}/update_stake")
    public Response updateReducerStakeAndBonus(@PathParam("ReducerId") Long id, @Valid UpdateReducerStakeDto dto) {
        adapter.updateStake(id, dto);
        var out = new ArrayList<>(baseLinks(id));
        out.add(shuffleMatcheLink(id));
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("reducer_summary/{id}")
    public Response getReducerSummary(@PathParam("id") Long id) {
        var out = adapter.getReducerSummary(id);
        out.getLinks().add(shuffleAllReducerMatchesInSummaryLink(id));
        out.getLinks().add(addReducerToSummaryLink(id));
        return Response.ok(out).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("create_reducer_summary")
    public Response creatReducerSummary(@Valid CreateReducerSummaryDto dto) {
        var id = adapter.createReducerSummary(dto);
        List<Link> out = new ArrayList<>();
        out.add(toReducerSummaryLink(id));
        out.add(deleteReducerSummaryLink(id));
        out.add(getAllReducerSummaryLink());
        return Response.ok(out).build();
    }

    @DELETE
    @Path("delete_reducer_summary/{id}")
    public Response deleteReducerSummary(@PathParam("id") Long summaryId) {
        adapter.deleteReducerSummary(summaryId);
        List<Link> out = new ArrayList<>();
        out.add(getAllReducersLink());
        return Response.ok(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/add_all_matches")
    public Response addAllMatchesToReducer(@PathParam("id") Long id, @Valid AddAllMatchesDto dto) {
        var out = adapter.AddAllMatchesToReducer(id, dto);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Path("{id}/clear_all_matches")
    public Response clearAllMatchesToReducer(@PathParam("id") Long id) {
        var out = adapter.clearAllReducerMatches(id);
        out.links().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/summary/shuffle_all_matches")
    public Response shuffleAllReducerMatchesInSummary(@PathParam("id") Long id, @Valid IndexDto index) {
        var out = adapter.shuffleAllMatches(id, index.index());
        out.getLinks().addAll(baseLinks(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/shuffle_matches")
    public Response shuffleReducerMatches(@PathParam("id") Long id, @Valid IndexDto index) {
        var out = adapter.shuffleMatches(id, index.index());
        out.links().addAll(baseLinks(id));
        out.links().add(shuffleMatcheLink(id));
        return Response.ok().entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add_reducer_to_summary")
    public Response addReducerToSummary(@QueryParam("summaryId") Long summaryId, @QueryParam("reducerId") Long reducerId) {
            var out = adapter.addReducerToSummary(summaryId, reducerId);
            return Response.ok().entity(out).build();
    }

    // ---------------- links ----------------
    private Link linkFactory(String baseEnding, String rel, String type) {
        // Matches other resources: prefix the resource path here
        return new Link(uriInfo.getBaseUri() + "reducer" + baseEnding, rel, type);
    }

    private Link toReducerSummaryLink(Long id) {
        return linkFactory("/reducer_summary/" + id, "get reducer summary", "GET");
    }

    private Link addAllMatcheToReducerLink(Long id) {
        return linkFactory("/" + id + "/add_all_matches", "add all matches to reducer", "PUT");
    }

    private Link clearAllMatchesLink(Long id) {
        return linkFactory("/" + id + "/clear_all_matches", "clear all matches from reducer", "PUT");
    }

    private Link shuffleMatcheLink(Long id) {
        return linkFactory("/" + id + "/shuffle_matches", "shuffle matches from reducer", "PUT");
    }

    private Link dispatcherLink() {
        return new Link(uriInfo.getBaseUri() + "personal-betting-system", "dispatcher", "GET");
    }

    public Link getAllReducersLink() {
        this.systemTime = systemTime;
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

    private Link shuffleAllReducerMatchesInSummaryLink(Long summaryId) {
        return linkFactory("/" + summaryId + "/shuffle_all_matches", "shuffle all matches in summary", "PUT");
    }

    private Link computeLink(Long reducerId) {
        return linkFactory("/" + reducerId + "/compute", "compute combinations", "PUT");
    }

    private Link placeBetLink(Long reducerId, Integer index) {
        return linkFactory("/" + reducerId + "/place_bet?slip_index=" + index, "place bet", "POST");
    }

    private Link addReducerToSummaryLink(Long summaryId) {
        return linkFactory("/add_reducer_to_summary?summaryId=" + summaryId + "&reducerId=", "add reducer to summary", "PUT");
    }

    private Link deleteReducerSummaryLink(Long reducerId) {
        return linkFactory("delete_reducer_summary/" + reducerId, "delete reducer summary", "DELETE");
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

    private Link getAllReducerSummaryLink() {
        return linkFactory("/reducer_summary", "get all reducer summary", "GET");
    }

    private Link creatReducerSummaryLink() {
        return linkFactory("/create_reducer_summary", "create reducer summary", "POST");
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
        out.add(clearAllMatchesLink(reducerId));
        out.add(addAllMatcheToReducerLink(reducerId));
        return out;
    }

    private void addLinksToMatches(ReadReducerDto reducer) {
        for (ReadMatchDto m : reducer.betMatchDtos()) {
            m.getLinks().add(resource.toMatchLink(m.getId()));
        }
    }

    private void addLinkToSlip(ReadReducerDto reducer) {
        int i = 0;
        for (ReducerSlipDto slip : reducer.slips()) {
            slip.getLinks().add(placeBetLink(reducer.id(), i));
            i++;
        }
    }

    public void setSytemTime(Boolean systemTime) {
        this.systemTime = systemTime;
    }

}
