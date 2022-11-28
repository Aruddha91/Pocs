package com.example.reactivekafkaproducer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("Orders")
public class OrdersDTO {

    @JsonProperty
    List<OrderDTO> orders;

    public List<OrderDTO> getOrders() {
        return orders;
    }
}
