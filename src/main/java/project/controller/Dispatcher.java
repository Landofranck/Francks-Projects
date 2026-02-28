package project.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import project.adapter.in.web.Utils.Link;

import java.util.ArrayList;
import java.util.List;

@Path("/personal_betting_system")
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
    @Inject
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        List<Link> out = new ArrayList<>();
        out.add(bettingAccountResource.getAllAccountsLinks());
        out.add(mobileMoneyAccountResource.getAllMomoLink());
        out.add(reducerResource.getAllReducersLink());
        out.add(matchResource.getAllMatchesLink());
        out.add(new Link(uriInfo.getBaseUri()+"personal_betting_system/set_to_system_time?system_time=","set system time","PUT"));
        return Response.ok(out).build();
    }

    @PUT
    @Path("/set_to_system_time")
    public Response setSytemTime(@QueryParam("system_time") Boolean systemTime) {
        List<Link> out = new ArrayList<>();
        out.add(new Link(uriInfo.getBaseUri()+"personal_betting_system","dispatcher","GET"));
        bettingAccountResource.setSystemTime(systemTime);
        mobileMoneyAccountResource.setSystemTime(systemTime);
       reducerResource.setSytemTime(systemTime);
        matchResource.getAllMatchesLink();
        return Response.ok(out).build();
    }

}
