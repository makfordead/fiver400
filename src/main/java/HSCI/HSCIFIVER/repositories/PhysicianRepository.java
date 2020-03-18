package HSCI.HSCIFIVER.repositories;
import HSCI.HSCIFIVER.entity.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;


public interface PhysicianRepository extends JpaRepository<Physician,Long> {
    @Query("SELECT u FROM Physician u where u.user.username =:username")
    public Physician getphysicianbyusername(@Param("username") String username);

}
