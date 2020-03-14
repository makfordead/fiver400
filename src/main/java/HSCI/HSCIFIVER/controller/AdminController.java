package HSCI.HSCIFIVER.controller;

import HSCI.HSCIFIVER.constant.Roles;
import HSCI.HSCIFIVER.dto.PhysicianCreateDto;
import HSCI.HSCIFIVER.entity.Physician;
import HSCI.HSCIFIVER.entity.User;
import HSCI.HSCIFIVER.repositories.PhysicianRepository;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private PhysicianRepository physicianRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/createPhysician")
    private ResponseEntity<?> createPhysician(@RequestBody PhysicianCreateDto physicianCreateDto) throws Exception{
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
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
