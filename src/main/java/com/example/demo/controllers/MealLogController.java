package com.example.demo.controllers;

import com.example.demo.models.MealLog;
import com.example.demo.repositories.MealLogRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class MealLogController {

    private final MealLogRepository mealLogRepository;

    public MealLogController(MealLogRepository mealLogRepository) {
        this.mealLogRepository = mealLogRepository;
    }

    @GetMapping
    public List<MealLog> getLogsByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return mealLogRepository.findByLogDate(localDate);
    }

    @PostMapping
    public MealLog addLog(@RequestBody MealLog log) {
        return mealLogRepository.save(log);
    }
}