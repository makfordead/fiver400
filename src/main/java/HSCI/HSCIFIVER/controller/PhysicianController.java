package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.constant.Status;
import HSCI.HSCIFIVER.dto.*;
import HSCI.HSCIFIVER.entity.*;
import HSCI.HSCIFIVER.repositories.MedicalRecordRespository;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.TreatmentRepository;
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
import java.util.Objects;
import java.util.Optional;

@RestController
public class PhysicianController {
    @Autowired
    private TreatmentRepository treatmentRepository;
    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private MedicalRecordRespository medicalRecordRespository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PutMapping("/updatephysician")
    @PreAuthorize("hasRole('PHYSICIAN')")
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
    @PreAuthorize("hasRole('PHYSICIAN')")

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
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> getAllPatients(Principal principal){

        List<MedicalRecord> medicalRecordList = medicalRecordRespository.getMedicalRecords(principal.getName());
        PhysiciansMedicalRecordGetListDto  physiciansMedicalRecordGetListDto = new PhysiciansMedicalRecordGetListDto();
        for (MedicalRecord record:medicalRecordList
             ) {
            if (record.getStatus().toString().equals(Status.DISMISS.toString()))
                continue;
            physiciansMedicalRecordGetListDto.addToList(record);
        }
        return new ResponseEntity<>(physiciansMedicalRecordGetListDto,HttpStatus.OK);

    }

    @PutMapping
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> updatePatientTreantment(Principal principal, @RequestBody CreateUpdateTreatmentDto
                                                     createUpdateTreatmentDto){
        return null;
        
    }
    @GetMapping("/getpatientDetails")
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> getPatientMedicalRecords(@RequestParam Long patientid){
        return new ResponseEntity<>(medicalRecordRespository.getMedicalRecordsfromPatientId(patientid),HttpStatus.OK);
    }
    @GetMapping("/getmedicalrecordbyid")
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> getMedicalRecordById(@RequestParam Long medicalid) throws Exception{
        MedicalRecord medicalRecord=medicalRecordRespository.getOne(medicalid);
        if(Objects.isNull(medicalRecord))
            throw new Exception("No medical record found");
        return new ResponseEntity<>(medicalRecord,HttpStatus.OK);
    }
    @PutMapping("/updatemedicalrecord")
    @PreAuthorize("hasRole('PHYSICIAN')")
    public ResponseEntity<?> updateMedicalRecord(@RequestParam MedicalRecord medicalRecord)
    throws Exception{
        if(Objects.isNull(medicalRecord.getId()))
            throw new Exception("The id is not valid");

        medicalRecordRespository.save(medicalRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/updatetreatment")
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> updateTreatment(@RequestParam Long recordId,@RequestBody TreatmentUpdateDto treatmentUpdateDto)
    throws Exception{
        Treatment treatment = modelMapper.map(treatmentUpdateDto,Treatment.class);
        Optional<MedicalRecord> medicalRecord = medicalRecordRespository.findById(recordId);
        if(!medicalRecord.isPresent())
            throw new Exception("Medical record not found");
        treatment.setId(medicalRecord.get().getTreatment().getId());
        treatment = treatmentRepository.save(treatment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("dismissPatient")
    @PreAuthorize("hasRole('PHYSICIAN')")

    public ResponseEntity<?> dismissPatient(@RequestParam Long recordid) throws Exception{
        Optional<MedicalRecord> medicalRecord = medicalRecordRespository.findById(recordid);
        if(!medicalRecord.isPresent())
            throw new Exception("Medical record not found");
        medicalRecord.get().setStatus(Status.DISMISS);
        medicalRecordRespository.save(medicalRecord.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
