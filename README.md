# duocart-shippo-integration
Dependancy for Shippo integration for DuoCart API

## ℹ️ Info
With this dependency you can add support for:  [Shippo](https://goshippo.com/) service - "*Shipping technology for every business*".

### ⚙️ Usage
-  Copy the dependency and add it to the project pom.xml file
```
<dependency>
    <groupId>io.github.intuitonlabs</groupId>
    <artifactId>duocart-shippo-integration</artifactId>
    <version>1.2</version>
</dependency>
```
-  After installation add the code from ```/examples``` directory to ```/contorller/storefront/CheckoutController.java``` and ```/converter/AddressConverter.java```
-  You need to make small changes to ```Order.java``` class and add ```Shipment.java``` class in the  project, available in ```/examples``` folder
-  You can extend the class ```ParcelUtility``` and overwrite method ```getParcelsInfo(double weight)``` from this dependency and add custom dimensions and weight reflecting your parcels. Currently, the parcel dimensions are based on DHL carrier with max weight of 25kg
-  After  creating account at Shippo and get the API_KEY you need to add is as envirument variable named ```SHIPPO_API_KEY```
- The dependency work only for US in country parcels

### 👨‍🏭 Contributors
Feel free to contribute to this library.

### ⚠️ Statement
There is no affiliation between Intuitionlabs and the project Dulo cart with Shippo service.

### ⚖️ License 
The integration is with APACHE 2 License as the Shippo Java Client which is used is with MIT.

