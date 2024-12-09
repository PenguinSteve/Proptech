package tdtu.Proptech.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sales")
public class Sales {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "property_id", nullable = false)
	private Property property;

	@ManyToOne
	@JoinColumn(name = "realtor_id", nullable = false)
	private User realtor;

	@Column(nullable = false)
	private LocalDate salesDate;

	@Column(nullable = false)
	private Double salesPrice;

	@Column(nullable = false)
	private String buyerName;

	@Column(nullable = false)
	private String buyerEmail;

	@Column(nullable = false)
	private String buyerPhone;

	public Sales(Property property, User realtor, LocalDate salesDate, Double salesPrice, String buyerName,
			String buyerEmail, String buyerPhone) {
		this.property = property;
		this.realtor = realtor;
		this.salesDate = salesDate;
		this.salesPrice = salesPrice;
		this.buyerName = buyerName;
		this.buyerEmail = buyerEmail;
		this.buyerPhone = buyerPhone;
	}
}
