package com.voshodnerd.quarkus.controller;

import com.voshodnerd.quarkus.model.dto.CampaignRecord;
import com.voshodnerd.quarkus.service.PromoCodeService;
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
@Path("/campaign")
public class CampaignController {

    private final PromoCodeService promoCodeService;

    @Inject
    public CampaignController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GET
    @Path("/campaign-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaigns() {
        return Response.ok().entity(promoCodeService.getCampaigns()).build();
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
}
