package com.voshodnerd.helidon.service;

import com.voshodnerd.helidon.exception.NoCampaignException;
import com.voshodnerd.helidon.model.Campaign;
import com.voshodnerd.helidon.model.PromoCode;
import com.voshodnerd.helidon.model.dto.CampaignRecord;
import com.voshodnerd.helidon.repository.CampaignRepository;
import com.voshodnerd.helidon.repository.PromoCodeRepository;
import com.voshodnerd.helidon.utils.CsvReader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PromoCodeService {

    private static final Logger LOGGER = Logger.getLogger(PromoCodeService.class.getName());

    private final CampaignRepository campaignRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Inject
    public PromoCodeService(CampaignRepository campaignRepository, PromoCodeRepository promoCodeRepository) {
        this.campaignRepository = campaignRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Transactional
    public Optional<PromoCode> issuePromoCode(String phoneNumber) {
        var existingPromoCode = promoCodeRepository.findFirstByIssuedBy(phoneNumber);
        if (existingPromoCode.isEmpty()) {
            var promoCode = promoCodeRepository.findFirstByStatus(PromoCode.PromoCodeStatus.ACTIVE);
            if (promoCode.isEmpty()) return Optional.empty();
            var promoCodeEntity = promoCode.get();
            promoCodeEntity.setStatus(PromoCode.PromoCodeStatus.USED);
            promoCodeEntity.setIssuedBy(phoneNumber);
            promoCodeRepository.save(promoCodeEntity);
            return promoCode;
        }
        return existingPromoCode;
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


    public boolean uploadPromoCodeFile(InputStream stream, Long campaignId) throws NoCampaignException {
        // Parse the CSV file
        var uploadList = new ArrayList<String>();
        try {
            uploadList = (ArrayList<String>) CsvReader.parseCsv(stream);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse CSV file ");
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

        promoCodeRepository.saveAll(promocodeList);
        return true;
    }

    private InputStream convertMultiPartToInputStream(MultiPart multiPart) {
        // Assuming the first body part contains the data you want
        BodyPart bodyPart = multiPart.getBodyParts().getFirst();

        // Check if the body part contains the expected media type
        if (bodyPart.getMediaType().equals(MediaType.APPLICATION_OCTET_STREAM_TYPE)) {
            // Get the entity as an InputStream
            return bodyPart.getEntityAs(InputStream.class);
        } else {
            // Handle other media types or throw an exception
            throw new IllegalArgumentException("Unsupported media type");
        }
    }
}