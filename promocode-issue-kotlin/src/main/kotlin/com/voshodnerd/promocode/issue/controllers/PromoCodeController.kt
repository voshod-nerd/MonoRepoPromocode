package com.voshodnerd.promocode.issue.controllers;

import com.voshodnerd.promocode.issue.model.dto.CampaignDTO;
import com.voshodnerd.promocode.issue.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PromoCodeController @Autowired constructor (private val promoCodeService: PromoCodeService) {

    @PostMapping
    fun createCampaign(@RequestBody campaignRecord: CampaignDTO): ResponseEntity<CampaignDTO> {
        var campaign = promoCodeService.createCampaign(campaignRecord);
        return ResponseEntity.ok(
            CampaignDTO(
                campaign.id, campaign.name, campaign.description,
                campaign.endDate,
                campaign.endDate
            )
        )
    }

    @GetMapping("/campaign-list")
    fun listCampaigns(): ResponseEntity<List<CampaignDTO>> {
        return ResponseEntity.ok(promoCodeService.getCampaigns());
    }

    @GetMapping("/issue-promo-code")
    fun issuePromoCode(): Any {
        val promoCode = promoCodeService.issuePromoCode() ?: return ResponseEntity.notFound();
        return ResponseEntity.ok(promoCode.value);

    }
}


