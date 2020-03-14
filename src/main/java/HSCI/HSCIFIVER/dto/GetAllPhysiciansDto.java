package HSCI.HSCIFIVER.dto;

import HSCI.HSCIFIVER.entity.Physician;
import lombok.Data;

import java.util.List;

@Data
public class GetAllPhysiciansDto {
    List<Physician> physicians;
}
