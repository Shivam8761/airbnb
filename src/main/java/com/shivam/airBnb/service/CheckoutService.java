package com.shivam.airBnb.service;


import com.shivam.airBnb.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
