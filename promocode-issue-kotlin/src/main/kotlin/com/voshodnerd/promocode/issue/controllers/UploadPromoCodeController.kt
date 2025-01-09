package com.voshodnerd.promocode.issue.controllers;



import com.voshodnerd.promocode.issue.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
class UploadPromoCodeController @Autowired constructor (private val promoCodeService: PromoCodeService) {


    @PostMapping(
        consumes = ["multipart/form-data"],
        produces = ["application/json"]
    )
    fun uploadPromoCodeFile(
        @RequestParam("filename") file: MultipartFile,
        @RequestParam("campaignId") campaignId: Long
    ): ResponseEntity<String> {
        if (promoCodeService.uploadPromoCodeFile(file, campaignId)) {
            return ResponseEntity.ok("Загружено");
        }
        return ResponseEntity.badRequest().body("Ошибка")
    }
}