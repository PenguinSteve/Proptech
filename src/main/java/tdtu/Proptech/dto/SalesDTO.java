package tdtu.Proptech.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {
	private Long id;
	private PropertyDTO property;
	private LocalDate salesDate;
	private Double salesPrice;
	private String buyerName;
	private String buyerEmail;
	private String buyerPhone;
}
