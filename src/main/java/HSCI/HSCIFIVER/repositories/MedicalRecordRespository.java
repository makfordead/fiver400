package HSCI.HSCIFIVER.repositories;

import HSCI.HSCIFIVER.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalRecordRespository extends JpaRepository<MedicalRecord,Long> {
    @Query("SELECT u FROM MedicalRecord u where u.patient.user.username = :username")
    public List<MedicalRecord> getmedicalrecordfromuserUsername(@Param("username") String username);
}
