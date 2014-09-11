package org.icatproject.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;

public class TestShellCommand {

	@Test
	public void testDate() throws Exception {
		ShellCommand sc = new ShellCommand("date");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertTrue("stdout", stdout.length() > 1);
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testSlow() throws Exception {
		ShellCommand sc = new ShellCommand("sleep", "3");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		System.out.println(stderr);
		assertEquals("stdout", 0, stdout.length());
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testPipein() throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream("date\nsleep 1\ndate".getBytes());
		ShellCommand sc = new ShellCommand(null, bais, "cat");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertEquals("stdout", 17, stdout.length());
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testHome() throws Exception {
		ShellCommand sc = new ShellCommand(Paths.get("/home"), (InputStream) null, "pwd");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertEquals("stdout", "/home\n", stdout);
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testBigOutput() throws Exception {
		ShellCommand sc = new ShellCommand("lsof");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertTrue("stdout", stdout.length() > 1);
		System.out.println(stderr);
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testLs() throws Exception {
		ShellCommand sc = new ShellCommand("ls", "-t");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertTrue("stdout", stdout.length() > 1);
		assertEquals("stderr", 0, stderr.length());
		assertEquals("exitCode", 0, exitCode);
	}

	@Test
	public void testBadDate() throws Exception {
		ShellCommand sc = new ShellCommand("date", "12");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertEquals("stdout", 0, stdout.length());
		assertEquals("stderr", "date: invalid date `12'\n", stderr);
		assertEquals("exitCode", 1, exitCode);
	}

	@Test
	public void testBadDateFromList() throws Exception {
		ShellCommand sc = new ShellCommand(Arrays.asList("date", "12"));
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertEquals("stdout", 0, stdout.length());
		assertEquals("stderr", "date: invalid date `12'\n", stderr);
		assertEquals("exitCode", 1, exitCode);
	}

	@Test
	public void testWibble() throws Exception {
		ShellCommand sc = new ShellCommand("wibble", "12");
		String stdout = sc.getStdout();
		String stderr = sc.getStderr();
		int exitCode = sc.getExitValue();
		assertEquals("stdout", 0, stdout.length());
		assertEquals("stderr", "Cannot run program \"wibble\": error=2, No such file or directory",
				stderr);
		assertEquals("exitCode", 1, exitCode);
	}

}