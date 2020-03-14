package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.dto.PhysicianGetResponseDto;
import HSCI.HSCIFIVER.dto.PhysicianUpdateDto;
import HSCI.HSCIFIVER.entity.Physician;
import HSCI.HSCIFIVER.entity.User;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class PhysicianController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhysicianRepository physicianRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PutMapping("/updatephysician")
    public ResponseEntity<?> updatePhysicianDetail(Principal principal,
                                                   @RequestBody PhysicianUpdateDto physicianUpdateDto){
        User user = userRepository.findUserByUsername(principal.getName());
        Physician physician = physicianRepository.getPhysicianByusername(principal.getName());
        Physician newPhysician = modelMapper.map(physicianUpdateDto,Physician.class);
        newPhysician.setId(physician.getId());
        newPhysician.setUser(physician.getUser());
        physician = physicianRepository.save(newPhysician);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getphysiciandetail")
    private ResponseEntity<?> getPhysicianDetail(Principal principal){
        Physician physician = physicianRepository.getPhysicianByusername(principal.getName());
        PhysicianGetResponseDto physicianGetResponseDto = modelMapper.map(physician,PhysicianGetResponseDto.class);
        return new ResponseEntity<>(physicianGetResponseDto,HttpStatus.OK);
    }


}
