package org.icatproject.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.icatproject.utils.IcatUnits.Value;
import org.junit.Test;

public class TestIcatUnits {
	@Test
	public void testArguments() {
		// Units cannot be parsed null
		IcatUnits icatUnits = new IcatUnits();
		Value value = icatUnits.convertValueToSiUnits(1., "impossible to parse");
		assertNull(value);

		// Unit parsed, value unchanged
		value = icatUnits.convertValueToSiUnits(1., "K");
		assertEquals("Kelvin", value.units);
		assertEquals(1., value.numericalValue, 0.);

		// Unit parsed, value converted
		value = icatUnits.convertValueToSiUnits(1., "GK");
		assertEquals("Kelvin", value.units);
		assertEquals(1e9, value.numericalValue, 0.);
	}

	@Test
	public void testAliasing() {
		IcatUnits icatUnits = new IcatUnits();
		Value value = icatUnits.convertValueToSiUnits(1., "celsius");
		assertNull(value);
		value = icatUnits.convertValueToSiUnits(1., "degC");
		assertNull(value);
		value = icatUnits.convertValueToSiUnits(1., "kelvin");
		assertNull(value);
		value = icatUnits.convertValueToSiUnits(1., "eV");
		assertNull(value);

		icatUnits = new IcatUnits("\u2103: celsius, degC; K: kelvin; J: eV 1.602176634e-19");
		value = icatUnits.convertValueToSiUnits(1., "celsius");
		assertEquals("Kelvin", value.units);
		value = icatUnits.convertValueToSiUnits(1., "degC");
		assertEquals("Kelvin", value.units);
		value = icatUnits.convertValueToSiUnits(1., "eV");
		assertEquals("Joule", value.units);
		assertEquals(1.602176634e-19, value.numericalValue, 0.);
	}
}