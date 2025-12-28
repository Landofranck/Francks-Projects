package project.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.BettinAccountDTO.CreateBettingAccountDto;
import project.adapter.in.web.BettingServiceAdapter;

import java.net.URI;
import java.util.List;

@Path("/betting-accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BettingAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;
    @GET
    public List<BettingAccountDto> getAll() {
        return serviceAdapter.getAllBettingAccounts();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreateBettingAccountDto dto) {
        Long id = serviceAdapter.createNewBettingAccount(dto);

        // Either return created location only
        return Response.created(URI.create("/betting-accounts/" + id)).build();

        // Or if you prefer returning JSON: return Response.status(201).entity(Map.of("id", id)).build();
    }
}
