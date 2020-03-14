package HSCI.HSCIFIVER.entity;

import HSCI.HSCIFIVER.constant.Status;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Data
@Entity
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Date treatmentDate;

    private String locationOfTreatment;

    private String anamneses;

    private String medicalFindings;

    private String medicalLetter;

    private String diagnoses;

    private String medications;

    private Status status;
}
