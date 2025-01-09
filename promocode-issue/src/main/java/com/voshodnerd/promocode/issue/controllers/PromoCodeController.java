package com.voshodnerd.promocode.issue.controllers;

import com.voshodnerd.promocode.issue.model.dto.CampaignRecord;
import com.voshodnerd.promocode.issue.model.dto.PromoCodeDTO;
import com.voshodnerd.promocode.issue.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<CampaignRecord> createCampaign(@RequestBody CampaignRecord campaignRecord) {
        var campaign = promoCodeService.createCampaign(campaignRecord);
        return new ResponseEntity<>(new CampaignRecord(campaign.getId(),
                campaign.getName(),
                campaign.getDescription(),
                campaign.getStartDate(),
                campaign.getEndDate()), HttpStatus.CREATED);
    }

    @GetMapping("/campaign-list")
    public ResponseEntity<List<CampaignRecord>> listCampaigns() {
        return ResponseEntity.ok(promoCodeService.getCampaigns());
    }

    @GetMapping("/issue-promo-code")
    public ResponseEntity<PromoCodeDTO> issuePromoCode() {
        var promoCode = promoCodeService.issuePromoCode();
        return promoCode.map(code -> ResponseEntity.ok(new PromoCodeDTO(code.getValue())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


