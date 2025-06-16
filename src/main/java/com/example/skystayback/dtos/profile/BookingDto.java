package com.example.skystayback.dtos.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private String code;
    private String type; // "hotel" or "apartment"
    private String accommodationName;
    private String accommodationCode;
    private String cityName;
    private String countryName;
    private String startDate;
    private String endDate;
    private String status;
    private String statusText;
    private Integer roomNumber;
    private String roomType;
    private Integer roomCapacity;
    private Double roomPrice;
    private Double totalAmount;
    private Double discount;
    private String orderCode;
    private String orderStatus;
    private Integer stars;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String image;
    private boolean canReview;
    private boolean hasReview;
    private Long reviewId;
    private boolean isPast;
    private boolean isCurrent;
    private boolean isFuture;
    private boolean canCancel;
    private String checkInTime;
    private String checkOutTime;
    private Integer totalNights;
    private String amenities;
}