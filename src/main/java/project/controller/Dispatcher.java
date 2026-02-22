package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import project.adapter.in.web.Utils.Link;

import java.util.ArrayList;
import java.util.List;

@Path("/personal-betting-system")
@Produces(MediaType.APPLICATION_JSON)
public class Dispatcher {
    @Inject
    BettingAccountResource bettingAccountResource;
    @Inject
    MatchResource matchResource;
    @Inject
    MobileMoneyAccountResource mobileMoneyAccountResource;
    @Inject
    ReducerResource reducerResource;

    @GET
    public Response getAll() {
        List<Link> out = new ArrayList<>();
        out.add(bettingAccountResource.getAllAccountsLinks());
        out.add(mobileMoneyAccountResource.getAllMomoLink());
        out.add(reducerResource.getAllReducersLink());
        out.add(matchResource.getAllMatchesLink());
        return Response.ok(out).build();
    }

}
