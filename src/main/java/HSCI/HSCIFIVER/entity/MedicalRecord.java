package HSCI.HSCIFIVER.entity;

import HSCI.HSCIFIVER.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Data
@Entity
public class MedicalRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recordName = UUID.randomUUID().toString().replaceAll("-", "");

    @OneToOne

    private Treatment treatment;

    @OneToOne
    private Physician physician;

    @OneToOne
    private Patient patient;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Appointments> appointments;

    private Status status;

}
