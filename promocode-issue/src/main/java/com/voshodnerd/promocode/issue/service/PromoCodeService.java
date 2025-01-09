package com.voshodnerd.promocode.issue.service;

import com.voshodnerd.promocode.issue.exception.NoCampaignException;
import com.voshodnerd.promocode.issue.model.Campaign;
import com.voshodnerd.promocode.issue.model.PromoCode;
import com.voshodnerd.promocode.issue.model.dto.CampaignRecord;
import com.voshodnerd.promocode.issue.repository.CampaignRepository;
import com.voshodnerd.promocode.issue.repository.PromoCodeRepository;
import com.voshodnerd.promocode.issue.utils.CsvReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promocodeRepository;
    private final CampaignRepository campaignRepository;

    @Transactional
    public Optional<PromoCode> issuePromoCode() {
        return promocodeRepository.findFirstByStatus(PromoCode.PromoCodeStatus.ACTIVE);
    }

    @Transactional
    public List<CampaignRecord> getCampaigns() {
        return campaignRepository.findAll().stream()
                .map(el -> new CampaignRecord(el.getId(),
                        el.getName(),
                        el.getDescription(),
                        el.getStartDate(),
                        el.getEndDate()))
                .toList();
    }

    @Transactional
    public Campaign createCampaign(CampaignRecord campaign) {
        Campaign campaignEntity = new Campaign();
        campaignEntity.setName(campaign.name());
        campaignEntity.setDescription(campaign.description());
        campaignEntity.setStartDate(campaign.startDate());
        campaignEntity.setEndDate(campaign.endDate());
        return campaignRepository.save(campaignEntity);
    }


    public boolean uploadPromoCodeFile(MultipartFile file, Long campaignId) throws NoCampaignException {
        // Parse the CSV file
        var uploadList = new ArrayList<String>();
        try {
            uploadList = (ArrayList) CsvReader.parseCsv(file.getInputStream());
        } catch (Exception e) {
            log.error("Failed to parse CSV file ");
        }
        if (uploadList.isEmpty()) return false;

        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(NoCampaignException::new);

        var promocodeList = uploadList.stream().map(promoCode -> {
            PromoCode promoCodeEntity = new PromoCode();
            promoCodeEntity.setValue(promoCode);
            promoCodeEntity.setStatus(PromoCode.PromoCodeStatus.ACTIVE);
            promoCodeEntity.setCampaign(campaign);
            return promoCodeEntity;
        }).toList();

        promocodeRepository.saveAll(promocodeList);
        return true;
    }
}
