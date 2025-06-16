package com.example.skystayback.dtos.common;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public void setContent(List<T> content) { this.content = content; }

    public void setPage(int page) { this.page = page; }

    public void setSize(int size) { this.size = size; }

    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}