package com.intuitionlabs.shippoduocartintegration.utiltiy;

import com.intuitionlabs.shippoduocartintegration.model.ParcelInfo;

public class ParcelUtility {

    /**
     * Parcel info is based on DHL parcels size and dimension
     * If you want to use own carrier extend this class and override the methods
     * @param weight
     * @return
     */

    public static ParcelInfo getParcelInfo(double weight) {

        if (weight <= 1.5) {
            return smallParcelOne(weight);
        } else if (weight <= 3) {
            return smallParcelTwo(weight);
        } else if (weight <= 7) {
            return mediumParcelOne(weight);
        } else if (weight <= 12) {
            return mediumParcelTwo(weight);
        } else if (weight <= 18) {
            return largeParcelOne(weight);
        } else if (weight <= 25) {
            return largeParcelTwo(weight);
        }

        return null;
    }


    /**
     * Parcel up to 1.5kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo smallParcelOne(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("34")
                .width("18")
                .height("10")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }

    /**
     * Parcel up to 3kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo smallParcelTwo(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("34")
                .width("32")
                .height("10")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }

    /**
     * Parcel up to 7kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo mediumParcelOne(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("34")
                .width("32")
                .height("18")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }

    /**
     * Parcel up to 12kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo mediumParcelTwo(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("34")
                .width("32")
                .height("34")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }

    /**
     * Parcel up to 18kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo largeParcelOne(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("42")
                .width("36")
                .height("37")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }

    /**
     * Parcel up to 25kg
     *
     * @param weight
     * @return
     */
    public static ParcelInfo largeParcelTwo(double weight) {
        ParcelInfo build = ParcelInfo.builder()
                .length("48")
                .width("40")
                .height("39")
                .weight(Double.toString(weight))
                .distance_unit("cm")
                .mass_unit("kg")
                .build();

        return build;
    }
}
