package se.lexicon.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.api.dto.PersonDto;
import se.lexicon.api.exception.DataConstraintViolationException;
import se.lexicon.api.exception.DataDuplicateException;
import se.lexicon.api.exception.DataNotFoundException;
import se.lexicon.api.service.PersonService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/person/")
@CrossOrigin("*")
public class PersonController {

    @Autowired
    PersonService service;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable("id") Integer id) throws DataNotFoundException {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("search/")
    public ResponseEntity<PersonDto> search(@RequestParam(name = "email") @Min(value = 1, message = "email is required") String email) throws DataNotFoundException {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@RequestBody @Valid PersonDto dto) throws DataDuplicateException, DataNotFoundException {
        System.out.println("dto = " + dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping
    public ResponseEntity<PersonDto> update(@RequestBody @Valid PersonDto dto) throws DataNotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(service.update(dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) throws DataNotFoundException, DataConstraintViolationException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
