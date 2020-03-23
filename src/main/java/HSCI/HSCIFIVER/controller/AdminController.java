package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.constant.Roles;
import HSCI.HSCIFIVER.dto.AdminGetAllPatientDto;
import HSCI.HSCIFIVER.dto.PhysicianCreateDto;
import HSCI.HSCIFIVER.entity.MedicalRecord;
import HSCI.HSCIFIVER.entity.Patient;
import HSCI.HSCIFIVER.entity.Physician;
import HSCI.HSCIFIVER.entity.User;
import HSCI.HSCIFIVER.repositories.MedicalRecordRespository;
import HSCI.HSCIFIVER.repositories.PatientRepository;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@RestController

public class AdminController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicalRecordRespository medicalRecordRespository;
    @Autowired
    private PhysicianRepository physicianRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/createPhysician")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPhysician(@RequestBody PhysicianCreateDto physicianCreateDto) throws Exception{
        User user = modelMapper.map(physicianCreateDto,User.class);
        user.setRole(Roles.PHYSICIAN);
        try {
            user = userRepository.save(user);

        }
        catch (Exception e) {
            throw new Exception("Cannot create User");
        }
        Physician physician = new Physician();
        physician.setUser(user);
        physicianRepository.save(physician);
        return new ResponseEntity<>("Sucess",HttpStatus.OK);
    }
    @PostMapping("/deletePhysician/")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> deletePhysician(@RequestParam Long physicianId) throws Exception{
        Physician physician = physicianRepository.getOne(physicianId);
        if(Objects.isNull(physician))
            throw new Exception("Physician not found");
        List<MedicalRecord> medicalRecordList = medicalRecordRespository.getMedicalRecordsByPhysicianId(physicianId);
        medicalRecordRespository.deleteAll(medicalRecordList);
        physicianRepository.delete(physician);
    return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/getallpatients")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> getAllPatients(){
        AdminGetAllPatientDto adminGetAllPatientDto = new AdminGetAllPatientDto();
        List<Patient> patients = patientRepository.findAll();
        for (Patient patient: patients
             ) {
            adminGetAllPatientDto.add(patient);
        }
        return new ResponseEntity<>(adminGetAllPatientDto,HttpStatus.OK);
    }
    @PostMapping("/admin/deletePatient/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatient(@RequestParam Long patientId) throws Exception{
        Patient patient = patientRepository.getOne(patientId);
        if(Objects.isNull(patient))
            throw new Exception("Patient not found");
        List<MedicalRecord> medicalRecordList = medicalRecordRespository.getMedicalRecordsfromPatientId(patientId);
        medicalRecordRespository.deleteAll(medicalRecordList);
        patientRepository.delete(patient);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
