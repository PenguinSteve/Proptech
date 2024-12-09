package tdtu.Proptech.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class PropertyDTO {
    private Long id;
    private String name;
    private String address;
    private Double price;
    private String area;
    private String description;
    private String type;
    private String status;
    private UserDTO realtor;
    private List<ImageDTO> images;
}
