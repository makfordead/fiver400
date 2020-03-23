package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.constant.Roles;
import HSCI.HSCIFIVER.constant.Status;
import HSCI.HSCIFIVER.dto.*;
import HSCI.HSCIFIVER.entity.*;
import HSCI.HSCIFIVER.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class PatientController {
    @Autowired
    private TreatmentRepository treatmentRepository;
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
    @CrossOrigin("*")
    @PreAuthorize("hasRole('PATIENT')")

    //@PreAuthorize("hasAnyRole('PATIENT')")
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
    @PreAuthorize("hasRole('PATIENT')")
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
    @GetMapping("/getallphysicians")
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<?> getallphysicians(Principal principal){
        List<Physician> physicians = physicianRepository.findAll();
        GetAllPhysiciansDto getAllPhysiciansDto = new GetAllPhysiciansDto();
        getAllPhysiciansDto.createPhysicianListDto(physicians);
        return new ResponseEntity<>(getAllPhysiciansDto,HttpStatus.OK);
    }
    @PostMapping("/createamedicalappointment")
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<?> createMedicalAppointment(
            Principal principal
            ,@RequestBody(required = true) Map<String,String> map)
                                                                throws Exception {
        Patient patient = patientRepository.getPatientByusername(principal.getName());
        Physician physician = physicianRepository.findById(Long.parseLong(map.get("physicianId"))).get();
        if(physician==null)
            throw new Exception("Physician not found");
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPhysician(physician);
        medicalRecord.setPatient(patient);
        medicalRecord.setStatus(Status.ACTIVE);
        Treatment treatment = new Treatment();
        treatment = treatmentRepository.save(treatment);
        medicalRecord.setTreatment(treatment);
        medicalRecord = medicalRecordRespository.save(medicalRecord);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getmedicalrecords")
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<?> getMedicalAppointment(Principal principal){

        List<MedicalRecord> medicalRecordsList = medicalRecordRespository.getmedicalrecordfromuserUsername(principal.getName());
        GetMedicalRecordDtoList getMedicalRecordDto = new GetMedicalRecordDtoList();
        getMedicalRecordDto.makeMedicalRedorcDtoList(medicalRecordsList);
        return new ResponseEntity<>(getMedicalRecordDto,HttpStatus.OK);
    }
    @GetMapping("/getPhysicianDetail")
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<?> getPhysicianDetail(@RequestParam Long physicianId){
        Physician physician = physicianRepository.getOne(physicianId);
        PhysicianDetailDto physicianDetailDto = modelMapper.map(physician,PhysicianDetailDto.class);
        return new ResponseEntity<>(physicianDetailDto,HttpStatus.OK);

    }
    @GetMapping("/getamedicalrecord")
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<?> getMedicalRecord(@RequestParam Long medicalRecordId) {

        MedicalRecord medicalRecord = medicalRecordRespository.getOne(medicalRecordId);
        SingleMedicalRecordDto singleMedicalRecordDto = modelMapper.map(medicalRecord,SingleMedicalRecordDto.class);
        return new ResponseEntity<>(singleMedicalRecordDto,HttpStatus.OK);

    }
}
