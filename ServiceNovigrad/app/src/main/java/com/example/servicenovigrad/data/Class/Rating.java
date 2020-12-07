package com.example.servicenovigrad.data.Class;

public class Rating {
    String serviceId,customerId,rating;

    public Rating() {
    }

    public Rating(String customerId, String rating) {
        this.customerId = customerId;
        this.rating = rating;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
