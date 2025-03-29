package com.voshodnerd.helidon.controller;

import com.voshodnerd.helidon.exception.NoCampaignException;
import com.voshodnerd.helidon.service.PromoCodeService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.MultiPart;

import java.io.InputStream;


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
            MultiPart multiPart) throws NoCampaignException {
        InputStream inputStream = null;
        Long campaignId = 0l;
        for (BodyPart part : multiPart.getBodyParts()) {
            if ("filename".equals(part.getContentDisposition()
                    .getParameters()
                    .get("name"))) {
                inputStream = part.getEntityAs(BodyPartEntity.class)
                        .getInputStream();
            }
            if ("campaignId".equals(part.getContentDisposition()
                    .getParameters()
                    .get("name"))) {
                campaignId = part.getEntityAs(Long.class);
            }
        }
        if (promoCodeService.uploadPromoCodeFile(inputStream, campaignId)) {
            return Response.ok()
                    .entity("Загружено")
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Ошибка")
                .build();
    }
}
