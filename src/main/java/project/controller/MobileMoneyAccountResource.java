package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.BettingAccount.BettingServiceAdapter;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.CreateMobileMoneyAccountDto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.MomoTransferRequestDto;
import project.adapter.in.web.MobilMoneyAccount.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.MobilMoneyAccount.MomoServiceAdapter;
import project.adapter.in.web.TransactionDTO.DepositDto;
import project.adapter.in.web.Utils.Link;
import project.application.port.in.MomoAccounts.MakeDepositUseCase;
import project.domain.model.Enums.TransactionType;

@Path("/mobile-money-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class MobileMoneyAccountResource {

    @Inject
    MomoServiceAdapter serviceAdapter;
    @Inject
    MakeDepositUseCase makeDepositUseCase;
    @Inject
    UriInfo uriInfo;
    private Boolean systemTime;

    /**
     * creates new mobile money account
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid CreateMobileMoneyAccountDto dto) {
        serviceAdapter.createNewMobileMoneyAccount(dto);
        var out=new ArrayList<>(baseLinks(dto.getId()));
        out.add(getAllMomoLink());
        return Response.ok(out).entity(getAllMomoLink()).build();
    }

    @POST
    @Path("/{momoId}/transfers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("momoId") Long momoId, @QueryParam("destinator") Long to, @Valid MomoTransferRequestDto dto) {
        if (systemTime) {
            dto.setTransactionTime(null);
        }else{
            Objects.requireNonNull(dto.getTransactionTime());
        }
        dto.fromAccountId = momoId;
        dto.toAccountId = to;
        serviceAdapter.transferMomo(dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMomoLink());
        out.add(getAccount(momoId));
        out.add(getAccount(to));
        return Response.ok(out).build();
    }

    /**
     * tops up the money in  mobilemoney account whithout taking from any externnal source, just increases the value
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/topups")

    public Response topUpAccount(@PathParam("id") Long id, @Valid MomoTopUpRequestDto dto) {
        if (systemTime) {
            dto.setTransactionTime(null);
        }else{
            Objects.requireNonNull(dto.getTransactionTime());
        }
        serviceAdapter.topUpMomo(id, dto);
        List<Link> out = new ArrayList<>();
        out.add(getAllMomoLink());
        out.add(getAccount(id));
        out.add(getTransactions(id));
        return Response.ok(out).build();
    }

    /**
     * returns all mobili money accounts to the user
     */
    @GET
    public Response getAll() {
        return getAllMomoAccountsResponse();
    }

    /**
     * returns list of all transactions in a momo account
     */
    @GET
    @Path("/{id}/get-history")
    public Response getMomoHistoryAll(@PathParam("id") Long momoId, @QueryParam("transactionType") TransactionType type) {
        var out = serviceAdapter.getMomoTransactions(momoId,type);
        out.links().add(getAccount(momoId));
        out.links().add(topUp(momoId));
        return Response.ok(out).build();
    }

    @GET
    @Path("/{momoId}")
    public Response getMomoAccount(@PathParam("momoId") Long momoId) {
        var out = serviceAdapter.getMomoAccountById(momoId);
        out.addLink(getAllMomoLink());
        out.getLinks().addAll(baseLinks(momoId));
        return Response.ok(out).build();
    }

    /**
     * makes a deposit from mobile money to betting account
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{momoId}/deposit-to-betting")
    public Response deposit(@PathParam("momoId") Long momoId,
                            @Valid DepositDto dto) {
        ArrayList<Link> out;
        out = new ArrayList<>(baseLinks(momoId));
        out.add(getAllMomoLink());
        out.add(new Link(uriInfo.getBaseUri() + "/betting-accounts/" + dto.getBettingAccountId(), "get BettingAccount", "GET"));
        if (systemTime) {
            dto.setTransactionTime(null);
        }else{
            Objects.requireNonNull(dto.getTransactionTime());
        }
        makeDepositUseCase.depositFromMobileMoneyToBettingAccount(momoId,dto.getTransactionTime(),dto.getBettingAccountId(), dto.getAmount(), dto.getDescription());
        return Response.ok(out).build();
    }

    private Link linkFactory(String baseEnding, String rel, String type) {
        return new Link(uriInfo.getBaseUri() + "mobile-money-accounts" + baseEnding, rel, type);
    }

    protected Link getAllMomoLink() {
        return linkFactory("", "get all momo accounts", "GET");
    }

    private Response getAllMomoAccountsResponse() {
        final var getAllResponse = this.serviceAdapter.getAllMomoAccounts();
        setSelfLinks(getAllResponse.allMomos());
        getAllResponse.links().add(createMomoAccount());
        getAllResponse.links().add(new Link(uriInfo.getBaseUri()+"personal-betting-system","dipatcher","GET"));
        return Response.ok(getAllResponse)
                .build();
    }

    private String createSelfLink(Long id) {
        return uriInfo.getRequestUriBuilder().path(Long.toString(id)).build().toString();
    }

    private void setSelfLinks(List<ReadMomoAccountDto> momoAccountDtos) {
        for (ReadMomoAccountDto dto : momoAccountDtos) {
            dto.getLinks().add(new Link(createSelfLink(dto.getId()), "self", "GET"));
        }
    }

    private Link createMomoAccount() {
        return linkFactory("", "create new momo account", "POST");
    }

    private Link topUp(Long id) {
        return linkFactory("/" + id + "/topups", "put money in momo account", "PUT");
    }

    private Link doTransfer(Long from) {
        return linkFactory("/" + from + "/transfers?destinator=", "transfer money in momo account", "PUT");
    }

    public Link getAccount(Long momoId) {
        return linkFactory("/" + momoId, "get momo account " + momoId, "GET");
    }

    private Link getTransactions(Long id) {
        return linkFactory("/" + id + "/get-history?trasactionType=", "get momo transaction history of " + id, "GET");
    }

    private List<Link> baseLinks(Long id) {
        List<Link> out = new ArrayList<>();
        out.add(topUp(id));
        out.add(doTransfer(id));
        out.add(getTransactions(id));
        return out;
    }

    public void setSystemTime(Boolean systemTime) {
        this.systemTime=systemTime;
    }
}
