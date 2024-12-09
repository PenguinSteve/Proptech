package tdtu.Proptech.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRentedPropertyRequest {
    private Long propertyId;
    private LocalDateTime rentalDate;
    private Integer rentalDuration;
    private Double rentalPrice;
    private String renterName;
    private String renterEmail;
    private String renterPhone;
}
