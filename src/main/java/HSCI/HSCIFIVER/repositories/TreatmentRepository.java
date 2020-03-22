package HSCI.HSCIFIVER.repositories;

import HSCI.HSCIFIVER.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment,Long> {
}
