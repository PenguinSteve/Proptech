package tdtu.Proptech.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UploadSoldPropertyRequest {
	private Long propertyId;
	private LocalDate salesDate;
	private Double salesPrice;
	private String buyerName;
	private String buyerEmail;
	private String buyerPhone;
}
