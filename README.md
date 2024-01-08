# dulocart-shippo-integration
Dependancy for Shippo integration for DuoCart API

## ℹ️ Info
With this dependency you can add support for:  [Shippo](https://goshippo.com/) service - "*Shipping technology for every business*".

### ⚙️ Usage
-  Copy the dependency and add it to the project pom.xml file
```
<dependency>
    <groupId>io.github.intuitonlabs</groupId>
    <artifactId>duocart-shippo-integration</artifactId>
    <version>2.1</version>
</dependency>
```
-  After installation add the code from ```/examples``` directory to ```/contorller/storefront/CheckoutController.java``` and ```/converter/AddressConverter.java```
-  You need to make small changes to ```Order.java``` class and add ```Shipment.java``` class in the  project, available in ```/examples``` folder
-  You can extend the class ```ParcelUtility``` and overwrite method ```getParcelsInfo(double weight)``` from this dependency and add custom dimensions and weight reflecting your parcels. Currently, the parcel dimensions are based on DHL carrier with max weight of 25kg
-  After  creating account at Shippo and get the API_KEY you need to add is as envirument variable named ```SHIPPO_API_KEY```
- In ```CompanyInfo.java``` class you need to have set ```private Currency currency``` property
- After all of the following is set you need to refine the desired carriers accounts from the Shippo dashboard.
- The library pick the first available smaller rate and use it for the shipping.
- If *FIXED* Shipping rate ```( deliveryCost )``` property is set in ```CompanyInfo.java``` the shipping cost for the order will be this value.The real value for the shipping will be displayed in Shippo
- The dependency work only for US in country parcels
- ⚠️You can't select preferred delivery time from the library this need to happen in Shippo dashboard 

### 👨‍🏭 Contributors
Feel free to contribute to this library.

### ⚠️ Statement
There is no affiliation between Intuitionlabs and the project Dulo cart with Shippo service.

### ⚖️ License 
The integration is with APACHE 2 License as the Shippo Java Client which is used is with MIT.

