package HSCI.HSCIFIVER.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recordName;

    @OneToOne
    private Treatment treatment;

    @OneToOne
    private Physician physician;

    @OneToOne
    private Patient patient;

    @OneToMany
    @JoinColumn
    private List<Appointments> appointments;

}
