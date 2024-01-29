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

    @PostMapping(value = "/createPet", consumes = "application/json", produces = "text/plain")
    public String createPet(@RequestBody Pet pet) {
        int id = newId.getAndIncrement();
        petModel.add(pet, id);
        return (petModel.size() == 1)
                ? "Поздравляем! Вы создали вашего первого домашнего питомца с идентификатором " + id
                : "Поздравляем! Вы создали вашего домашнего питомца с идентификатором " + id;
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer, Pet> getAll() {
        return petModel.getAll();
    }

    @GetMapping(value = "/getPet", produces = "application/json")
    public Pet getPet(@RequestParam int id) {
        Pet pet = petModel.getFromList(id);
        if (pet == null) {
            // Если питомец с указанным идентификатором не найден, возвращаем null
            throw new PetNotFoundException(id);
        }
        return pet;
    }

    @DeleteMapping(value = "/deletePet/{id}", produces = "text/plain")
    public String deletePet(@PathVariable int id) {
        if (petModel.remove(id)) {
            return "Питомец с идентификатором " + id + " удален";
        } else {
            return "Питомец с идентификатором " + id + " не найден";
        }
    }

    @PutMapping(value = "/updatePet/{id}", consumes = "application/json", produces = "text/plain")
    public String updatePet(@PathVariable int id, @RequestBody Pet updatedPet) {
        Pet existingPet = petModel.getFromList(id);
        if (existingPet == null) {
            return "Питомец с идентификатором " + id + " не найден";
        }
        petModel.update(id, updatedPet);
        return "Питомец с идентификатором " + id + " изменен";
    }

    // Обработка исключения, когда питомец не найден
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Pet not found")
    public static class PetNotFoundException extends RuntimeException {
        public PetNotFoundException(int id) {
            super("Pet with ID " + id + " not found");
        }
    }
}
