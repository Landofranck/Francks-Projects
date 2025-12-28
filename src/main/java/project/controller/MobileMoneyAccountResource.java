package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettingServiceAdapter;
import project.adapter.in.web.MobileMoneyAccountDto;

import java.net.URI;

@Path("/mobile-money-accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MobileMoneyAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;

    @POST
    public Response create(Long momoId, MobileMoneyAccountDto dto) {
        Long id = serviceAdapter.createNewMobileMoneyAccount(momoId, dto);
        return Response.created(URI.create("/mobile-money-accounts/" + id)).build();
    }
}
