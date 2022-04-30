package org.icatproject.utils;

import static org.junit.Assert.assertEquals;
import org.icatproject.utils.IcatUnits.SystemValue;
import org.junit.Test;

public class TestIcatUnits {
	@Test
	public void testArguments() {
		// Both null
		IcatUnits icatUnits = new IcatUnits();
		SystemValue systemValue = icatUnits.new SystemValue(null, "impossible to parse");
		assertEquals(null, systemValue.units);
		assertEquals(null, systemValue.value);

		// Unit parsed, value null
		systemValue = icatUnits.new SystemValue(null, "K");
		assertEquals("Kelvin", systemValue.units);
		assertEquals(null, systemValue.value);

		// Unit parsed, value converted
		systemValue = icatUnits.new SystemValue(1., "GK");
		assertEquals("Kelvin", systemValue.units);
		assertEquals(new Double(1e9), systemValue.value);
	}

	@Test
	public void testAliasing() {
		IcatUnits icatUnits = new IcatUnits();
		SystemValue systemValue = icatUnits.new SystemValue(null, "celsius");
		assertEquals(null, systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "degC");
		assertEquals(null, systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "kelvin");
		assertEquals(null, systemValue.units);

		icatUnits = new IcatUnits("\u2103: celsius degC, K: kelvin");
		systemValue = icatUnits.new SystemValue(null, "celsius");
		assertEquals("Kelvin", systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "degC");
		assertEquals("Kelvin", systemValue.units);
		systemValue = icatUnits.new SystemValue(null, "kelvin");
		assertEquals("Kelvin", systemValue.units);
	}
}