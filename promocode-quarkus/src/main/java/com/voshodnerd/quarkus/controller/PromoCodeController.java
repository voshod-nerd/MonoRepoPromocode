package com.voshodnerd.quarkus.controller;

import com.voshodnerd.quarkus.service.PromoCodeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @Inject
    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GET
    @Path("/issue-promo-code/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response issuePromoCode(@PathParam("phone") String phone) {
        var promoCode = promoCodeService.issuePromoCode(phone);
        return promoCode.map(v -> Response.status(Response.Status.OK)  // Set the HTTP status to 200 OK
                .entity(v.getValue())   // Set the body of the response
                .build()).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

}
