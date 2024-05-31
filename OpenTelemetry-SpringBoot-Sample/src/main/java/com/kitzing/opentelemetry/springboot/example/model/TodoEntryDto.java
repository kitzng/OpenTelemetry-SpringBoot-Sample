package com.kitzing.opentelemetry.springboot.example.model;

import lombok.Builder;

import java.util.Date;

@Builder
public class TodoEntryDto {

    public Integer id;
    public String username;
    public String description;
    public Date dueDate;

}
