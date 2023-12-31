package com.intuitionlabs.dulocartshippointegration.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor

/**
 * Addresses are the locations a parcel is being shipped from and to. They represent company and residential places.
 * Among other things, you can use address objects to create shipments, calculate shipping rates, and purchase shipping labels.
 */
public class ShippoAddress {

    private String name;
    private String street1;
    private String street2;
    private String country;
    private String state;
    private String zipCode;
    private String city;
    private String phone;
    private String email;

    /**
     * shippo metadata is customer id with company are used only for sender
     */
    private String metadata;
    private String company;

}
