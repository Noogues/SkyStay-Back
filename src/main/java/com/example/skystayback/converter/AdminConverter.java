package com.example.skystayback.converter;
import com.example.skystayback.dtos.common.DataVO;
import com.example.skystayback.dtos.common.MessageResponseVO;
import com.example.skystayback.dtos.common.ResponseVO;
import com.example.skystayback.dtos.hotel.RoomConfigurationVO;
import com.example.skystayback.models.RoomConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminConverter {

    public static <T> ResponseVO<List<T>> convertPageToResponseVO(Page<T> page, String successMessage) {
        List<T> data = new ArrayList<>();
        page.forEach(data::add);
        ResponseVO<List<T>> responseVO = new ResponseVO<>();
        responseVO.setResponse(new DataVO<>(data));
        MessageResponseVO message = new MessageResponseVO();
        message.setTimestamp(LocalDateTime.now());
        message.setCode(200);
        message.setMessage(successMessage);
        responseVO.setMessages(message);
        return responseVO;
    }

    public static RoomConfigurationVO convertToRoomConfigurationVO(RoomConfiguration roomConfiguration) {
        RoomConfigurationVO roomConfigurationVO = new RoomConfigurationVO();
        roomConfigurationVO.setId(roomConfiguration.getId());
        roomConfigurationVO.setCapacity(roomConfiguration.getCapacity());
        roomConfigurationVO.setType(roomConfiguration.getType());
        return roomConfigurationVO;
    }
}
