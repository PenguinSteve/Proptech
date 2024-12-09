package tdtu.Proptech.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadRentedPropertyRequest {
    private Long propertyId;
    private LocalDateTime rentalDate;
    private Integer rentalDuration;
    private Double rentalPrice;
    private String renterName;
    private String renterEmail;
    private String renterPhone;
}
