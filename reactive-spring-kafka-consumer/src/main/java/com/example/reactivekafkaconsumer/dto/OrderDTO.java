package com.example.reactivekafkaconsumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Order")
public class OrderDTO {

    @JsonProperty("id")
    private String orderId;

    @JsonProperty
    private String source;

    @JsonProperty
    private String destination;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
