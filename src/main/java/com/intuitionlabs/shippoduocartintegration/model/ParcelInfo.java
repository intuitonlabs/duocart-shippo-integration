package com.intuitionlabs.shippoduocartintegration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ParcelInfo {

    private String length;
    private String width;
    private String height;
    private String distance_unit;
    private String weight;
    private String mass_unit;

}
