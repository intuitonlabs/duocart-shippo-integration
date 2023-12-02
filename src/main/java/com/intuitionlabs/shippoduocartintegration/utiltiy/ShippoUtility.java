package com.intuitionlabs.shippoduocartintegration.utiltiy;

import com.intuitionlabs.shippoduocartintegration.exceptions.InvalidAddressException;
import com.intuitionlabs.shippoduocartintegration.exceptions.TransactionFailException;
import com.intuitionlabs.shippoduocartintegration.model.ParcelInfo;
import com.intuitionlabs.shippoduocartintegration.model.ShippoAddress;
import com.intuitionlabs.shippoduocartintegration.model.ShippoTrackingInformation;
import com.shippo.Shippo;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import com.shippo.model.*;

import java.util.*;

public class ShippoUtility {

    private static final String API_KEY = System.getenv("SHIPPO_API_KEY");
    private static final String API_VERSION = "2018-02-08";
    private static final String SUCCESS_SHIPMENT_RESPONSE_TEXT = "SUCCESS";

    public static void setApi() {
        Shippo.setApiKey(API_KEY);
        Shippo.setApiVersion(API_VERSION);
    }

    public static Double getShippingRate(ShippoAddress toAddress, ShippoAddress fromAddress, ParcelInfo parcelInfo) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        setApi();

        com.shippo.model.Address toAddressShippo = createToAddress(toAddress);
        throwInvalidAddressException(toAddressShippo);
        com.shippo.model.Address fromAddressShippo = createFromAddress(fromAddress);
        throwInvalidAddressException(fromAddressShippo);

        List<Map<String, Object>> parcel = createParcel(parcelInfo);

        Shipment shipment = createShipment(fromAddressShippo, toAddressShippo, parcel);
        RateCollection rateCollection = createRateCollection(shipment);
        Collections.sort(rateCollection.getData(), Comparator.comparingDouble((rate) -> Double.parseDouble((String)rate.getAmount())));

        return Double.parseDouble((String)rateCollection.getData().get(0).getAmount());
    }


    public static ShippoTrackingInformation initShipping(ShippoAddress toAddress, ShippoAddress fromAddress, ParcelInfo parcelInfo, Double foundRate) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        setApi();

        com.shippo.model.Address toAddressShippo = createToAddress(toAddress);
        com.shippo.model.Address fromAddressShippo = createFromAddress(fromAddress);

        List<Map<String, Object>> parcel = createParcel(parcelInfo);
        Shipment shipment = createShipment(fromAddressShippo, toAddressShippo, parcel);
        Rate rates = getRate(shipment, foundRate);

        return createTransactionReturnTrackingInfo(rates);
    }

    public static com.shippo.model.Address createToAddress(ShippoAddress address) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Map<String, Object> fromAddressMap = new HashMap<String, Object>();
        fromAddressMap.put("name", address.getName());
        fromAddressMap.put("street1", address.getStreet1());
        fromAddressMap.put("street2", address.getStreet2());
        fromAddressMap.put("city", address.getCity());
        fromAddressMap.put("state", address.getState());
        fromAddressMap.put("zip", address.getZipCode());
        fromAddressMap.put("country", address.getCountry());
        fromAddressMap.put("email", address.getEmail());
        fromAddressMap.put("phone", address.getPhone());
        fromAddressMap.put("validate", true);

        return com.shippo.model.Address.create(fromAddressMap);
    }

    public static com.shippo.model.Address createFromAddress(ShippoAddress address) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Map<String, Object> fromAddressMap = new HashMap<String, Object>();
        fromAddressMap.put("name", address.getName());
        fromAddressMap.put("company", address.getCompany());
        fromAddressMap.put("street1", address.getStreet1());
        fromAddressMap.put("street2", address.getStreet2());
        fromAddressMap.put("city", address.getCity());
        fromAddressMap.put("state", address.getState());
        fromAddressMap.put("zip", address.getZipCode());
        fromAddressMap.put("country", address.getCountry());
        fromAddressMap.put("email", address.getEmail());
        fromAddressMap.put("phone", address.getPhone());
        fromAddressMap.put("validate", true);

        return com.shippo.model.Address.create(fromAddressMap);
    }

    public static List<Map<String, Object>> createParcel(ParcelInfo parcelInfo) {
        Map<String, Object> parcelMap = new HashMap<>();
        parcelMap.put("length", parcelInfo.getLength());
        parcelMap.put("width", parcelInfo.getWidth());
        parcelMap.put("height", parcelInfo.getHeight());
        parcelMap.put("distance_unit", parcelInfo.getDistance_unit());
        parcelMap.put("weight", parcelInfo.getWeight());
        parcelMap.put("mass_unit", parcelInfo.getMass_unit());

        return List.of(parcelMap);
    }

    public static Shipment createShipment(com.shippo.model.Address addressFrom, com.shippo.model.Address addressTo, List<Map<String, Object>> parcels) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Map<String, Object> shipmentMap = new HashMap<String, Object>();
        shipmentMap.put("address_to", addressTo);
        shipmentMap.put("address_from", addressFrom);
        shipmentMap.put("parcels", parcels);
        shipmentMap.put("async", false);

        return Shipment.create(shipmentMap);
    }

    public static RateCollection createRateCollection(Shipment shipment) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Map<String, Object> rateMap = new HashMap<String, Object>();
        rateMap.put("id", shipment.getObjectId());
        rateMap.put("currency_code", "USD");
        rateMap.put("async", false);

        return Shipment.getShippingRates(rateMap);
    }

    public static Rate getRate(Shipment shipment, double foundRate) {
        return shipment.getRates().stream().filter(x -> Double.parseDouble((String) x.getAmount()) == foundRate).findFirst().get();
    }

    public static ShippoTrackingInformation createTransactionReturnTrackingInfo(Rate rate) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException {
        Map<String, Object> transParams = new HashMap<>();
        transParams.put("rate", rate.getObjectId());
        transParams.put("async", false);
        Transaction transaction = Transaction.create(transParams);

        if (transaction.getStatus().equals(SUCCESS_SHIPMENT_RESPONSE_TEXT)) {
            String shippingLabel = transaction.getLabelUrl().toString();
            String trackingNumber = transaction.getTrackingNumber().toString();

            return ShippoTrackingInformation.builder()
                    .trackingNumber(trackingNumber)
                    .carrier(rate.getProvider().toString())
                    .labelUrl(shippingLabel)
                    .build();

        } else {
            throw new TransactionFailException(String.format("The creating of transaction with the delivery company failed. Please try again. Info: %s", transaction.getMessages()));
        }
    }

    public static Exception throwInvalidAddressException(com.shippo.model.Address address) {
        if(!address.getValidationResults().getIsValid()) {
            throw new InvalidAddressException(String.format("The provided address is invalid. Please enter correct value. Info address zip %s", address.getZip()));
        } else {
            return null;
        }
    }


}
