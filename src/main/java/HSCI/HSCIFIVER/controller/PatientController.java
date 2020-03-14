package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.constant.Roles;
import HSCI.HSCIFIVER.dto.*;
import HSCI.HSCIFIVER.entity.*;
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

import java.security.Principal;
import java.util.List;

@RestController
public class PatientController {
    @Autowired
    private MedicalRecordRespository medicalRecordRespository;
    @Autowired
    private PhysicianRepository physicianRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/register/patientregister")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRegisterDTO patientRegisterDTO)
    throws Exception {
        User temp = userRepository.findUserByUsername(patientRegisterDTO.getUsername());
        if(temp != null)
            throw new Exception("Username already taken!");
        User user = modelMapper.map(patientRegisterDTO,User.class);
        user.setRole(Roles.PATIENT);

        Patient patient = new Patient();
        Insurance insurance = new Insurance();

        patient.setUser(user);
        patient.setInsurance(insurance);
        patientRepository.save(patient);

        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/addpatientdetails")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<?> addPatientDetails(
            Principal principal,
            @RequestBody PatientDetailsDto patientDetailsDto) throws Exception{
        User user = userRepository.findUserByUsername(principal.getName());

        if(user == null)
            throw new Exception("User Not Found");
        Patient patient = patientRepository.getPatientByusername(principal.getName());

        Patient newPatient = modelMapper.map(patientDetailsDto,Patient.class);
        newPatient.setId(patient.getId());
        newPatient.setUser(patient.getUser());
        newPatient.setInsurance(patient.getInsurance());
        patientRepository.save(newPatient);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/addpatientinsurancedetails")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<?> addInsuranceDetails(Principal principal, @RequestBody PatientInsuranceDto patientInsuranceDto){
        User user = userRepository.findUserByUsername(principal.getName());

        Patient patient = patientRepository.getPatientByusername(user.getUsername());
        Insurance insurance = new Insurance();
        insurance = modelMapper.map(patientInsuranceDto,Insurance.class);
        insurance.setId(patient.getInsurance().getId());
        patient.setInsurance(insurance);

        patientRepository.save(patient);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getpatientdetails")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<?> getPatientDetails(Principal principal){
        Patient patient = patientRepository.getPatientByusername(principal.getName());
        PatientResponseDto patientResponseDto = modelMapper.map(patient,PatientResponseDto.class);
        return new ResponseEntity<>(patientResponseDto,HttpStatus.OK);
    }
    @GetMapping("/getpatientinsurancedetails")
    @PreAuthorize("hasAnyRole('PATIENT')")
    public ResponseEntity<?> getPatientInsuranceDetails(Principal principal){
        Patient patient = patientRepository.getPatientByusername(principal.getName());
        Insurance insurance = patient.getInsurance();
        PatientInsuranceDto patientInsuranceDto = modelMapper.map(insurance,PatientInsuranceDto.class);
        return new ResponseEntity<>(patientInsuranceDto,HttpStatus.OK);
    }
    @GetMapping("getallphysicians")
    public ResponseEntity<?> getallphysicians(Principal principal){
        List<Physician> physicians = physicianRepository.findAll();
        GetAllPhysiciansDto getAllPhysiciansDto = new GetAllPhysiciansDto();
        getAllPhysiciansDto.setPhysicians(physicians);
        return new ResponseEntity<>(getAllPhysiciansDto,HttpStatus.OK);
    }
    @PostMapping("/createamedicalappointment")
    public ResponseEntity<?> createMedicalAppointment(
            Principal principal
            ,@RequestParam("physicianId") Long physicianId)
                                                                throws Exception {
        Patient patient = patientRepository.getPatientByusername(principal.getName());
        Physician physician = physicianRepository.findById(physicianId).get();
        if(physician==null)
            throw new Exception("Physician not found");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPhysician(physician);
        medicalRecord.setPatient(patient);
        medicalRecord = medicalRecordRespository.save(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
