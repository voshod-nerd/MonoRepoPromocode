package com.voshodnerd.quarkus.controller;


import com.voshodnerd.quarkus.exception.NoCampaignException;
import com.voshodnerd.quarkus.service.PromoCodeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@RequestScoped
@Path("/upload")
public class UploadPromoCodeController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadPromoCodeController.class);

    private PromoCodeService promoCodeService;

    @Inject
    public UploadPromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPromoCodeFile(
            @FormParam("filename")
            @PartType(MediaType.APPLICATION_OCTET_STREAM) InputStream file,
            @RestForm("campaignId") @PartType(MediaType.TEXT_PLAIN) Long campaignId) throws NoCampaignException {
        if (promoCodeService.uploadPromoCodeFile(file, campaignId)) {
            return Response.ok()
                    .entity("Загружено")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Ошибка").build();
    }


}
