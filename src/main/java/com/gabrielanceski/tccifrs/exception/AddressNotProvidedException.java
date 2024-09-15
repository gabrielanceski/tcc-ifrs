package com.gabrielanceski.tccifrs.exception;


public class AddressNotProvidedException extends InvalidDataException {

    public AddressNotProvidedException() {
        super("Address is null or not provided!");
    }
}
