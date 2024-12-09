package tdtu.Proptech.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadSoldPropertyRequest {
    private Long propertyId;
    private LocalDateTime salesDate;
    private Double salesPrice;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
}
