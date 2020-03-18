package HSCI.HSCIFIVER.dto;

import HSCI.HSCIFIVER.entity.Appointments;
import HSCI.HSCIFIVER.entity.Patient;
import HSCI.HSCIFIVER.entity.Physician;
import HSCI.HSCIFIVER.entity.Treatment;
import lombok.Data;

import java.util.List;

@Data
public class SingleMedicalRecordDto {
    private String recordName;
    List<Treatment> treatments;
    private Physician physician;
    private Patient patient;
    private List<Appointments> appointments;
}
