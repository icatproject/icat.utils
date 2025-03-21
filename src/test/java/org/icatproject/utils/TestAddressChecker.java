package org.icatproject.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestAddressChecker {
	@Test
	public void testIpv4() throws AddressCheckerException {
		AddressChecker a = new AddressChecker(" 192.168.3.0/24 190.168.3.0/28 ");
		assertTrue("One", a.check("192.168.3.255"));
		assertTrue("Two", a.check("190.168.3.15"));
		assertFalse("Three", a.check("192.168.4.19"));
		assertFalse("Four", a.check("190.168.3.16"));
	}

	@Test
	public void testIpv6() throws AddressCheckerException{
		AddressChecker a = new AddressChecker("192:168:3:0:0:0:0:0/112");
		assertTrue("One", a.check("192:168:3:0:0:0:0:0"));
		assertTrue("Two", a.check("192:168:3:0:0:0:0:FFFF"));
		assertFalse("Three", a.check("192:168:3:0:0:0:1:0"));
	}

	@Test
	public void testLocalhost() throws AddressCheckerException {
		AddressChecker a = new AddressChecker("localhost");

		assertTrue(a.check("127.0.0.1"));
		assertFalse(a.check("127.0.0.0")); // flip last bit
		assertFalse(a.check("255.0.0.1")); // flip first bit

		assertTrue(a.check("::1"));
		assertFalse(a.check("::0")); // flip last bit
		assertFalse(a.check("8000::1")); // flip first bit
	}

	@Test
	public void testInvalid() {
		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("test.invalid");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("localhost/32");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("/");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("/32");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("10.0.0.0/");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("10.0.0.0/-1");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("10.0.0.0/x");
		});

		assertThrows(AddressCheckerException.class, () -> {
			new AddressChecker("10.0.0.0/33");
		});
	}
}
