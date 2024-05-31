package com.kitzing.opentelemetry.springboot.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TodoEntry {

    @Id
    @GeneratedValue
    public Integer id;
    public String username;
    public String description;
    public Date dueDate;
}
