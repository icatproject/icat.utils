package org.icatproject.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ContainerGetter {

	public enum ContainerType {
		WILDFLY, GLASSFISH, UNKNOWN
	};

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