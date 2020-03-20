package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.dto.CreateUpdateTreatmentDto;
import HSCI.HSCIFIVER.dto.PhysicianGetResponseDto;
import HSCI.HSCIFIVER.dto.PhysicianUpdateDto;
import HSCI.HSCIFIVER.entity.*;
import HSCI.HSCIFIVER.repositories.MedicalRecordRespository;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PhysicianController {
    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private MedicalRecordRespository medicalRecordRespository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PutMapping("/updatephysician")
    public ResponseEntity<?> updatePhysicianDetail(Principal principal, @RequestBody PhysicianUpdateDto physicianUpdateDto){
        User user = userRepository.findUserByUsername(principal.getName());
        Physician physician = physicianRepository.getphysicianbyusername(principal.getName());
        Physician newPhysician = modelMapper.map(physicianUpdateDto,Physician.class);
        newPhysician.setId(physician.getId());
        newPhysician.setUser(physician.getUser());
        physician = physicianRepository.save(newPhysician);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getphysiciandetail")
    public ResponseEntity<?> getPhysicianDetail(Principal principal){
        Physician physician = physicianRepository.getphysicianbyusername(principal.getName());
        PhysicianGetResponseDto physicianGetResponseDto = modelMapper.map(physician,PhysicianGetResponseDto.class);
        return new ResponseEntity<>(physicianGetResponseDto,HttpStatus.OK);
    }

    @PostMapping("/maketreatment")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> makeMedicalTreatment(Principal principal, @RequestBody CreateUpdateTreatmentDto createUpdateTreatmentDto
    ,@RequestParam Long medicalRecordId) throws Exception{

        User user = userRepository.findUserByUsername(principal.getName());
        Treatment treatment = modelMapper.map(createUpdateTreatmentDto,Treatment.class);
        MedicalRecord medicalRecord = medicalRecordRespository.getOne(medicalRecordId);
        if(medicalRecord == null){
            throw new Exception("Medical Record Not Found");
        }
        medicalRecord.setTreatment(treatment);
        medicalRecord = medicalRecordRespository.save(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/getPatients")
    public ResponseEntity<?> getAllPatients(Principal principal){

        List<MedicalRecord> medicalRecordList = medicalRecordRespository.getMedicalRecords(principal.getName());
        List<Patient> patientList = new ArrayList<>();
        for (MedicalRecord medicalRecord: medicalRecordList) {
            patientList.add(medicalRecord.getPatient());
        }
        return new ResponseEntity<>(patientList,HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<?> updatePatientTreantment(Principal principal, @RequestBody CreateUpdateTreatmentDto
                                                     createUpdateTreatmentDto){
        return null;
        
    }
    @GetMapping("/getpatientDetails")
    public ResponseEntity<?> getPatientMedicalRecords(@RequestParam Long patientid){
        return new ResponseEntity<>(medicalRecordRespository.getMedicalRecordsfromPatientId(patientid),HttpStatus.OK);
    }

}
