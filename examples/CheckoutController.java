
import static com.intuitionlabs.dulocartshippointegration.utiltiy.ShippoUtility.getShippingRate;
import static com.intuitionlabs.dulocartshippointegration.utiltiy.ShippoUtility.initShipping;
import static com.intuitionlabs.dulocartshippointegration.utiltiy.ParcelUtility.getParcelInfo;

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
    public ResponseEntity<CheckoutDto> getCustomerCheckOut(@PathVariable("customerId") Long id) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Checkout checkout = service.findByCustomerId(id);
        getShippingRate(checkout);

        Checkout saved = service.save(checkout);

        return ResponseEntity.ok(converter.toCheckoutDto(saved));
    }

    @PatchMapping(value = "/{id}/{addressId}")
    public ResponseEntity<HttpStatus> updateShippingAddress(@PathVariable("id") Long id, @PathVariable Long addressId) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Checkout checkout = service.findByCustomerId(id);
        Address foundAddress = addressService.findById(addressId);
        foundAddress.setPrimary(true);
        addressService.save(foundAddress);

        getShippingRate(checkout);

        service.save(checkout);

        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    // Following changes need to be applied to submitOrder() endpoint
    @PatchMapping(value = "/submit/{id}")
    public ResponseEntity<HttpStatus> submitOrder(@PathVariable("id") Long id) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Checkout checkout = service.findById(id);
        CompanyInfo companyInfo = companyInfoService.findFirstCompanyInfo();
        Order order = orderConverter.toOrderFromCheckout(checkout);
        decreaseProductQuantityWhenAddToOrder(order.getProducts(), productService);

        // 1. Get cart weight based on all products in cart
        double cartWeight = checkout.getCart().getWeight();

        // 3. Create parcel and shipment with Shippo
        ShippoTrackingInformation trackingInformation = initShipping(addressConverter.shippoAddressFrom(companyInfo), addressConverter.shippoAddressTo(checkout.getShippingAddress()), getParcelsInfo(cartWeight), getShippingRate(checkout));

        // 4. Set the returned information from the shipment and add it to the order
        Shipment shipment = Shipment.builder()
                .trackingUrl(trackingInformation.getTrackingUrl())
                .trackingNumber(trackingInformation.getTrackingNumber())
                .carrier(trackingInformation.getCarrier())
                .build();

        order.addShipment(shipment);
        orderService.save(order);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // Adding getShippingRate() method used in controllers
    private Rate getShippingRate(Checkout checkout) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        // 1. Get company info for outbound address
        CompanyInfo companyInfo = companyInfoService.findFirstCompanyInfo();

        Optional<Address> primaryAddress = checkout.getCustomer().getShippingAddresses().stream().filter(x -> x.getType().equals(AddressType.SHIPPING) && x.isPrimary()).findFirst();
        primaryAddress.ifPresent(checkout::setShippingAddress);

        // 2. Get cart weight based on all products in cart
        double cartWeight = checkout.getCart().getWeight();

        // 3. Get shipping rate from Shippo based on parcel dimensions and weight
        Rate shippingRate = ShippoUtility.getShippingRate(addressConverter.shippoAddressFrom(companyInfo), addressConverter.shippoAddressTo(checkout.getShippingAddress()), getParcelsInfo(cartWeight), companyInfo.getCurrency());

        // 4. Set the delivery cost. Used fixed if available in company info else use the rate from Shippo
        double deliveryCost = companyInfo.getFixedShippingRate() > 0.0 ? companyInfo.getFixedShippingRate() : Double.parseDouble((String) shippingRate.getAmountLocal());
        checkout.setDelivery(deliveryCost);

        return shippingRate;
    }

}
