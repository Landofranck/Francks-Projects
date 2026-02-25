package project.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.hibernate.annotations.MapKeyCompositeType;
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

    private Long bettingId;
    private Long slipId;

    @GET
    public Response getAll(@QueryParam("brokerType") BrokerType broker, @QueryParam("MatchId") Long id, @QueryParam("MatchName") String name, @QueryParam("matchStart") Instant startTime, @QueryParam("matchEnd") Instant endTime) {
        return getAllMatchesResponse(broker, id, name, startTime, endTime);
    }

    @GET
    @Path("/getAllMatchOutComesByParams")
    public Response getMatchOutComesAll(Long id, @QueryParam("ownerName") String ownerName, @QueryParam("matchKey") MatchKey matchKey, @QueryParam("outcomeName") String outComeName, @QueryParam("league") League league) {
        var out = serviceAdapter.getMatchOutcomesByParam(ownerName, matchKey, outComeName, league);
        out.links().add(getAllMatchesLink());
        out.links().add(toMatchLink(id));
        setOutComeUpDateLinks(out.outComes());
        return Response.ok().entity(out).build();
    }

    @GET
    @Path("/byId/{matchId}")
    public Response getByIdORName(@PathParam("matchId") Long id) {
        var out = serviceAdapter.getMatchByid(id);
        out.addLink(updateMatchLink(id));
        setOutComeUpDateLinks(out.getMatchOutComes());
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
    public Response updateMatchOutcomeEntity(@PathParam("matchId") Long id,@NotNull @QueryParam("ownerMatchName") String ownerMatchName, @NotNull @QueryParam("MatchKey") MatchKey matchKey, @NotNull @QueryParam("outcomeName") String outComeName, @NotNull @QueryParam("league") League league, @Valid UpdateMatchOutcomeDto dto) {
        serviceAdapter.updateMatchOutcomes(id,ownerMatchName,matchKey,outComeName,league,dto);
        List<Link> out = new ArrayList<>();
      if(bettingId!=null&&slipId!=null){
          out.add(new Link(uriInfo.getBaseUri() + "betting_accounts/"+bettingId +"/bet_slips?bet_slip_id="+slipId,"get bet slip", "GET"));
          bettingId=null;
          slipId=null;
      }
        out.add(getAllMatchesLink());
        out.add(getAllMatchOutComesByParams(dto.matchKey(), ""));
        out.add(toMatchLink(id));
        return Response.ok(out).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{matchId}/to_bonus")
    public Response setMatchToBonus(@PathParam("matchId") Long id) {
        serviceAdapter.setMatchToBonus(id);
        var match = serviceAdapter.getMatchByid(id);
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

    protected Link updateOutComeLink(Long bettingId,Long slipId, ReadMatchEventPickDto pick) {
        this.bettingId=bettingId;
        this.slipId=slipId;
        return linkFactory("/" + pick.getMatchId() + "/updateMatchOutcome?ownerMatchName="+pick.getOwnerMatchName()+"&MatchKey="+pick.getMatchKey()+"&outcomeName="+pick.getOutcomeName()+"&league="+pick.getLeague(), "Update matchOutCome", "PUT");
    }

    private Link setMatchToBonusLink(Long id) {
        return linkFactory("/" + id + "/to-bonus", "set match to bonus", "PUT");
    }

    private void setOutComeUpDateLinks(List<ReadMatchEventPickDto> matchesEventDtos) {

        for (ReadMatchEventPickDto dto : matchesEventDtos) {
            dto.getLinks().add(updateOutComeLink(null,null,dto));
        }
    }

}



