package org.icatproject.utils;

import javax.measure.IncommensurableException;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.format.MeasurementParseException;

import tech.units.indriya.format.SimpleUnitFormat;

/**
 * Utility to perform conversions to SI (System) units.
 */
public class IcatUnits {

	/**
	 * Holds the SI units and value for a quantity. If the units provided at construction could not be parsed, then these will be null.
	 */
	public class SystemValue {
		public String units = null;
		public Double value = null;

		/**
		 * Converts value units into an SI quantity.
		 * 
		 * @param value Quantity in (potentially) non-SI units.
		 * @param units Units of the provided value.
		 */
		public SystemValue(Double value, String units) {
			try {
				Unit<?> unit = unitFormat.parse(units);
				Unit<?> systemUnit = unit.getSystemUnit();
				this.units = systemUnit.getName();
				if (value == null) {
					return;
				}
				UnitConverter converter = unit.getConverterToAny(systemUnit);
				this.value = converter.convert(value.doubleValue());
			} catch (MeasurementParseException | UnconvertibleException | IncommensurableException e) {
				// If the units can't be parsed, or the value converted, then just return
				return;
			}
		}
	}

	private static final SimpleUnitFormat unitFormat = SimpleUnitFormat.getInstance();

	/**
	 * Creates instance with any aliasing.
	 */
	public IcatUnits() {
	}

	/**
	 * In addition to the standard names and prefixes, allows aliasing other terms
	 * for a unit. Note that Unit should be refered to by the symbol specified in
	 * the
	 * <a href=
	 * "https://javadoc.io/doc/tech.units/indriya/latest/tech/units/indriya/unit/Units.html">Indriya
	 * documentation</a>, but the aliases can be any string. However, OoM prefixes
	 * will not be applied to aliases. For example, "mK" would be understood as
	 * 0.001K, but if "Kelvin" is aliased than "mKelvin" will not be understood.
	 * 
	 * @param aliasOptions String with the format "<symbolA>: <aliasA1> <aliasA2>,
	 *                     <symbolB>: <aliasB2> ..."
	 */
	public IcatUnits(String aliasOptions) {
		for (String unitAliases : aliasOptions.split(",")) {
			String[] splitUnitAliases = unitAliases.split(":");
			Unit<?> unit = unitFormat.parse(splitUnitAliases[0].trim());
			for (String alias : splitUnitAliases[1].trim().split("\\s+")) {
				unitFormat.alias(unit, alias);
			}
		}
	}

}