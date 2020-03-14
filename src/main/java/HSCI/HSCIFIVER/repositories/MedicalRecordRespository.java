package HSCI.HSCIFIVER.repositories;

import HSCI.HSCIFIVER.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRespository extends JpaRepository<MedicalRecord,Long> {
}
