package com.kitzing.opentelemetry.springboot.example.persistence;

import com.kitzing.opentelemetry.springboot.example.entity.TodoEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<TodoEntry, Integer> {
}
