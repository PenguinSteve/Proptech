package tdtu.Proptech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;
    private PropertyDTO property;
    private LocalDateTime rentalDate;
    private Integer rentalDuration;
    private Double rentalPrice;
    private String renterName;
    private String renterEmail;
    private String renterPhone;
}
