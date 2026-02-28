package project.controller;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.*;
import project.adapter.in.web.TransactionDTO.DepositInBettingAccountDto;
import project.adapter.in.web.TransactionDTO.TransactionDto;
import project.adapter.in.web.Utils.Link;
import project.adapter.in.web.BettingAccount.BettingServiceAdapter;
import project.adapter.in.web.TransactionDTO.WithdrawDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.MakeBetRequestDto;
import project.adapter.in.web.BettingAccount.bettinAccountDTO.betslip.ReadBetSlipDto;
import project.application.port.in.MomoAccounts.MakeDepositUseCase;
import project.domain.model.Enums.BetStatus;
import project.domain.model.Enums.BetStrategy;
import project.domain.model.Enums.BrokerType;
import project.domain.model.Enums.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.ws.rs.client.Entity.entity;

@Path("/betting_accounts")
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
    private Boolean systemTime;
    private Long bettingId;

    //gets all betting accounts from database
    @GET
    public Response getAll(@QueryParam("broker_type") BrokerType broker) {
        return getAllBettingAccountsResponse(broker);
    }

    @GET
    @Path("/{bettingId}")
    public Response getById(@PathParam("bettingId") Long bettingId) {
        var out = serviceAdapter.loadBettingAccount(bettingId);
        List<Link> list = new ArrayList<>();
        var id = out.getId();
        list.add(getAllAccountsLinks());
        list.add(doWithdrawalLink(id));
        list.add(resource.createMatchLink());
        list.add(resource.getAllMatchesForBrokerTypeLink(out.getBrokerType(), null, null));
        list.add(addPickToDraftLink(id));
        list.add(doDepositLink(id));
        list.add(linkFactory("/" + id + "/betslips/make_bet", "placeBetFromDraft", "PUT"));
        list.add(linkFactory("/" + id + "/create_bonus", "createNewBonus", "PUT"));
        list.add(linkFactory("matches/byParams", "readMatch", "GET"));
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
    @Path("/{bettingId}/deposit_to_betting")
    public Response deposit(@PathParam("bettingId") Long bettingId,
                            @Valid DepositInBettingAccountDto dto) {
        if (systemTime) {
            dto.setTransactionTime(null);
        } else {
            Objects.requireNonNull(dto.getTransactionTime(),"if system time is set to false the you have to input transaction time");
        }
        makeDepositUseCase.depositFromMobileMoneyToBettingAccount(dto.getMomoId(), dto.getTransactionTime(), bettingId, dto.getAmount(), dto.getDescription());
        return Response.ok(baseLinks(bettingId)).build();
    }

    @GET
    @Path("/{bettingId}/bet_history")
    public Response getBetHistory(@PathParam("bettingId") Long bettingId,
                                  @QueryParam("status") BetStatus status, @QueryParam("matchKey") String matchKey, @QueryParam("strategy") BetStrategy strategy) {
        var out = serviceAdapter.getBetHistory(bettingId, status, matchKey, strategy);
        for (ReadBetSlipDto m : out.getBetHistory()) {
            m.getLinks().add(toBetSlipLink(bettingId, m.getId()));
        }
        out.getLinks().addAll(baseLinks(bettingId));

        return Response.ok(out)
                .build();
    }

    @GET
    @Path("/{bettingId}/bet_slips")
    public Response getBetSlip(@PathParam("bettingId") Long bettingId, @QueryParam("bet_slip_id") Long slipId) {
        var out = serviceAdapter.getBetSlip(slipId);
        for (ReadMatchEventPickDto pick : out.getPicks()) {
            pick.getLinks().add(resource.updateOutComeLink(bettingId, slipId, pick));
        }
        out.getLinks().addAll(baseLinks(bettingId));

        return Response.ok(out)
                .build();
    }

    @GET
    @Path("/{bettingId}/transaction_history")
    public Response getTransactionHistory(@PathParam("bettingId") Long bettingId, @QueryParam("transactionType") TransactionType type) {
        var out = serviceAdapter.getTransactionHistory(bettingId, type);
        addTransactionLinks(out.getTransactionHistory());
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
    @Path("/{bettingId}/withdraw_to_momo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdrawToMomo(@PathParam("bettingId") Long bettingId, @QueryParam("momoId") Long momoId, @Valid WithdrawDto dto) {
        if (systemTime) {
            dto.setTransactionTime(null);
        } else {
            Objects.requireNonNull(dto.getTransactionTime(),"if system time is set to false then you have to input transaction time");
        }
        serviceAdapter.withdrawFromBettingToMobileMoney(bettingId, momoId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(mobileMoneyResources.getAccount(momoId));
        return Response.ok(out).build();
    }

    /**
     * adds pick to an empty slip on an account
     */
    @GET
    @Path("/{bettingId}/draft_slip")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getDraft(@PathParam("bettingId") Long bettingId) {
        var out = serviceAdapter.getDraftSlip(bettingId);
        out.addLink(addPickToDraftLink(bettingId));
        out.getLinks().addAll(baseLinks(bettingId));
        out.addLink(emptyDraftSlipLink(bettingId));
        out.addLink(removePick(bettingId));
        out.addLink(placeBetLink(bettingId));
        return Response.ok(out).build();
    }

    /**
     * adds pick to an empty slip on an account
     */
    @PUT
    @Path("/{bettingId}/draft_slip/add_pick")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPick(@PathParam("bettingId") Long bettingId, @Valid AddPickRequestBetSlipDto dto) {
        serviceAdapter.addPickToBetSlip(bettingId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(placeBetLink(bettingId));
        out.add(removePick(bettingId));
        out.add(linkFactory("betting-accounts/" + bettingId + "betslips/add-pick", "add pick to draft slip", "PUT"));
        return Response.ok(out).build();
    }

    /**
     * removes pick to an draft slip on an account
     */
    @PUT
    @Path("/{bettingId}/draft_slip/remove_pick")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removePick(@PathParam("bettingId") Long bettingId, @QueryParam("pickIndex") Integer pickIndex) {
        serviceAdapter.removePickByNumber(bettingId, pickIndex);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(placeBetLink(bettingId));
        out.add(removePick(bettingId));
        out.add(addPickToDraftLink(bettingId));
        return Response.ok(out).build();
    }

    /**
     * creates a new empty draft slip in the betting
     */
    @PUT
    @Path("/{bettingId}/empty_slip")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creatEmptyDraft(@PathParam("bettingId") Long bettingId) {
        serviceAdapter.creatEmptyDraftSlip(bettingId);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        out.add(addPickToDraftLink(bettingId));
        return Response.status(Response.Status.CREATED)
                .entity(out).build();
    }

    @PUT
    @Path("/{bettingId}/bet_slips/make_bet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makeBet(@PathParam("bettingId") Long bettingId, @Valid MakeBetRequestDto dto) {
        serviceAdapter.makeBet(bettingId, dto);
        var out = new ArrayList<>(baseLinks(bettingId));
        out.add(emptyDraftSlipLink(bettingId));
        return Response.ok(out).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/create_bonus")
    public Response createBonus(@PathParam("id") Long id, @Valid BonusDto dto) {
        serviceAdapter.createBonus(id, dto);
        var out = new ArrayList<>(baseLinks(id));
        return Response.ok(out).build();
    }


    @PUT
    @Path("/{bettingId}/refund/slipId")
    public Response setSlipToRefund(@PathParam("bettingId") Long bettingId, @QueryParam("slipId") Long slipId) {
        serviceAdapter.setSlipToRefund(bettingId, slipId);
        var out = new ArrayList<>(baseLinks(bettingId));
        return Response.ok(out).build();
    }

    private Response getAllBettingAccountsResponse(BrokerType broker) {
        final var getAllResponse = this.serviceAdapter.getAllBettingAccounts(broker);
        setSelfLinks(getAllResponse.bettingAccounts());
        getAllResponse.links().add(getAllAccountsLinks());
        getAllResponse.links().add(createBettingAccount());
        getAllResponse.links().add(new Link(uriInfo.getBaseUri() + "personal-betting-system", "dipatcher", "GET"));
        return Response.ok(getAllResponse)
                .build();
    }

    private void setSelfLinks(List<GetBettingAccountDto> bettingAccount) {
        for (GetBettingAccountDto dto : bettingAccount) {
            dto.getSelfLinks().add(new Link(createSelfLink(dto.getId()), "self", "GET"));
        }
    }

    private void addTransactionLinks(List<TransactionDto> transactionHistory) {
        for (TransactionDto t : transactionHistory) {
            if (t.getType() == TransactionType.BET_PLACED || t.getType() == TransactionType.BET_REFUNDED || t.getType() == TransactionType.BET_WON)
                t.getLinks().add(toBetSlipLink(t.getOwnerId(), t.getBetSlipId()));
        }
    }

    private String createSelfLink(Long id) {
        return uriInfo.getRequestUriBuilder().path(Long.toString(id)).build().toString();
    }

    private Link linkFactory(String baseEnding, String rel, String type) {
        return new Link(uriInfo.getBaseUri() + "betting_accounts" + baseEnding, rel, type);
    }

    private Link createBettingAccount() {
        return linkFactory("", " create betting account", "POST");

    }

    protected Link toBettingAccount(Long id) {
        return linkFactory("/" + id.toString(), " get betting account", "GET");
    }

    private Link toBetHistoryLink(Long id) {
        return linkFactory("/" + id + "/bet_history?strategy&matchKey&status", " get betting account history", "GET");
    }

    private Link toTransactionHistory(Long id) {
        return linkFactory("/" + id + "/transaction_history?transactionType=", " get transaction account history", "GET");
    }

    private Link toBetSlipLink(Long bettingAccount, Long slipId) {
        return linkFactory("/" + bettingAccount + "/bet_slips?bet-slip-id=" + slipId, " getBetslip", "GET");
    }

    private Link doWithdrawalLink(Long id) {
        return linkFactory("/" + id + "/withdraw_to_momo?momoId=", " withdraw from 1xbet to momo", "PUT");
    }

    private Link doDepositLink(Long id) {
        return linkFactory("/" + id + "/deposit_to_betting", " deposit to 1xbet from momo", "PUT");
    }

    protected Link getAllAccountsLinks() {
        this.systemTime = systemTime;
        return linkFactory("", "get all betting accounts", "GET");
    }

    protected void setSystemTime(Boolean systemTime) {
        this.systemTime = systemTime;
    }

    private Link getDraftSlipLink(Long id) {
        return linkFactory("/" + id + "/draft_slip", "get draft slip of account " + id, "GET");
    }

    protected Link emptyDraftSlipLink(Long bettingId) {
        return linkFactory("/" + bettingId + "/empty_slip", "empty draft slip", "PUT");
    }


    private List<Link> baseLinks(Long id) {
        List<Link> out = new ArrayList<>();
        out.add(toBettingAccount(id));
        out.add(toTransactionHistory(id));
        out.add(toBetHistoryLink(id));
        out.add(doWithdrawalLink(id));
        out.add(doDepositLink(id));
        out.add(getDraftSlipLink(id));
        return out;
    }

    private Link addPickToDraftLink(Long id) {
        return linkFactory("/" + id + "/draft_slip/add_pick", "add pick to draft slip", "PUT");

    }

    private Link removePick(Long id) {
        return linkFactory("/" + id + "/draft_slip/remove_pick", "remove pick to draft slip", "PUT");

    }

    private Link placeBetLink(Long id) {
        return linkFactory("/" + id + "/bet_slips/make_bet", "place bet", "PUT");
    }
}
