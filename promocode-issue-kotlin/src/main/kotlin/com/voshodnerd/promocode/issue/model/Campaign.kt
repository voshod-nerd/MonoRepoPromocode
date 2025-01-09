package com.voshodnerd.promocode.issue.model;

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String = ""
    var description: String = ""

    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    var startDate: ZonedDateTime? = null

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    var endDate: ZonedDateTime? = null

    constructor() {
        this.id = null
        this.name = "Unknown"
        this.description = "Unknown"
        this.startDate = ZonedDateTime.now()
        this.endDate = ZonedDateTime.now()
    }

    constructor(name: String, description: String, startDate: ZonedDateTime?, endDate: ZonedDateTime?) {
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
    }


}
