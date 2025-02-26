package com.voshodnerd.helidon.controller;

import com.voshodnerd.helidon.model.dto.CampaignRecord;
import com.voshodnerd.helidon.service.PromoCodeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @Inject
    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCampaign(CampaignRecord campaignRecord) {
        var campaign = promoCodeService.createCampaign(campaignRecord);
        return Response.ok().entity(new CampaignRecord(campaign.getId(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getStartDate(),
                campaign.getEndDate())).build();

    }

    @GET
    @Path("/campaign-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaigns() {
        return Response.ok().entity(promoCodeService.getCampaigns()).build();
    }


    @GET
    @Path("/issue-promo-code")
    @Produces(MediaType.APPLICATION_JSON)
    public Response issuePromoCode() {
        var promoCode = promoCodeService.issuePromoCode();
        return promoCode.map(v -> Response.status(Response.Status.OK)  // Set the HTTP status to 200 OK
                .entity(v.getValue())   // Set the body of the response
                .build()).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

}
