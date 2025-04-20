package com.example.skystayback.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO {
    Integer limit = 30;
    Integer page = 0;

    public Integer getOffset() {
        return page * limit;
    }

    public Pageable toPageable() {
        return Pageable.ofSize(limit).withPage(page - 1);
    }
}
