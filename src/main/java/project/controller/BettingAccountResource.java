package project.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.BettinAccountDTO.BettingAccountDto;
import project.adapter.in.web.BettingServiceAdapter;

import java.net.URI;

@Path("/betting-accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BettingAccountResource {

    @Inject
    BettingServiceAdapter serviceAdapter;

    @POST
    public Response create(BettingAccountDto dto) {
        Long id = serviceAdapter.createNewBettingAccount(dto);

        // Either return created location only
        return Response.created(URI.create("/betting-accounts/" + id)).build();

        // Or if you prefer returning JSON: return Response.status(201).entity(Map.of("id", id)).build();
    }
}
