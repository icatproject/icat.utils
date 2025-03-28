package org.icatproject.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to check IP4 and IP6 addresses for acceptability.
 */
public class AddressChecker {

	private final List<Pattern> patterns = new ArrayList<Pattern>();

	private static class Pattern {
		private final InetAddress patternAddress;
		private final BigInteger mask;

		public Pattern(InetAddress patternAddress, Integer prefixLength) throws AddressCheckerException {
			int inetAddressBits = patternAddress.getAddress().length * 8;

			if (prefixLength == null) {
				// Default to an exact match (/32 for IPv4, /128 for IPv6)
				prefixLength = inetAddressBits;
			}

			if (prefixLength > inetAddressBits) {
				throw new AddressCheckerException(String.format("Prefix length %d cannot be greater than %d for address %s", prefixLength, inetAddressBits, patternAddress.getHostAddress()));
			}

			/* Set the highest-order bits, e.g. for IPv4 with prefixLength=24:
			 *  - bits 0-7 are set to 0
			 *  - bits 8-31 are set to 1
			 */
			BigInteger mask = BigInteger.ZERO;
			for (int i = 0; i < prefixLength; i++) {
				mask = mask.setBit(inetAddressBits - i - 1);
			}

			this.patternAddress = patternAddress;
			this.mask = mask;
		}

		public boolean matches(InetAddress address) {
			// Check that they are the same protocol (IPv4/IPv6)
			if (address.getAddress().length != patternAddress.getAddress().length) {
				return false;
			}

			BigInteger maskedAddress = inetAddressToBigInteger(address).and(mask);
			BigInteger maskedPatternAddress = inetAddressToBigInteger(patternAddress).and(mask);

			return maskedAddress.equals(maskedPatternAddress);
		}
	}

	private static BigInteger inetAddressToBigInteger(InetAddress inetAddress) {
		// Takes a byte array in big-endian order, so it is suitable for network addresses
		// '1' indicates that a positive number should be returned
		return new BigInteger(1, inetAddress.getAddress());
	}

	/**
	 * Takes a space separated list of patterns to accept
	 * 
	 * @param patternString
	 *            a space separated list of patterns to accept
	 * @throws AddressCheckerException
	 *             if any pattern is invalid.
	 */
	public AddressChecker(String patternString) throws AddressCheckerException {
		for (String s : patternString.trim().split("\\s+")) {
			// Split on the first "/", creating up to 2 parts
			String[] parts = s.split("/", 2);

			// A hostname can resolve to multiple IP addresses
			InetAddress[] inetAddresses;
			try {
				inetAddresses = InetAddress.getAllByName(parts[0]);
			} catch (UnknownHostException e) {
				throw new AddressCheckerException(String.format("Invalid address: %s", s));
			}

			Integer maskBits = null;
			if (parts.length > 1) {
				try {
					maskBits = Integer.valueOf(parts[1]);
				} catch (NumberFormatException e) {
					throw new AddressCheckerException(String.format("Invalid network prefix: %s", s));
				}

				if (maskBits < 0) {
					throw new AddressCheckerException(String.format("Network prefix cannot be negative: %s", s));
				}
			}

			for (InetAddress inetAddress : inetAddresses) {
				// InetAddress.toString() returns "<hostname>/<ip>", so it will only start with "/" if an IP was used.
				if (!inetAddress.toString().startsWith("/") && maskBits != null) {
					throw new AddressCheckerException(String.format("Cannot specify network prefix with a hostname: %s", s));
				}

				patterns.add(new Pattern(inetAddress, maskBits));
			}
		}
	}

	/**
	 * Check that an IP address matches one of the desired patterns
	 * 
	 * @param address
	 *            the input address
	 * 
	 * @return true if it matches
	 * @throws AddressCheckerException
	 *             if the address is badly formed.
	 */
	public boolean check(String address) throws AddressCheckerException {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new AddressCheckerException(String.format("Invalid address: %s", address));
		}

		for (Pattern pattern : patterns) {
			if (pattern.matches(inetAddress)) {
				return true;
			}
		}

		return false;
	}
}
