package project.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.bettinAccountDTO.AddPickRequestBetSlipDto;
import project.adapter.in.web.bettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.bettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.TransactionDTO.WithdrawDto;
import project.adapter.in.web.bettinAccountDTO.betslip.BetSlipDto;
import project.adapter.in.web.bettinAccountDTO.betslip.MakeBetRequestDto;
import project.application.port.in.MakeWithdrawalUseCase;

import java.net.URI;
import java.util.List;

@Path("/betting-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BettingAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;
    @Inject
    MakeWithdrawalUseCase makeWithdrawalUseCase;

    //gets all betting accounts from database
    @GET
    public List<BettingAccountDto> getAll() {
        return serviceAdapter.getAllBettingAccounts();
    }

    @GET
    @Path("/{bettingId}")
    public BettingAccountDto getById(@PathParam("bettingId") Long bettingId) {
        return serviceAdapter.loadBettingAccount(bettingId);
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateBettingAccountDto dto) {
        Long id = serviceAdapter.createNewBettingAccount(dto);

        // Either return created location only
        return Response.status(Response.Status.CREATED).entity(id).build();

        // Or if you prefer returning JSON: return Response.status(201).entity(Map.of("id", id)).build();
    }

    /**
     * makes a withdrawal from a betting account to a designated mobile money account
     */
    @PUT
    @Path("/{bettingId}/withdraw-to-momo/{momoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdrawToMomo(@PathParam("bettingId") Long bettingId,
                                   @PathParam("momoId") Long momoId,
                                   WithdrawDto dto) {
        makeWithdrawalUseCase.withdrawFromBettingToMobileMoney(bettingId, momoId, dto.getAmount(), dto.getDescription());
        return Response.noContent().build();
    }


    /**
     * adds pick to an empty slip on an account
     */
    @PUT
    @Path("/{bettingId}/betslips/add-pick")
    @Consumes(MediaType.APPLICATION_JSON)
    public BetSlipDto addPick(@PathParam("bettingId") Long bettingId, AddPickRequestBetSlipDto dto) {
        return serviceAdapter.addPickToBetSlip(bettingId, dto);
    }

    @PUT
    @Path("/{bettingId}/betslips/make-bet")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makeBet(@PathParam("bettingId") Long bettingId, MakeBetRequestDto dto) {
        Long slipId = serviceAdapter.makeBet(bettingId, dto);
        return Response.status(201).entity(java.util.Map.of("betSlipId", slipId)).build();
    }


}
