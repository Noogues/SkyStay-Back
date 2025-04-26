package com.example.skystayback.dtos.hotel;


import com.example.skystayback.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAdminVO {

   private Integer room_number;
   private Integer capacity;
   private RoomType room_type;
   private Boolean is_available;

}
