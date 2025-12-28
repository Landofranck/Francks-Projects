package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.MobileMoneyDto.CreateMobileMoneyAccountDto;


import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import project.adapter.in.web.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobileMoneyDto.MomoTransferRequestDto;
import project.adapter.in.web.MobileMoneyDto.ReadMomoAccountDto;
import project.adapter.in.web.TransactionDTO.DepositDto;
import project.application.port.in.MakeDepositUseCase;

@Path("/mobile-money-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class MobileMoneyAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;
    @Inject
    MakeDepositUseCase makeDepositUseCase;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateMobileMoneyAccountDto dto) {
        Long id = serviceAdapter.createNewMobileMoneyAccount(dto.getId(), dto);
        return Response.created(URI.create("/mobile-money-accounts/" + id)).build();
    }

    @POST
    @Path("/transfers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid MomoTransferRequestDto dto) {
        serviceAdapter.transferMomo(dto);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/topups")

    public Response topUp(@PathParam("id") Long id, @Valid MomoTopUpRequestDto dto) {
        serviceAdapter.topUpMomo(id, dto);
        return Response.noContent().build();
    }

    @GET
    public List<ReadMomoAccountDto> getAll() {
        return serviceAdapter.getAllMomoAccounts();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{momoId}/deposit-to-betting")
    public Response deposit(@PathParam("momoId") Long momoId,
                            DepositDto dto) {

        makeDepositUseCase.depositFromMobileMoneyToBettingAccount(momoId, dto.getBettingAccountId(), dto.getAmount(), dto.getDescription());
        return Response.ok().build();
    }

}
