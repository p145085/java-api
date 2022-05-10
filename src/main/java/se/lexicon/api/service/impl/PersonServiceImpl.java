package se.lexicon.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.api.dto.PersonDto;
import se.lexicon.api.entity.Person;
import se.lexicon.api.exception.ArgumentException;
import se.lexicon.api.exception.DataDuplicateException;
import se.lexicon.api.exception.DataNotFoundException;
import se.lexicon.api.repository.PersonRepository;
import se.lexicon.api.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    ModelMapper mapper;
    PersonRepository repository;

    @Autowired
    public PersonServiceImpl(ModelMapper mapper, PersonRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public PersonDto findById(Integer id) throws DataNotFoundException {
        if (id == null || id == 0) throw new ArgumentException("Person Id should not be null or 0");
        Person result = repository.findById(id).orElseThrow(() -> new DataNotFoundException("Person not found"));
        return mapper.map(result, PersonDto.class);
    }

    @Override
    public PersonDto findByEmail(String email) throws DataNotFoundException {
        if (email == null || email.trim().equals("")) throw new ArgumentException("Email should not be null");
        Person result = repository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Person not found"));
        return mapper.map(result, PersonDto.class);
    }

    @Override
    public List<PersonDto> findAll() {
        List<Person> list = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(list::add);
        return list.stream().map(category -> mapper.map(category, PersonDto.class)).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PersonDto create(PersonDto dto) throws DataDuplicateException, DataNotFoundException {
        if (dto == null) throw new ArgumentException("Person data should not be null");
        if (dto.getId() != null) throw new ArgumentException("id should be null");
        if(repository.findByEmail(dto.getEmail()).isPresent()) throw new DataDuplicateException("Email is duplicate");
        Person entity = mapper.map(dto, Person.class);
        Person result = repository.save(entity);

        return mapper.map(result, PersonDto.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PersonDto update(PersonDto dto) throws DataNotFoundException {
        System.out.println("dto = " + dto);
        if (dto == null) throw new ArgumentException("Person data should not be null");
        if (dto.getId() == null) throw new ArgumentException("id should not be null");

        Person entity = mapper.map(dto, Person.class);
        Person result = repository.save(entity);

        return mapper.map(result, PersonDto.class);
    }

    @Override
    public void delete(Integer id) throws DataNotFoundException {
        repository.delete(mapper.map(findById(id), Person.class));
    }

}
