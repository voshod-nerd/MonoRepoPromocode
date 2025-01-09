package com.voshodnerd.promocode.issue.service;

import com.voshodnerd.promocode.issue.exception.NoCampaignException;
import com.voshodnerd.promocode.issue.model.Campaign;
import com.voshodnerd.promocode.issue.model.PromoCode;
import com.voshodnerd.promocode.issue.model.dto.CampaignDTO;
import com.voshodnerd.promocode.issue.repository.CampaignRepository;
import com.voshodnerd.promocode.issue.repository.PromoCodeRepository;
import com.voshodnerd.promocode.issue.utils.CsvReader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile


@Service
class PromoCodeService @Autowired constructor(
    var promoCodeRepository: PromoCodeRepository,
    var campaignRepository: CampaignRepository
) {


    @Transactional
    fun issuePromoCode(): PromoCode? {
        return promoCodeRepository.findFirstByStatus(PromoCode.PromoCodeStatus.ACTIVE)
    }

    @Transactional
    fun getCampaigns(): MutableList<CampaignDTO> {
        return campaignRepository.findAll().stream()
            .map { el ->
                CampaignDTO(
                    el.id,
                    el.name,
                    el.description,
                    el.startDate,
                    el.endDate
                )
            }.toList()
    }

    @Transactional
    fun createCampaign(campaign: CampaignDTO): Campaign {
        val campaignEntity = Campaign()
        campaignEntity.name = campaign.name
        campaignEntity.description = campaign.description
        campaignEntity.startDate = campaign.startDate
        campaignEntity.endDate = campaign.endDate
        return campaignRepository.save(campaignEntity)
    }


    fun uploadPromoCodeFile(file: MultipartFile, campaignId: Long): Boolean {
        // Parse the CSV file
        var uploadList = ArrayList<String>();
        try {
            uploadList = CsvReader.parseCsv(file.getInputStream()) as ArrayList<String>;
        } catch (e: Exception) {
            println("Failed to parse CSV file");
        }
        if (uploadList.isEmpty()) return false;

        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow { NoCampaignException() }

        val promoCodeList = uploadList.stream().map { promoCode ->
            {
                PromoCode(promoCode, PromoCode.PromoCodeStatus.ACTIVE, campaign)
            }
        }.map { it() }.toList()
        promoCodeRepository.saveAll(promoCodeList)
        return true;
    }
}
