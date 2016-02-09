package fr.hemit.utils;


import org.junit.Assert;
import org.junit.Test;

import fr.hemit.utils.StringUtils;

public class StringUtilsTests {

	@Test
	public void testHashpassword() {
		Assert.assertEquals("ec547cc6682507aa952e898479487ad9", StringUtils.HashPasswordWithMd5("aCompl3XPwd!"));
	}

}
