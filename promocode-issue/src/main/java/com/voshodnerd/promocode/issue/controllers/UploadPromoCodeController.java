package com.voshodnerd.promocode.issue.controllers;


import com.voshodnerd.promocode.issue.exception.NoCampaignException;
import com.voshodnerd.promocode.issue.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadPromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<String> uploadPromoCodeFile(
            @RequestParam("filename") MultipartFile file,
            @RequestParam("campaignId") Long campaignId) throws NoCampaignException {

        if (promoCodeService.uploadPromoCodeFile(file, campaignId)) {
            return new ResponseEntity<>("Загружено", HttpStatus.OK);
        }
        return new ResponseEntity<>("Ошибка", HttpStatus.BAD_REQUEST);
    }
}