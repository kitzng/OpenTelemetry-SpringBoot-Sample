package com.kitzing.opentelemetry.springboot.example.boundary;

import com.kitzing.opentelemetry.springboot.example.control.TodoService;
import com.kitzing.opentelemetry.springboot.example.model.TodoEntryDto;
import io.opentelemetry.api.trace.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/todo")
public class TodoApi {

    Logger logger = LoggerFactory.getLogger(TodoApi.class);
    final TodoService todoService;

    public TodoApi(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity postEntry(@RequestBody TodoEntryDto entry) {
        var previousTraceId = Span.current().getSpanContext().getTraceId();
        Span.wrap(SpanContext.createFromRemoteParent(UUID.randomUUID().toString(), UUID.randomUUID().toString(), TraceFlags.getSampled(), TraceState.getDefault()));
        Span.current().setAttribute("previous-trace-id", previousTraceId);
        logger.info("New entry added!");
        todoService.saveTodoItem(entry);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<TodoEntryDto>> getAll() {
        MDC.put("traceID", Span.current().getSpanContext().getTraceId());
        MDC.put("spanID", Span.current().getSpanContext().getSpanId());
        var result = todoService.getList();
        logger.info(String.format("Sending %d entries to client", result.size()));
        return ResponseEntity.ok(result);
    }

    @PatchMapping
    public ResponseEntity patchError() {
        try {
            logger.info("Something went wrong, oops...");
            throw new RuntimeException("Sorry mate");
        }catch (Exception e) {
            Span.current().recordException(e);
            return ResponseEntity.badRequest().body("Caught exception: " + e.getMessage());
        }
    }
}
