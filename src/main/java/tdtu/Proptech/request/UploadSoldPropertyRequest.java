package tdtu.Proptech.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadSoldPropertyRequest {
	private Long propertyId;
	private LocalDate salesDate;
	private Double salesPrice;
	private String buyerName;
	private String buyerEmail;
	private String buyerPhone;
}
