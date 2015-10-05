package org.icatproject.utils;

import org.icatproject.IcatException_Exception;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestICATGetter {

	@BeforeClass
	public static void beforeClass() {
		serverUrl = System.getProperty("serverUrl");
		if (serverUrl == null) {
			throw new NullPointerException();
		}
	}

	static String serverUrl;

	@Test(expected = IcatException_Exception.class)
	public final void testNull() throws Exception {
		ICATGetter.getService(null);
	}

	@Test
	public final void testGood() throws Exception {
		ICATGetter.getService(serverUrl);

	}

	@Test
	public final void testGood3() throws Exception {
		ICATGetter.getService(serverUrl + "/icat/ICAT?wsdl");
	}

	@Test(expected = IcatException_Exception.class)
	public final void testBadSuffix() throws Exception {
		ICATGetter.getService(serverUrl + "/QQQ/ICAT?wsdl");
	}

}