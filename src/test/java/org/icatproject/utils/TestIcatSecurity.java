package org.icatproject.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestIcatSecurity {

	@Test
	public final void testDigest() throws Exception {
		assertEquals("20D7BD6C934147BE744071DE756EB107931AC13466530F6CE9C970B07FC1C2A5",
				IcatSecurity.digest(42L, "here", "secret"));
		assertEquals("20D7BD6C934147BE744071DE756EB107931AC13466530F6CE9C970B07FC1C2A5",
				IcatSecurity.digest(42L, "here", "secret"));
		assertEquals("DD70E6ED357AB95614F190F33E35B95A00305418E76DF421493224A11AB7A4D1",
				IcatSecurity.digest(43L, "here", "secret"));
	}

}