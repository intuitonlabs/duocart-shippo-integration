
import static com.intuitionlabs.shippoduocartintegration.utiltiy.ShippoUtility.getShippingRate;
import static com.intuitionlabs.shippoduocartintegration.utiltiy.ShippoUtility.initShipping;
import static com.intuitionlabs.shippoduocartintegration.utiltiy.ParcelUtility.getParcelInfo;

public class CheckoutController {

    // Add CompanyInfoService and Address converter to set the outbound address and destination address
    private final CompanyInfoService companyInfoService;
    private final AddressConverter addressConverter;

    public CheckoutController(CompanyInfoService companyInfoService, AddressConverter addressConverter) {
        this.companyInfoService = companyInfoService;
        this.addressConverter = addressConverter;
    }

    // Following changes need to be applied to getCustomerCheckOut() endpoint
    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CheckoutDto> getCustomerCheckOut(@PathVariable("customerId") Long id)  {
        Checkout checkout = service.findByCustomerId(id);

        // 1. Get company info for outbound address
        CompanyInfo companyInfo = companyInfoService.findById(1L);

        Optional<Address> primaryAddress = checkout.getCustomer().getShippingAddresses().stream().filter(x -> x.getType().equals(AddressType.SHIPPING) && x.isPrimary()).findFirst();
        primaryAddress.ifPresent(checkout::setShippingAddress);

        // 2. Get cart weight based on all products in cart
        double cartWeight = checkout.getCart().getWeight();

        // 3. Get shipping rate from Shippo based on parcel dimensions and weight
        Double shippingRate = getShippingRate(addressConverter.shippoAddressFrom(companyInfo), addressConverter.shippoAddressTo(checkout.getShippingAddress()), getParcelsInfo(cartWeight));
        // 4. Set the delivery cost
        checkout.setDelivery(shippingRate);
        Checkout saved = service.save(checkout);

        return ResponseEntity.ok(converter.toCheckoutDto(saved));
    }

    // Following changes need to be applied to submitOrder() endpoint
    @PatchMapping(value = "/submit/{id}")
    public ResponseEntity<HttpStatus> submitOrder(@PathVariable("id") Long id) {
        Checkout checkout = service.findById(id);
        CompanyInfo companyInfo = companyInfoService.findById(1L);
        Order order = orderConverter.toOrderFromCheckout(checkout);
        decreaseProductQuantityWhenAddToOrder(order.getProducts(), productService);

        // 1. Get cart weight based on all products in cart
        double cartWeight = checkout.getCart().getWeight();

        // 3. Create parcel and shipment with Shippo
        ShippoTrackingInformation trackingInformation = initShipping(addressConverter.shippoAddressTo(checkout.getShippingAddress()),  addressConverter.shippoAddressFrom(companyInfo), getParcelsInfo(cartWeight), order.getDeliveryCost());

        // 4. Set the returned information from the shipment and add it to the order
        Shipment shipment = Shipment.builder()
                .trackingUrl(trackingInformation.getTrackingUrl())
                .trackingNumber(trackingInformation.getTrackingNumber())
                .carrier(trackingInformation.getCarrier())
                .labelUrl(trackingInformation.getLabelUrl())
                .build();

        order.addShipment(shipment);
        orderService.save(order);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}