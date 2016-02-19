package org.icatproject.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Similar to the java.util.Properties class but provides methods to convert
 * values to a specific type and throw an exception if this cannot be done.
 * 
 * For example the call getNonNegativeInt() will return an integer which is
 * greater than or equal to zero.
 */
public class CheckedProperties {

	private Properties properties = new Properties();

	public class CheckedPropertyException extends Exception {

		private static final long serialVersionUID = 1L;

		CheckedPropertyException(String msg) {
			super(msg);
		}
	}

	private String fileName;

	private static FileSystem fileSystem = FileSystems.getDefault();

	/**
	 * Load from the specified file name
	 * 
	 * @param fileName
	 *            The name of the file to use to
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public void loadFromFile(String fileName) throws CheckedPropertyException {
		InputStream fis = null;

		try {
			fis = new FileInputStream(fileName);
			properties.load(fis);
			this.fileName = fileName;
		} catch (IOException e) {
			throw new CheckedPropertyException("Unable to load properties from " + fileName);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
	}

	/**
	 * Load properties from a name resource.
	 * 
	 * @param name
	 *            The name of the resource
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public void loadFromResource(String name) throws CheckedPropertyException {
		URL url = this.getClass().getClassLoader().getResource(name);
		if (url == null) {
			throw new CheckedPropertyException("Unable to locate resource " + name);
		}
		loadFromFile(url.getFile());
	}

	/**
	 * Return value as a string. The string will not be null or empty.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if the property is not found
	 */
	public String getString(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		value = value.trim();
		if (value.isEmpty()) {
			throw new CheckedPropertyException(name + " may not be empty in " + this.fileName);
		}
		return value;
	}

	/**
	 * Return value as a string. The string will not be null or empty unless the
	 * default value is used.
	 * 
	 * @param name
	 *            the name of the property
	 * @param defaultValue
	 *            the value to be returned if property not found
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if the property is found but is empty
	 */
	public String getString(String name, String defaultValue) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		value = value.trim();
		if (value.isEmpty()) {
			throw new CheckedPropertyException(name + " may not be empty in " + this.fileName);
		}
		return value;
	}

	/**
	 * Return value as an integer. It will be greater than zero.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public int getPositiveInt(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			int iValue = Integer.parseInt(value);
			if (iValue <= 0) {
				throw new CheckedPropertyException(
						name + " as defined in " + this.fileName + " is not a representation of a positive integer");
			}
			return iValue;
		} catch (NumberFormatException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a positive integer");
		}
	}

	/**
	 * Return value as an integer. It will be greater than or equal zero.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public int getNonNegativeInt(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			int iValue = Integer.parseInt(value);
			if (iValue < 0) {
				throw new CheckedPropertyException(name + " as defined in " + this.fileName
						+ " is not a representation of a non-negative integer");
			}
			return iValue;
		} catch (NumberFormatException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a non-negative integer");
		}
	}

	/**
	 * Return value as a double.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public double getDouble(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a floating point number");
		}
	}

	/**
	 * Return value as a boolean.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public boolean getBoolean(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		return Boolean.parseBoolean(value);
	}

	/**
	 * Return value as a boolean.
	 * 
	 * @param name
	 *            the name of the property
	 * @param defaultValue
	 *            the value to be returned if property not found
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public boolean getBoolean(String name, boolean defaultValue) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return getBoolean(name);
	}

	/**
	 * Return value as a URL.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public URL getURL(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			return new URL(value);
		} catch (MalformedURLException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a URL");
		}
	}

	/**
	 * Return true if the property with the specified name exists
	 * 
	 * @param name
	 *            name the name of the property
	 * 
	 * @return true if the property with the specified name exists else false
	 */
	public boolean has(String name) {
		return properties.getProperty(name) != null;
	}

	/**
	 * Return value as a File.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public File getFile(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		return new File(value);
	}

	/**
	 * Return value as a Path.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public Path getPath(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		return fileSystem.getPath(value);
	}

	/**
	 * Return value as an integer. It will be greater than or equal to zero.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public long getNonNegativeLong(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			long lValue = Long.parseLong(value);
			if (lValue < 0L) {
				throw new CheckedPropertyException(
						name + " as defined in " + this.fileName + " is not a representation of a non-negative long");
			}
			return lValue;
		} catch (NumberFormatException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a non-negative long");
		}

	}

	/**
	 * Return value as an integer. It will be greater than zero.
	 * 
	 * @param name
	 *            the name of the property
	 * 
	 * @return the value of the property
	 * 
	 * @throws CheckedPropertyException
	 *             if unable to carry out request
	 */
	public long getPositiveLong(String name) throws CheckedPropertyException {
		String value = properties.getProperty(name);
		if (value == null) {
			throw new CheckedPropertyException(name + " is not defined in " + this.fileName);
		}
		try {
			long lValue = Long.parseLong(value);
			if (lValue <= 0L) {
				throw new CheckedPropertyException(
						name + " as defined in " + this.fileName + " is not a representation of a positive long");
			}
			return lValue;
		} catch (NumberFormatException e) {
			throw new CheckedPropertyException(
					name + " as defined in " + this.fileName + " is not a representation of a positive long");
		}

	}

}
