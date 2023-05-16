package com.example.active.business.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    @ApiModelProperty(notes = "Activity ID", example = "1")
    private Long id;
    @ApiModelProperty(notes = "Activity Title", example = "Lane Swim - Short Course")
    private String title;
    @ApiModelProperty(notes = "Activity Type", example = "Lane Swim")
    private String type;
    @ApiModelProperty(notes = "Activity Category", example = "Swimming")
    private String category;
    @ApiModelProperty(notes = "Activity Reservation URL", example = ".....")
    private String reservationURL;
    @ApiModelProperty(notes = "Activity Availability", example = "True")
    private Boolean isAvailable;
    @ApiModelProperty(notes = "Activity Start Time", example = "....")
    private LocalDateTime startTime;
    @ApiModelProperty(notes = "Activity End Time", example = "1")
    private LocalDateTime endTime;
    @ApiModelProperty(notes = "Minimum Age for Activity", example = "16")
    private Integer minAge;
    @ApiModelProperty(notes = "Maximum Age for Activity", example = "45")
    private Integer maxAge;
    @ApiModelProperty(notes = "Activity Status Update Time", example = "...")
    private LocalDateTime lastUpdated;
    @ApiModelProperty(notes = "Facility of the Activity", example = "Nepean Sportsplex")
    private FacilityDTO facility;
}