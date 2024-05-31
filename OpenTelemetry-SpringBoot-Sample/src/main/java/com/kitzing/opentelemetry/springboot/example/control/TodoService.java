package com.kitzing.opentelemetry.springboot.example.control;

import com.kitzing.opentelemetry.springboot.example.entity.TodoEntry;
import com.kitzing.opentelemetry.springboot.example.model.TodoEntryDto;
import com.kitzing.opentelemetry.springboot.example.persistence.TodoRepository;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TodoService {

    final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @WithSpan("save-item")
    public void saveTodoItem(TodoEntryDto entry) {
        todoRepository.save(map(entry));
    }

    @WithSpan("get-list")
    public List<TodoEntryDto> getList() {
        return StreamSupport.stream(todoRepository.findAll().spliterator(), false)
                .peek(x -> Span.current().addEvent("Mapping item with id " + x.id)) //showcasing events
                .map(this::map)
                .collect(Collectors.toList());
    }

    @WithSpan  //will be shown as it is public
    public TodoEntryDto map(TodoEntry entry) {
        return TodoEntryDto.builder()
                .id(entry.id)
                .username(entry.username)
                .dueDate(entry.dueDate)
                .description(entry.description)
                .build();
    }

    @WithSpan //will not be shown as it is private
    private TodoEntry map(TodoEntryDto entry) {
        return TodoEntry.builder()
                .id(entry.id)
                .username(entry.username)
                .dueDate(entry.dueDate)
                .description(entry.description)
                .build();
    }
}
