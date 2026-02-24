package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.BettingAccount.BettingServiceAdapter;
import project.adapter.in.web.Utils.IdDto;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.CreateMatchDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchDto;
import project.adapter.in.web.Reducer.UpdateMatchDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.ReadMatchEventPickDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.UpdateMatchOutcomeDto;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.League;
import project.domain.model.Enums.MatchKey;

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
    @Path("/getAllMatchOutComesByParams")
    public Response getMatchOutComesAll(@QueryParam("ownerName") String ownerName, @QueryParam("matchKey") MatchKey matchKey, @QueryParam("outcomeName") String outComeName, @QueryParam("league") League league) {
        var out = serviceAdapter.getMatchOutcomesByParam(ownerName, matchKey, outComeName, league);
        out.links().add(getAllMatchesLink());
        setOutComeUpDateLinks(out.outComes(), 0L);
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/byId/{matchId}")
    public Response getByIdORName(@PathParam("matchId") Long id) {
        var out = serviceAdapter.getMatchByid(id);
        out.addLink(updateMatchLink(id));
        setOutComeUpDateLinks(out.getMatchOutComes(), out.getId());
        out.addLink(getAllMatchesLink());
        out.addLink(setMatchToBonusLink(id));
        return Response.ok(out).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateMatchDto dto) {
        Long id = serviceAdapter.createMatch(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(createMatchLink());
        out.add(getAllMatchOutComesByParams(MatchKey.FAVOURED, dto.getHome() + " vs " + dto.getAway()));
        out.add(toMatchLink(id));
        return Response.status(Response.Status.CREATED).entity(out).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete_match")
    public Response deleteMatchFromSystem(@Valid IdDto matchId) {
        serviceAdapter.deleteMatchFromSystem(matchId);
        return Response.ok(getAllMatchesLink()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/updateMatch")
    public Response updateMatch(@PathParam("id") Long id, @Valid UpdateMatchDto match) {
        serviceAdapter.updateMatch(id, match);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(toMatchLink(id));
        return Response.ok(out).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{matchId}/updateMatchOutcome")
    public Response updateMatchOutcomeEntity(@PathParam("matchId") Long id, @Valid UpdateMatchOutcomeDto dto) {
        serviceAdapter.updateMatchOutcomes(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(getAllMatchOutComesByParams(dto.matchKey(), ""));
        out.add(toMatchLink(id));
        return Response.ok(out).build();

    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{matchId}/to-bonus")
    public Response setMatchToBonus(@PathParam("matchId") Long id) {
        serviceAdapter.setMatchToBonus(id);
        var match=serviceAdapter.getMatchByid(id);
        List<Link> out = new ArrayList<>();
        out.add(getAllMatchesLink());
        out.add(getAllMatchOutComesByParams(match.getMatchOutComes().getFirst().getMatchKey(), ""));
        out.add(toMatchLink(id));
        return Response.ok(out).build();

    }

    private Link linkFactory(String baseEnding, String rel, String type) {
        return new Link(uriInfo.getBaseUri() + "matches" + baseEnding, rel, type);
    }

    protected Link toMatchLink(Long id) {
        return linkFactory("/byId/" + id, "get match with id ", "GET");
    }

    private Link getAllMatchOutComesByParams(MatchKey matchKey, String ownerNamer) {
        return linkFactory("/getAllMatchOutComesByParams?MatchKey=" + matchKey + "&ownerName=" + ownerNamer, "get all match outcomes by parameters", "GET");
    }

    public Link getMatchOutcomeEntity(Long id) {
        return linkFactory("?MatchId=" + id, "Get match oucome pick", "GET");
    }

    private Response getAllMatchesResponse(BrokerType broker, Long id, String name, Instant start, Instant ende) {
        final var getAllResponse = this.serviceAdapter.getAllMatches(broker, id, name, start, ende);
        setSelfLinks(getAllResponse.allMatches());
        getAllResponse.links().add(createMatchLink());
        getAllResponse.links().add(getAllMatchOutComesByParams(null, name));
        getAllResponse.links().add(new Link(uriInfo.getBaseUri() + "personal-betting-system", "dipatcher", "GET"));
        return Response.ok(getAllResponse)
                .build();
    }

    protected Link getAllMatchesLink() {
        return linkFactory("?brokerType=&id=&startTime=&endTime=", "get all matdchs", "GET");
    }

    protected Link getAllMatchesForBrokerTypeLink(BrokerType broker, Instant start, Instant end) {
        StringBuilder sb = new StringBuilder("?brokerType=" + broker);
        if (start != null) {
            sb.append("&startTime=").append(start);
        }
        if (end != null) {
            sb.append("&endTime=").append(end);
        }
        return linkFactory(sb.toString(), "get all matches", "GET");
    }

    protected Link createMatchLink() {
        return linkFactory("", "create new match", "POST");
    }

    private Link updateMatchLink(Long id) {
        return linkFactory("/" + id + "/updateMatch", "update match", "PUT");
    }

    private void setSelfLinks(List<ReadMatchDto> matchesDto) {
        for (ReadMatchDto dto : matchesDto) {
            dto.getLinks().add(toMatchLink(dto.getId()));
            dto.getLinks().add(setMatchToBonusLink(dto.getId()));
        }
    }

    private Link updateOutComeLink(Long id) {
        return linkFactory("/" + id + "/updateMatchOutCome", "Update matchOutCome", "PUT");
    }

    private Link setMatchToBonusLink(Long id) {
        return linkFactory("/"+id+"/to-bonus","set match to bonus", "PUT");
    }

    private void setOutComeUpDateLinks(List<ReadMatchEventPickDto> matchesEventDtos, Long id) {

        for (ReadMatchEventPickDto dto : matchesEventDtos) {
            dto.setUpdate(updateOutComeLink(id));
        }
    }

}



