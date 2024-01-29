package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petModel = PetModel.getInstance();
    private static final AtomicInteger newId = new AtomicInteger(1);

@PostMapping(value = "/createPet",consumes = "application/json")
    public String createPet(@RequestBody Pet pet) {
    int id = newId.getAndIncrement();
    petModel.add(pet, id);
    if (petModel.size() == 1) {
        return "Поздравляем! Вы создали вашего первого домашнего питомца с идентификатором " + id;
    } else {
        return "Поздравляем! Вы создали вашего домашнего питомца с идентификатором " + id;
    }
    }
    @GetMapping(value = "/getAll",produces = "application/json")
    public Map<Integer,Pet> getAll() {
        return petModel.getAll();

    }

    @GetMapping(value = "/getPet", consumes = "application/json", produces = "application/json")
    public Pet getPet(@RequestBody Map<String,Integer>id) {
        return petModel.getFromList(id.get("id"));
    }
    @DeleteMapping(value = "/deletePet/{id}")
    public String deletePet(@PathVariable int id) {
        if (petModel.remove(id)) {
            return "Питомец с идентификатором " + id + " удален";
        } else {
            return "Питомец с идентификатором " + id + " не найден";

        }
    }
    @PutMapping(value = "/updatePet/{id}", consumes = "application/json")
    public String updatePet(@PathVariable int id, @RequestBody Pet updatedPet) {
        if (petModel.getFromList(id) == null) {
            return "Питомец с идентификатором " + id + " не найден";
        }
        if (petModel.update(id, updatedPet)) {
            return "Питомец с идентификатором " + id + " изменен";
        } else {
            return "Не удалось изменить питомца с идентификатором " + id;
        }
    }
}
