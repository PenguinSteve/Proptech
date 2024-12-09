package tdtu.Proptech.request;

import java.time.LocalDateTime;

public class UploadSoldPropertyRequest {
    private Long propertyId;
    private LocalDateTime saleDate;
    private Double salePrice;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
}
