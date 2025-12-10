package com.nhnacademy._vidiafront.admin.dto.request;

public record AdminDeliverySearchRequest(
        String deliveryStatus,
        Integer page,
        Integer size
) {
    public Integer pageOrDefault() {
        return page == null ? 0 : page;
    }

    public Integer sizeOrDefault() {
        return size == null ? 20 : size;
    }

    public String deliveryStatusOrNull(){
        return (deliveryStatus == null || deliveryStatus.isBlank()) ? "WAITING" : deliveryStatus;
    }
}
