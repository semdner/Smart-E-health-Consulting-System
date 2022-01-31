package com.ehealthsystem.tools;

public interface HasAddress {
    String getStreet();
    String getHouseNumber();
    String getZipCode();

    default String getFormattedAddress() {
        return "%s %s, %s".formatted(getStreet(), getHouseNumber(), getZipCode());
    }
}
