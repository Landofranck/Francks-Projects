package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.TransactionDTO.DepositDto;
import project.application.port.in.MakeDepositUseCase;

@Path("/mobile-money-accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DepositResource {

    @Inject
    MakeDepositUseCase makeDepositUseCase;

    @POST
    @Path("/{momoId}/deposit-to-betting/{bettingId}")
    public Response deposit(@PathParam("momoId") Long momoId,
                            @PathParam("bettingId") Long bettingId,
                            DepositDto dto) {

        makeDepositUseCase.depositFromMobileMoneyToBettingAccount(momoId, bettingId, dto.getAmount());
        return Response.ok().build();
    }
}
