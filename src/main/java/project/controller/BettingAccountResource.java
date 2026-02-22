package project.controller;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.adapter.in.web.TransactionDTO.DepositInBettingAccountDto;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.bettinAccountDTO.*;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.TransactionDTO.WithdrawDto;
import project.adapter.in.web.bettinAccountDTO.betslip.MakeBetRequestDto;
import project.adapter.in.web.bettinAccountDTO.betslip.ReadBetSlipDto;
import project.application.port.in.MomoAccounts.MakeDepositUseCase;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.TransactionType;

import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.client.Entity.entity;

@Path("/betting-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BettingAccountResource {

    private static final Logger log = LoggerFactory.getLogger(BettingAccountResource.class);
    @Inject
    BettingServiceAdapter serviceAdapter;
    @Inject
    MakeDepositUseCase makeDepositUseCase;
    @Inject
    MobileMoneyAccountResource mobileMoneyResources;
    @Inject
    MatchResource resource;
    @Inject
    UriInfo uriInfo;
    private Long bettingId;

    //gets all betting accounts from database
    @GET
    public Response getAll(@QueryParam("brokerType")BrokerType broker) {
        return getAllBettingAccountsResponse(broker);
    }

    @GET
    @Path("/{bettingId}")
    public Response getById(@PathParam("bettingId") Long bettingId) {
        var out = serviceAdapter.loadBettingAccount(bettingId);
        List<Link> list = new ArrayList<>();
        var id = out.getId();
        list.add(linkFactory("/" + id + "/withdraw-to-momo/momoId", "withdraw", "PUT"));
        list.add(resource.createMatchLink());
        list.add(resource.getAllMatchesForBrokerTypeLink(out.getBrokerType(),null,null));
        list.add(linkFactory("/" + id + "/betslips/add-pick", "addPickToDraft", "PUT"));
        list.add(linkFactory("/" + id + "/deposit-to-betting", "makeDeposit", "PUT"));
        list.add(linkFactory("/" + id + "/betslips/make-bet", "placeBetFromDraft", "PUT"));
        list.add(linkFactory("/" + id + "/create-bonus", "createNewBonus", "PUT"));
        list.add(linkFactory("matches/byParams", "getMatch", "GET"));
        list.add(getDraftSlipLink(bettingId));
        list.add(emptyDraftSlipLink(bettingId));
        out.setSelfLinks(list);
        out.setBetHistory(toBetHistoryLink(bettingId));
        out.setTransactionHistory(toTransactionHistory(bettingId));

        return Response.ok(out)
                .build();
    }

    /**
     * makes a deposit from mobile money to betting account
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{bettingId}/deposit-to-betting")
    public Response deposit(@PathParam("bettingId") Long bettingId,
                            @Valid DepositInBettingAccountDto dto) {

        makeDepositUseCase.depositFromMobileMoneyToBettingAccount(dto.getMomoId(), bettingId, dto.getAmount(), dto.getDescription());
        List<Link> out = new ArrayList<>();
        out.add(toBettingAccount(bettingId));
        out.addAll(baseLinks(bettingId));
        return Response.ok()
                .entity(out).build();
    }

    @GET
    @Path("/{bettingId}/bet-history")
    public Response getBetHistory(@PathParam("bettingId") Long bettingId,
                                  @QueryParam("status") BetStatus status, @QueryParam("matchKey") String matchKey, @QueryParam("strategy") BetStrategy strategy) {
        var out = serviceAdapter.getBetHistory(bettingId, status, matchKey, strategy);
        for (ReadBetSlipDto m : out.getBetHistory()) {
            m.getLinks().add(toBetSlip(bettingId, m.getId()));
        }
        out.getLinks().addAll(baseLinks(bettingId));

        return Response.ok(out)
                .build();
    }

    @GET
    @Path("/{bettingId}/betslips")
    public Response getBetSlip(@PathParam("bettingId") Long bettingId, @QueryParam("id") Long id) {
        var out = serviceAdapter.getBetSlip(id);
        out.getLinks().add(resource.getMatchOutcomeEntity(id));
        out.getLinks().addAll(baseLinks(bettingId));

        return Response.ok(out)
                .build();
    }

    @GET
    @Path("/{bettingId}/transaction-history")
    public Response getTransactionHistory(@PathParam("bettingId") Long bettingId, @QueryParam("transactionType")TransactionType type) {
        var out = serviceAdapter.getTransactionHistory(bettingId, type);
        out.getLinks().addAll(baseLinks(bettingId));

        return Response.ok(out)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateBettingAccountDto dto) {
        serviceAdapter.createNewBettingAccount(dto);
        List<Link> out = new ArrayList<>();
        out.add(linkFactory("personal-betting-system", "dispatcher", "GET"));
        out.add(getAllAccountsLinks());
        // Either return created location only
        return Response.status(Response.Status.CREATED).entity(out).build();

        // Or if you prefer returning JSON: return Response.status(201).entity(Map.of("id", id)).build();
    }

    /**
     * makes a withdrawal from a betting account to a designated mobile money account
     */
    @PUT
    @Path("/{bettingId}/withdraw-to-momo/momoId")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdrawToMomo(@PathParam("bettingId") Long bettingId, @QueryParam("momoId") Long momoId, @Valid WithdrawDto dto) {
        serviceAdapter.withdrawFromBettingToMobileMoney(bettingId, momoId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(mobileMoneyResources.getAccount(momoId));
        return Response.noContent().entity(out).build();
    }

    /**
     * adds pick to an empty slip on an account
     */
    @GET
    @Path("/{bettingId}/draft-slip")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDraft(@PathParam("bettingId") Long bettingId) {
        var out = serviceAdapter.getDraftSlip(bettingId);
        out.addLink(addPickToDraft(bettingId));
        out.getLinks().addAll(baseLinks(bettingId));
        out.addLink(emptyDraftSlipLink(bettingId));
        out.addLink(getDraftSlipLink(bettingId));
        out.addLink(removePick(bettingId));
        return Response.ok()
                .entity(out).build();
    }

    /**
     * adds pick to an empty slip on an account
     */
    @PUT
    @Path("/{bettingId}/draft-slip/add-pick")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPick(@PathParam("bettingId") Long bettingId, @Valid AddPickRequestBetSlipDto dto) {
        serviceAdapter.addPickToBetSlip(bettingId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(placeBet(bettingId));
        out.add(removePick(bettingId));
        out.add(linkFactory("betting-accounts/" + bettingId + "betslips/add-pick", "add pick to draft slip", "PUT"));
        return Response.ok()
                .entity(out).build();
    }

    /**
     * removes pick to an draft slip on an account
     */
    @PUT
    @Path("/{bettingId}/draft-slip/remove-pick")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removePick(@PathParam("bettingId") Long bettingId, @QueryParam("pickIndex") Integer pickIndex) {
        serviceAdapter.removePickByNumber(bettingId, pickIndex);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(placeBet(bettingId));
        out.add(removePick(bettingId));
        out.add(addPickToDraft(bettingId));
        return Response.ok()
                .entity(out).build();
    }

    /**
     * creates a new empty draft slip in the betting
     */
    @PUT
    @Path("/{bettingId}/empty-slip")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creatEmptyDraft(@PathParam("bettingId") Long bettingId) {
        serviceAdapter.creatEmptyDraftSlip(bettingId);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(addPickToDraft(bettingId));
        return Response.status(Response.Status.CREATED)
                .entity(out).build();
    }

    @PUT
    @Path("/{bettingId}/betslips/make-bet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makeBet(@PathParam("bettingId") Long bettingId, @Valid MakeBetRequestDto dto) {
        serviceAdapter.makeBet(bettingId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        return Response.noContent()
                .entity(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/create-bonus")
    public Response createBonus(@PathParam("id") Long id, @Valid BonusDto dto) {
        serviceAdapter.createBonus(id, dto);
        var out = new ArrayList<>(baseLinks(id));
        return Response.noContent().entity(out).build();
    }


    @PUT
    @Path("/{bettingId}/refund/slipId")
    public Response setSlipToRefund(@PathParam("bettingId") Long bettingId, @QueryParam("slipId") Long slipId) {
        serviceAdapter.setSlipToRefund(bettingId, slipId);
        var out = new ArrayList<>(baseLinks(bettingId));
        return Response.noContent().entity(out).build();
    }

    private Response getAllBettingAccountsResponse(BrokerType broker) {
        final var getAllResponse = this.serviceAdapter.getAllBettingAccounts(broker);
        setSelfLinks(getAllResponse.bettingAccounts());
        getAllResponse.links().add(getAllAccountsLinks());
        getAllResponse.links().add(createBettingAccount());
        getAllResponse.links().add(new Link(uriInfo.getBaseUri()+"personal-betting-system","dipatcher","GET"));
        return Response.ok(getAllResponse)
                .build();
    }

    private void setSelfLinks(List<GetBettingAccountDto> bettingAccount) {
        for (GetBettingAccountDto dto : bettingAccount) {
            dto.getSelfLinks().add(new Link(createSelfLink(dto.getId()), "self", "GET"));
        }
    }

    private String createSelfLink(Long id) {
        return uriInfo.getRequestUriBuilder().path(Long.toString(id)).build().toString();
    }

    private Link linkFactory(String baseEnding, String rel, String type) {
        return new Link(uriInfo.getBaseUri() + "betting-accounts" + baseEnding, rel, type);
    }

    private Link createBettingAccount() {
        return linkFactory("", " create betting account", "POST");

    }

    protected Link toBettingAccount(Long id) {
        return linkFactory("/"+id.toString(), " get betting account", "GET");
    }

    private Link toBetHistoryLink(Long id) {
        return linkFactory("/" + id + "/bet-history?strategy&matchKey&status", " get betting account history", "GET");
    }

    private Link toTransactionHistory(Long id) {
        return linkFactory("/" + id + "/transaction-history?transactionType=", " get transaction account history", "GET");
    }

    private Link toBetSlip(Long bettingAccount, Long slipId) {
        return linkFactory("/" + bettingAccount + "/betSlips?bet-slip-id=" + slipId, " getBetslip", "GET");
    }

    private Link doWithdrawal(Long id) {
        return linkFactory("/" + id + "/withdraw-to-momo/momoId", " withdraw from 1xbet to momo", "PUT");
    }

    private Link doDeposit(Long id) {
        return linkFactory("/" + id + "/deposit-to-betting", " deposit to 1xbet from momo", "PUT");
    }

    protected Link getAllAccountsLinks() {
        return linkFactory("", "get all betting accounts", "GET");
    }

    private Link getDraftSlipLink(Long id) {
        return linkFactory("/"+id+"/draft-slip", "get draft slip of account " + id, "GET");
    }

    protected Link emptyDraftSlipLink(Long bettingId) {
        return linkFactory("/" + bettingId + "/empty-slip", "empty draft slip", "PUT");
    }


    private List<Link> baseLinks(Long id) {
        List<Link> out = new ArrayList<>();
        out.add(toBettingAccount(id));
        out.add(toTransactionHistory(id));
        out.add(toBetHistoryLink(id));
        out.add(doWithdrawal(id));
        out.add(doDeposit(id));
        out.add(getDraftSlipLink(id));
        return out;
    }

    private Link addPickToDraft(Long id) {
        return linkFactory("/" + id + "/draft-slip/add-pick", "add pick to draft slip", "PUT");

    }

    private Link removePick(Long id) {
        return linkFactory("/" + id + "/draft-slip/remove-pick", "remove pick to draft slip", "PUT");

    }

    private Link placeBet(Long id) {
        return linkFactory("/" + id + "/bet-slips/make-bet", "place bet", "PUT");
    }
}
