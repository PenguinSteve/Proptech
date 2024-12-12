package tdtu.Proptech;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class CustomFormatter {
	public String priceFormatter(double amount) {
		return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount);
	}
}
