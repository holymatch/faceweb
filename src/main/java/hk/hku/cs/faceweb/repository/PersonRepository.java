package hk.hku.cs.faceweb.repository;

import hk.hku.cs.faceweb.model.Face;
import hk.hku.cs.faceweb.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Transactional(readOnly=false)
    public Person findByFace(Face face);

}
