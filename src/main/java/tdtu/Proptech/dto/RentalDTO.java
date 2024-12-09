package tdtu.Proptech.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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
