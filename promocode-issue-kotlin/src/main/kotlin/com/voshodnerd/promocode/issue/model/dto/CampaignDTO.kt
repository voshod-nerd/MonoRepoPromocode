package com.voshodnerd.promocode.issue.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

data class CampaignDTO(
    val id: Long?,
    val name: String,
    val description: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC") val startDate: ZonedDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC") val endDate: ZonedDateTime?
)

