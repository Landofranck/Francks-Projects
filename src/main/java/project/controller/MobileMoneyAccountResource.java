package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.MobileMoneyDto.MobileMoneyAccountDto;


import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import project.adapter.in.web.MobileMoneyDto.MomoTopUpRequestDto;
import project.adapter.in.web.MobileMoneyDto.MomoTransferRequestDto;

@Path("/mobile-money-accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MobileMoneyAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;

    @POST
    public Response create(MobileMoneyAccountDto dto) {
        Long id = serviceAdapter.createNewMobileMoneyAccount(dto.getId(), dto);
        return Response.created(URI.create("/mobile-money-accounts/" + id)).build();
    }

    @POST
    @Path("/transfers")
    public Response transfer(@Valid MomoTransferRequestDto dto) {
        serviceAdapter.transferMomo(dto);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/topups")
    public Response topUp(@PathParam("id") Long id, @Valid MomoTopUpRequestDto dto) {
        serviceAdapter.topUpMomo(id, dto);
        return Response.noContent().build();
    }


    @GET
    public List<MobileMoneyAccountDto> getAll() {
        return serviceAdapter.getAllMomoAccounts();
    }

}
