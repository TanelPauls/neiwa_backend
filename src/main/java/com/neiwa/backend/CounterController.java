package com.neiwa.backend;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/counter")
public class CounterController {
    private final CounterRepository repo;

    public CounterController(CounterRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Counter> all() {
        return repo.findAll();
    }

    @PostMapping("/increment")
    public Counter increment() {
        Counter counter = repo.findAll().stream().findFirst().orElse(new Counter(0L));
        counter.setValue(counter.getValue() + 1);
        return repo.save(counter);
    }
    @PostMapping("/decrement")
    public Counter decrement() {
        Counter counter = repo.findAll().stream().findFirst().orElse(new Counter(0L));
        counter.setValue(counter.getValue() - 1);
        return repo.save(counter);
    }
}
