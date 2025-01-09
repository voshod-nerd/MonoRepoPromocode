package com.voshodnerd.promocode.issue.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record CampaignRecord(Long id, String name, String description,@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")  ZonedDateTime startDate,@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC") ZonedDateTime endDate) {
}
