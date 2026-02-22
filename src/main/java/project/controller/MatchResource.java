package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.bettinAccountDTO.MatchDto;
import project.adapter.in.web.Reducer.UpdateMatchDto;
import project.adapter.in.web.bettinAccountDTO.MatchEventPickDto;
import project.adapter.in.web.bettinAccountDTO.betslip.UpdateMatchOutcomeDto;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Path("/matches")
@Produces(MediaType.APPLICATION_JSON)
public class MatchResource {

    @Inject
    BettingServiceAdapter serviceAdapter;
    @Inject
    UriInfo uriInfo;

    @GET
    public Response getAll(@QueryParam("brokerType") BrokerType broker, @QueryParam("MatchId") Long id, @QueryParam("MatchName") String name, @QueryParam("matchStart") Instant startTime, @QueryParam("matchEnd") Instant endTime) {
        return getAllMatchesResponse(broker, id, name, startTime, endTime);
    }

    @GET
    @Path("/byParams")
    public Response getMatchOutComesAll(@QueryParam("matchKey") String matchKey, @QueryParam("outcomeName") String outComeName, @QueryParam("league") League league) {
        var out = serviceAdapter.getMatchOutcomesByParam(matchKey, outComeName, league);
        out.links().add(getAllMatchesLink());
        setOutComeUpDateLinks(out.outComes(), 0L);
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/bynameOrId")
    public Response getByIdORName(@QueryParam("brokerType") BrokerType broker, @QueryParam("MatchId") Long id, @QueryParam("MatchName") String name) {
        var out = serviceAdapter.getMatchByid(id, broker, name);
        out.addLink(updateMatchLink(id));
        setOutComeUpDateLinks(out.getMatchOutComes(), out.getId());
        out.addLink(getAllMatchesLink());
        return Response.ok().entity(out).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid MatchDto dto) {
        Long id = serviceAdapter.createMatch(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(toMatchLink(id, dto.getHome()));
        return Response.status(Response.Status.CREATED).entity(out).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete_match")
    public Response deleteMatchFromSystem(@Valid IdDto matchId) {
        serviceAdapter.deleteMatchFromSystem(matchId);
        return Response.noContent().entity(getAllMatchesLink()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/updateMatch")
    public Response updateMatch(@PathParam("id") Long id, @Valid UpdateMatchDto match) {
        serviceAdapter.updateMatch(id, match);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(toMatchLink(id, ""));
        return Response.noContent().entity(out).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{matchId}/updateMatchOutcome")
    public Response updateMatchOutcomeEntity(@PathParam("matchId") Long id, @Valid UpdateMatchOutcomeDto dto) {
        serviceAdapter.updateMatchOutcomes(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(toMatchLink(id, ""));
        return Response.noContent().entity(out).build();

    }

    private Link linkFactory(String baseEnding, String rel, String type) {
        return new Link(uriInfo.getBaseUri() + "matches/" + baseEnding, rel, type);
    }

    private Link toMatchLink(Long id, String home) {
        return linkFactory("/byParams?MatchId=" + id + "&MatchName=" + home, "get match", "GET");
    }

    public Link getMatchOutcomeEntity(Long id) {
        return linkFactory("?MatchId=" + id, "Get match oucome pick", "GET");
    }

    private Response getAllMatchesResponse(BrokerType broker, Long id, String name, Instant start, Instant ende) {
        final var getAllResponse = this.serviceAdapter.getAllMatches(broker, id, name, start, ende);
        setSelfLinks(getAllResponse.allMatches());
        getAllResponse.links().add(createMatchLink());
        getAllResponse.links().add(new Link(uriInfo.getBaseUri() + "personal-betting-system", "dipatcher", "GET"));
        return Response.ok(getAllResponse)
                .build();
    }

    protected Link getAllMatchesLink() {
        return linkFactory("", "get all reducer accounts", "GET");
    }
    protected Link getAllMatchesForBrokerTypeLink(BrokerType broker,Instant start,Instant end) {
        StringBuilder sb=new StringBuilder("?brokerType="+broker);
        if(start!=null){
            sb.append("&startTime=").append(start);
        }
        if(end!=null){
            sb.append("&endTime=").append(end);
        }
        return linkFactory(sb.toString(), "get all reducer accounts", "GET");
    }

    protected Link createMatchLink() {
        return linkFactory("", "create new match", "POST");
    }

    private Link updateMatchLink(Long id) {
        return linkFactory("/" + id + "/updateMatch", "create new match", "POST");
    }

    private Link getMatchLink() {
        return linkFactory("", "create new match", "POST");

    }

    private void setSelfLinks(List<MatchDto> matchesDto) {
        for (MatchDto dto : matchesDto) {
            dto.getLinks().add(new Link(createSelfLink(dto.getId()), "self", "GET"));
        }
    }

    private Link updateOutComeLink(Long id) {
        return linkFactory("/" + id + "/updateMatchOutCome", "Update matchOutCome", "PUT");
    }

    private void setOutComeUpDateLinks(List<MatchEventPickDto> matchesEventDtos, Long id) {

        for (MatchEventPickDto dto : matchesEventDtos) {
            dto.setUpdate(updateOutComeLink(id));
        }
    }

    private String createSelfLink(Long id) {
        return uriInfo.getRequestUriBuilder().path(Long.toString(id)).build().toString();
    }

}

