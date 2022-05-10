package se.lexicon.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.lexicon.api.entity.Person;
import se.lexicon.api.repository.PersonRepository;

@Component
public class InitData implements CommandLineRunner {
    @Autowired
    PersonRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Person("Ulf","Bengtsson","ulf.bengtsson@lexicon.se",".Net Teacher"));
        repository.save(new Person("Erik","Svensson","erik.svensson@lexicon.se","Java Teacher"));
        repository.save(new Person("Simon","Elbrink","simon.elbrink@lexicon.se","Java Teacher"));
        repository.save(new Person("Mehrdad","Javan","mehrdad.javan@lexicon.se","Java Teacher"));
    }
}
