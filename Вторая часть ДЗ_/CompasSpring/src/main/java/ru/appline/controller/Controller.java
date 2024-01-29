package ru.appline.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Compass;
import ru.appline.logic.CompassModel;

import java.util.Map;

@RestController
@RequestMapping("/compass")
public class CompassController {

    private static final CompassModel compassModel = CompassModel.getInstance();

    @Autowired
    public CompassController(CompassModel compassModel) {
        this.compassModel = compassModel;
    }

    @PostMapping(value = "/createCompass", consumes = "application/json")
    public void createCompass(@RequestBody String side, Compass compass) {
        compassModel.add(compass, side);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Compass> getAll() {
        return compassModel.getAll();
    }

    @GetMapping(value = "/getSide", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSide(@RequestParam("Degree") Integer degree) {
        Compass side1 = compassModel.get("North");
        if (degree > side1.getMin() && degree < side1.getMax()) {
            return new ResponseEntity<>("{\"Side\": \"North\"}", HttpStatus.OK);
        }
        // Аналогичные проверки для других направлений...
        return new ResponseEntity<>("{\"text\": \"FAILED\"}", HttpStatus.OK);
    }
}