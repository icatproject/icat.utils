package org.icatproject.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.icatproject.utils.IcatUnits.SystemValue;
import org.junit.Test;

public class TestIcatUnits {
	@Test
	public void testArguments() {
		// Both null
		IcatUnits icatUnits = new IcatUnits();
		SystemValue systemValue = icatUnits.new SystemValue(null, "impossible to parse");
		assertNull(systemValue.units);
		assertNull(systemValue.value);

		// Unit parsed, value null
		systemValue = icatUnits.new SystemValue(null, "K");
		assertEquals("Kelvin", systemValue.units);
		assertNull(systemValue.value);

		// Unit parsed, value converted
		systemValue = icatUnits.new SystemValue(1., "GK");
		assertEquals("Kelvin", systemValue.units);
		assertEquals(new Double(1e9), systemValue.value);
	}

	@Test
	public void testAliasing() {
		IcatUnits icatUnits = new IcatUnits();
		SystemValue systemValue = icatUnits.new SystemValue(null, "celsius");
		assertNull(systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "degC");
		assertNull(systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "kelvin");
		assertNull(systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "eV");
		assertNull(systemValue.units);

		new IcatUnits("\u2103: celsius, degC; K: kelvin; J: eV 1.602176634e-19");
		new IcatUnits();
		systemValue = icatUnits.new SystemValue(null, "celsius");
		assertEquals("Kelvin", systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "degC");
		assertEquals("Kelvin", systemValue.units);
		systemValue = icatUnits.new SystemValue(1., "eV");
		assertEquals("Joule", systemValue.units);
		assertEquals(new Double(1.602176634e-19), systemValue.value);
	}
}