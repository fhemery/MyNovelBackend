package fr.hemit;

import org.springframework.security.crypto.password.PasswordEncoder;

import fr.hemit.utils.StringUtils;

public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return StringUtils.HashPasswordWithMd5(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return StringUtils.HashPasswordWithMd5(rawPassword.toString()).equals(encodedPassword);
	}

}
