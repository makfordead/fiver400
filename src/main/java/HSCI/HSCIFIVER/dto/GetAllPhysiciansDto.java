package HSCI.HSCIFIVER.dto;

import HSCI.HSCIFIVER.entity.Physician;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllPhysiciansDto {


    List<PhysicianListDto> physiciansData = new ArrayList<>();

    public  void createPhysicianListDto(List<Physician> physicians){
        for (Physician phy:physicians
             ) {
            PhysicianListDto temp = new PhysicianListDto();
            temp.id = phy.getId();
            temp.name = phy.getName();
            temp.email=phy.getEmail();
            temp.surname=phy.getSurname();
            physiciansData.add(temp);

        }

    }
    @Data
    private class PhysicianListDto{
        private Long id;
        private String name;
        private String surname;
        private String email;
    }
}
