package org.icatproject.utils;

import static org.junit.Assert.assertEquals;

import org.icatproject.utils.ContainerGetter.ContainerType;
import org.junit.Test;

public class TestContainerGetter {

	@Test
	public final void testUnknown() throws Exception {
		assertEquals(ContainerType.UNKNOWN, ContainerGetter.getContainer());
	}
}
