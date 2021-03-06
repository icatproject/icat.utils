package org.icatproject.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.icatproject.utils.CheckedProperties.CheckedPropertyException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCheckedProperties {

	private static CheckedProperties s_properties;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		s_properties = new CheckedProperties();
		s_properties.loadFromFile("src/test/resources/PropertiesFromResources.properties");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFileWithEnv1() throws CheckedPropertyException {
		assertEquals(s_properties.getFile("f1").toString(), System.getenv("HOME") + "/a/" + System.getenv("USER"));
		assertEquals(s_properties.getPath("f1").toString(), System.getenv("HOME") + "/a/" + System.getenv("USER"));
	}

	@Test
	public final void testFileWithEnv2() throws CheckedPropertyException {
		assertEquals(s_properties.getFile("f2").toString(), "a.b");
		assertEquals(s_properties.getPath("f2").toString(), "a.b");
	}

	@Test
	public final void testFileWithEnv3() throws CheckedPropertyException {
		assertEquals(s_properties.getFile("f3").toString(), "/a/b" + System.getenv("USER") + "/${USER");
		assertEquals(s_properties.getPath("f3").toString(), "/a/b" + System.getenv("USER") + "/${USER");
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testPropertiesFromResources() throws Exception {
		s_properties.loadFromFile("Doesn't exist");
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testEmpty() throws Exception {
		s_properties.getString("empty");
	}

	@Test
	public final void testGetString1() throws Exception {
		assertEquals("One", s_properties.getString("one"));
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetString2() throws Exception {
		s_properties.getString("noone");
	}

	@Test
	public final void testGetString3() throws Exception {
		assertEquals("wibble", s_properties.getString("noone", "wibble"));
	}

	@Test
	public final void testGetString4() throws Exception {
		assertEquals("", s_properties.getString("noone", ""));
	}

	@Test
	public final void testGetString5() throws Exception {
		assertEquals(null, s_properties.getString("noone", null));
	}

	@Test
	public final void testDbl1() throws Exception {
		assertEquals(1E23, s_properties.getDouble("dbl1"), 1.);
	}

	@Test
	public final void testTrail() throws Exception {
		assertEquals("aardvark", s_properties.getString("animal"));
	}

	@Test
	public final void testDbl2() throws Exception {
		assertEquals(2.0, s_properties.getDouble("dbl2"), 1E-12);
	}

	@Test
	public final void testDbl3() throws Exception {
		assertEquals(3.0, s_properties.getDouble("dbl3"), 1E-12);
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testBadDbl() throws Exception {
		s_properties.getDouble("badDbl");
	}

	@Test
	public final void testGetNonNegativeInt1() throws Exception {
		assertEquals(2, s_properties.getNonNegativeInt("two"));
	}

	@Test
	public final void testGetNonNegativeInt2() throws Exception {
		assertEquals(0, s_properties.getNonNegativeInt("zero"));
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetNonNegativeInt3() throws Exception {
		s_properties.getNonNegativeInt("minustwo");
	}

	@Test
	public final void testGetNonNegativeLong1() throws Exception {
		assertEquals(2L, s_properties.getNonNegativeLong("two"));
	}

	@Test
	public final void testGetNonNegativeLong2() throws Exception {
		assertEquals(0L, s_properties.getNonNegativeLong("zero"));
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetNonNegativeLong3() throws Exception {
		s_properties.getNonNegativeLong("minustwo");
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetPositiveInt1() throws Exception {
		s_properties.getPositiveInt("badtwo");
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetPositiveInt2() throws Exception {
		s_properties.getPositiveInt("minustwo");
	}

	@Test(expected = CheckedPropertyException.class)
	public final void testGetPositiveInt3() throws Exception {
		s_properties.getPositiveInt("zero");
	}

	@Test
	public final void testGetPositiveInt4() throws Exception {
		assertEquals(2, s_properties.getPositiveInt("two"));
	}

	@Test
	public final void testBools() throws Exception {
		assertTrue(s_properties.getBoolean("boolt1"));
		assertTrue(s_properties.getBoolean("boolt2"));
		assertTrue(s_properties.getBoolean("boolt3"));
		assertFalse(s_properties.getBoolean("boolf1"));
		assertFalse(s_properties.getBoolean("boolf2"));
		assertFalse(s_properties.getBoolean("boolf3"));
		assertFalse(s_properties.getBoolean("boolf3", false));
		assertFalse(s_properties.getBoolean("boolf3", true));
		assertTrue(s_properties.getBoolean("boolt3", false));
		assertTrue(s_properties.getBoolean("boolt3", true));
		assertFalse(s_properties.getBoolean("bool", false));
		assertTrue(s_properties.getBoolean("bool", true));
	}
}