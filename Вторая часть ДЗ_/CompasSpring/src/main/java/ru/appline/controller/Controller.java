package ru.appline.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    private Map<String, String> ranges = new HashMap<>();

    @PostMapping("/ranges")
    public void setRanges(@RequestBody Map<String, String> ranges) {
        this.ranges = ranges;
    }

    @GetMapping("/side")
    public String getSide(@RequestParam int degree) {
        for (Map.Entry<String, String> entry : ranges.entrySet()) {
            String[] range = entry.getValue().split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            if (degree >= start && degree <= end) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }
}

