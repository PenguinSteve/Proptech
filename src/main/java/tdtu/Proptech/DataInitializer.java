package tdtu.Proptech;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.SubscriptionRepository;
import tdtu.Proptech.repository.UserRepository;
import tdtu.Proptech.service.user.UserService;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DataInitializer {
	private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final SubscriptionRepository subscriptionRepository;

	@Bean
	CommandLineRunner loadData() {
		return args -> {
			loadSubscription();
			loadDataUsers();
		};
	}

	private void loadDataUsers() {
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setPassword("123456");
		user.setPhone("1");
		
		if (!userRepository.findByEmail("test@gmail.com").isPresent())
			userService.register(user);
		User admin = new User();
		admin.setEmail("admin@gmail.com");
		admin.setPassword(encoder.encode("123456"));
		admin.setPhone("2");
		admin.setRole("ADMIN");
		if (!userRepository.findByEmail("admin@gmail.com").isPresent())
			userRepository.save(admin);
	}

	private void loadSubscription() {
		if (!subscriptionRepository.findByPlanName("STARTER").isPresent()) {
			Subscription starter = new Subscription();
			starter.setPlanName("STARTER");
			starter.setDurationInDays(5);
			starter.setPrice(0.0);
			starter.setBenefits("Tối đa 5 tin (Hiển thị trong 7 ngày");
			subscriptionRepository.save(starter);
		}
		if (!subscriptionRepository.findByPlanName("BASIC").isPresent()) {
			Subscription basic = new Subscription();
			basic.setPlanName("BASIC");
			basic.setDurationInDays(30);
			basic.setPrice(600000.0);
			basic.setBenefits("Tối đa 10 tin (Hiển thị trong 14 ngày)");
			subscriptionRepository.save(basic);

		}

		if (!subscriptionRepository.findByPlanName("STANDARD").isPresent()) {

			Subscription standard = new Subscription();
			standard.setPlanName("STANDARD");
			standard.setDurationInDays(30);
			standard.setPrice(1100000.0);
			standard.setBenefits("Tối đa 20 tin (Hiển thị trong 21 ngày)");
			subscriptionRepository.save(standard);

		}
		if (!subscriptionRepository.findByPlanName("VIP").isPresent()) {

			Subscription vip = new Subscription();
			vip.setPlanName("VIP");
			vip.setDurationInDays(30);
			vip.setPrice(2500000.0);
			vip.setBenefits("Không giới hạn tin (Hiển thị trong 30 ngày)");
			subscriptionRepository.save(vip);

		}
	}
}