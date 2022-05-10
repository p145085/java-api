package se.lexicon.api.service;

import se.lexicon.api.dto.PersonDto;
import se.lexicon.api.exception.DataConstraintViolationException;
import se.lexicon.api.exception.DataDuplicateException;
import se.lexicon.api.exception.DataNotFoundException;

import java.util.List;

public interface PersonService {

    PersonDto findById(Integer id) throws DataNotFoundException;

    PersonDto findByEmail(String email) throws DataNotFoundException;

    List<PersonDto> findAll();

    PersonDto create(PersonDto dto) throws DataDuplicateException, DataNotFoundException;

    PersonDto update(PersonDto dto) throws DataNotFoundException;

    void delete(Integer id) throws DataNotFoundException, DataConstraintViolationException;


}
