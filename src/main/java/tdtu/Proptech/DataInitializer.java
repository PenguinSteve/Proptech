package tdtu.Proptech;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.model.Subscription;
import tdtu.Proptech.model.User;
import tdtu.Proptech.repository.SubscriptionRepository;
import tdtu.Proptech.service.user.UserService;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DataInitializer {
	private final UserService userService;

	private final SubscriptionRepository subscriptionRepository;

	@Bean
	CommandLineRunner loadData() {
		return args -> {
			loadDataUsers();
			loadSubscription();
		};
	}

	private void loadDataUsers() {
		User user = new User();
		user.setEmail("a@abd.com");
		user.setPassword("123456");
		user.setPhone("1");
		userService.register(user);
	}

	private void loadSubscription(){
		Subscription starter = new Subscription();
		starter.setPlanName("STARTER");
		starter.setDurationInDays(5);
		starter.setPrice(0.0);
		starter.setBenefits("Tối đa 5 tin (Hiển thị trong 7 ngày");

		Subscription basic = new Subscription();
		basic.setPlanName("BASIC");
		basic.setDurationInDays(30);
		basic.setPrice(600000.0);
		basic.setBenefits("Tối đa 10 tin (Hiển thị trong 14 ngày)");

		Subscription standard = new Subscription();
		standard.setPlanName("STANDARD");
		standard.setDurationInDays(30);
		standard.setPrice(1100000.0);
		standard.setBenefits("Tối đa 20 tin (Hiển thị trong 21 ngày)");

		Subscription vip = new Subscription();
		vip.setPlanName("VIP");
		vip.setDurationInDays(30);
		vip.setPrice(2500000.0);
		vip.setBenefits("Không giới hạn tin (Hiển thị trong 30 ngày)");

		subscriptionRepository.save(starter);
		subscriptionRepository.save(basic);
		subscriptionRepository.save(standard);
		subscriptionRepository.save(vip);
	}

}