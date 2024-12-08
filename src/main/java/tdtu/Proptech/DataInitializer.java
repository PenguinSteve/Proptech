package tdtu.Proptech;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tdtu.Proptech.model.User;
import tdtu.Proptech.service.user.UserService;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DataInitializer {
	UserService userService;

	@Bean
	CommandLineRunner loadData() {
		return args -> {
			loadDataUsers();
		};
	}

	private void loadDataUsers() {
		User user = new User();
		user.setEmail("a@abd.com");
		user.setPassword("123456");
		user.setPhone("1");
		userService.register(user);
	}

}