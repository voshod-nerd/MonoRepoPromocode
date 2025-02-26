package com.voshodnerd.helidon.controller;

import com.voshodnerd.helidon.exception.NoCampaignException;
import com.voshodnerd.helidon.service.PromoCodeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.MultiPart;


@RequestScoped
@Path("/upload")
public class UploadPromoCodeController {

    private final PromoCodeService promoCodeService;

    @Inject
    public UploadPromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPromoCodeFile(
            MultiPart multiPart,
            @PathParam(value = "poolId") Long campaignId) throws NoCampaignException {

        if (promoCodeService.uploadPromoCodeFile(multiPart, campaignId)) {
            return Response.ok().entity("Загружено").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Ошибка").build();
    }
}
