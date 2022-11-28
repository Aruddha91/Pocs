package com.example.reactivekafkaconsumer.dto;

import com.fasterxml.jackson.annotation.*;
import org.springframework.lang.NonNull;


import java.math.BigDecimal;

@JsonRootName("Booking")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    @NonNull
    @JsonProperty()
    private String bookingNumber;

    @NonNull
    @JsonProperty()
    private String externalBookingIdentifier;

    @NonNull
    @JsonProperty()
    private Integer totalBookedPackageQuantity;

    @JsonProperty()
    private Integer totalBookedItemQuantity;

    @JsonProperty()
    private Integer totalBookedInnerItemQuantity;

    @JsonProperty()
    private BigDecimal totalBookedGrossWeight;

    @JsonProperty()
    private BigDecimal totalBookedNetWeight;

    @JsonProperty()
    private BigDecimal totalBookedNetNetWeight;

    @JsonProperty()
    private BigDecimal totalBookedVolume;

    @JsonProperty()
    private BigDecimal totalCargoValue;

    @JsonProperty()
    private String summaryMarksAndNumbers;

    @JsonProperty()
    private String summaryDescriptionOfGoods;

    @JsonProperty()
    private String remarkInternal;

    @JsonProperty()
    private String remarkExternal;

    @NonNull
    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(@NonNull String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    @NonNull
    public String getExternalBookingIdentifier() {
        return externalBookingIdentifier;
    }

    public void setExternalBookingIdentifier(@NonNull String externalBookingIdentifier) {
        this.externalBookingIdentifier = externalBookingIdentifier;
    }

    public Integer getTotalBookedPackageQuantity() {
        return totalBookedPackageQuantity;
    }

    public void setTotalBookedPackageQuantity(Integer totalBookedPackageQuantity) {
        this.totalBookedPackageQuantity = totalBookedPackageQuantity;
    }

    public Integer getTotalBookedItemQuantity() {
        return totalBookedItemQuantity;
    }

    public void setTotalBookedItemQuantity(Integer totalBookedItemQuantity) {
        this.totalBookedItemQuantity = totalBookedItemQuantity;
    }

    public Integer getTotalBookedInnerItemQuantity() {
        return totalBookedInnerItemQuantity;
    }

    public void setTotalBookedInnerItemQuantity(Integer totalBookedInnerItemQuantity) {
        this.totalBookedInnerItemQuantity = totalBookedInnerItemQuantity;
    }

    public BigDecimal getTotalBookedGrossWeight() {
        return totalBookedGrossWeight;
    }

    public void setTotalBookedGrossWeight(BigDecimal totalBookedGrossWeight) {
        this.totalBookedGrossWeight = totalBookedGrossWeight;
    }

    public BigDecimal getTotalBookedNetWeight() {
        return totalBookedNetWeight;
    }

    public void setTotalBookedNetWeight(BigDecimal totalBookedNetWeight) {
        this.totalBookedNetWeight = totalBookedNetWeight;
    }

    public BigDecimal getTotalBookedNetNetWeight() {
        return totalBookedNetNetWeight;
    }

    public void setTotalBookedNetNetWeight(BigDecimal totalBookedNetNetWeight) {
        this.totalBookedNetNetWeight = totalBookedNetNetWeight;
    }

    public BigDecimal getTotalBookedVolume() {
        return totalBookedVolume;
    }

    public void setTotalBookedVolume(BigDecimal totalBookedVolume) {
        this.totalBookedVolume = totalBookedVolume;
    }

    public BigDecimal getTotalCargoValue() {
        return totalCargoValue;
    }

    public void setTotalCargoValue(BigDecimal totalCargoValue) {
        this.totalCargoValue = totalCargoValue;
    }

    public String getSummaryMarksAndNumbers() {
        return summaryMarksAndNumbers;
    }

    public void setSummaryMarksAndNumbers(String summaryMarksAndNumbers) {
        this.summaryMarksAndNumbers = summaryMarksAndNumbers;
    }

    public String getSummaryDescriptionOfGoods() {
        return summaryDescriptionOfGoods;
    }

    public void setSummaryDescriptionOfGoods(String summaryDescriptionOfGoods) {
        this.summaryDescriptionOfGoods = summaryDescriptionOfGoods;
    }

    public String getRemarkInternal() {
        return remarkInternal;
    }

    public void setRemarkInternal(String remarkInternal) {
        this.remarkInternal = remarkInternal;
    }

    public String getRemarkExternal() {
        return remarkExternal;
    }

    public void setRemarkExternal(String remarkExternal) {
        this.remarkExternal = remarkExternal;
    }
}
