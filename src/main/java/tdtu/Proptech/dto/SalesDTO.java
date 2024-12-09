package tdtu.Proptech.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SalesDTO {
	private Long id;
	private PropertyDTO property;
	private LocalDate salesDate;
	private Double salesPrice;
	private String buyerName;
	private String buyerEmail;
	private String buyerPhone;
}
