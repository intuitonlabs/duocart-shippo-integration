package com.intuitionlabs.shippoduocartintegration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShippoTrackingInformation {

    private String carrier;
    private String trackingNumber;
    private String labelUrl;
}
