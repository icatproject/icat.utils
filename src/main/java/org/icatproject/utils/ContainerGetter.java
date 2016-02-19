package org.icatproject.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

/** Is able to determine the type of the container */
public class ContainerGetter {

	/** The type of container */
	public enum ContainerType {
		/** WildFly */
		WILDFLY,

		/** Glassfish */
		GLASSFISH, 
		
		/** Container not recognised */
		UNKNOWN
	};

	/**
	 * returns the type of the container in use
	 * 
	 * @return the type of container
	 * 
	 */
	public static ContainerType getContainer() {
		ContainerType result = ContainerType.UNKNOWN;
		if (Files.exists(Paths.get("..", "jboss-modules.jar"))) {
			result = ContainerType.WILDFLY;
		} else if (Files.exists(Paths.get("glassfish-acc.xml"))) {
			result = ContainerType.GLASSFISH;
		}
		return result;
	}

}