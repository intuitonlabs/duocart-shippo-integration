@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
public class Order {

    // Add proparty for shipments in the Order.java class
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Shipment shipment;



    // Add method for adding shipment
    public void addShipment(Shipment shipment) {
        if(Objects.nonNull(shipment)) {
            shipment.setOrder(this);
            this.shipment = shipment;
        }
    }
}