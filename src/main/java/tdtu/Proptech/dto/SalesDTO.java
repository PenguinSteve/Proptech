package tdtu.Proptech.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SalesDTO {
    private Long id;
    private PropertyDTO property;
    private LocalDateTime salesDate;
    private Double salesPrice;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
}
