@Component
public class AddressConverter {


    // Add the folloing methods to the AddressConverter class in the Dulo card  project
    public ShippoAddressDto toShippoAddress(Address address) {
        ShippoAddressDto build = ShippoAddressDto.builder()
                .email(address.getCustomer().getEmail())
                .phone(address.getPhoneNumber())
                .name(address.getCustomer().getFullName())
                .country(address.getCountry())
                .street1(address.getAddress1())
                .street2(address.getAddress2())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .state(address.getState())
                .build();
        return build;
    }

    public ShippoAddressDto toFromShippoAddress(CompanyInfo companyInfo) {
        ShippoAddressDto build = ShippoAddressDto.builder()
                .company(companyInfo.getCompany())
                .email(companyInfo.getEmail())
                .street1(companyInfo.getAddress1())
                .street2(companyInfo.getAddress2())
                .phone(companyInfo.getPhone())
                .city(companyInfo.getCity())
                .state(companyInfo.getState())
                .name(companyInfo.getName())
                .country(companyInfo.getCountry())
                .zipCode(companyInfo.getZip())
                .build();

        return build;
    }
}