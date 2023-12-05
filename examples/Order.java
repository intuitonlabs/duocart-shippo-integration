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
    private Shipment shipments;



    // Add method for adding shipment
    public void addShippments(Shippment shippment) {
        if(Objects.nonNull(shippment)) {
            shippment.setOrder(this);
            this.shippment = shippment;
        }
    }
}