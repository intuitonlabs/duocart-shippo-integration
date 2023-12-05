package com.intuitionlabs.shippoduocartintegration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shipments")
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carrier;
    private String trackingNumber;
    private String labelUrl;
    private String trackingUrl;

    @OneToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_shipment_id"))
    @JsonIgnore
    private Order order;

}
