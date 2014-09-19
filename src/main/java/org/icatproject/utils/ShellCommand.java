package org.icatproject.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper around ProcessBuilder to allow stdout and stderr to be kept separate without risk of
 * blocking. It throws no exceptions - it is the responsibility of the caller to look at the exit
 * code and at stderr and stdout to see if it worked as expected.
 */
public class ShellCommand {

	private int exitValue;
	private String stdout;
	private String stderr;

	/**
	 * Construct a ShellCommand
	 * 
	 * @param args
	 */
	public ShellCommand(String... args) {
		List<String> arglist = new ArrayList<String>(args.length);
		for (String arg : args) {
			arglist.add(arg);
		}
		init(null, null, arglist);
	}

	/**
	 * Construct a ShellCommand
	 * 
	 * @param args
	 */
	public ShellCommand(Path home, InputStream is, String... args) {
		List<String> arglist = new ArrayList<String>(args.length);
		for (String arg : args) {
			arglist.add(arg);
		}
		init(home, is, arglist);
	}

	/**
	 * Construct a ShellCommand
	 * 
	 * @param arglist
	 */
	public ShellCommand(List<String> arglist) {
		init(null, null, arglist);
	}

	private void init(Path home, InputStream is, List<String> args) {
		Process p = null;
		StreamReader osr;
		StreamReader esr;
		InputStream posr = null;
		InputStream pesr = null;
		OutputStream pisr = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(args);
			if (home != null) {
				pb.directory(home.toFile());
			}

			p = pb.start();

			posr = p.getInputStream();
			osr = new StreamReader(posr);
			osr.start();

			pesr = p.getErrorStream();
			esr = new StreamReader(pesr);
			esr.start();

			pisr = p.getOutputStream();
			if (is != null) {
				byte[] bytes = new byte[1024];
				int n;
				while ((n = is.read(bytes)) != -1) {
					pisr.write(bytes, 0, n);
				}
			}
			pisr.close(); // Close the stream feeding the process

			osr.join();
			esr.join();
			p.waitFor();

			exitValue = p.exitValue();
			stdout = osr.getOut();
			stderr = esr.getOut();

		} catch (Exception e) {
			exitValue = 1; // Standard linux "catchall" value
			stdout = "";
			stderr = e.getMessage();

		} finally {
			/* Make sure everything is safely closed */
			close(pisr);
			close(posr);
			close(pesr);
		}
	}

	/** Close a stream ignoring any errors */
	private void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}

	/**
	 * Get the stdout from the call
	 * 
	 * @return stdout as a String which may contain line separator characters
	 */
	public String getStdout() {
		return stdout;
	}

	/**
	 * Get the stderr from the call
	 * 
	 * @return stderr as a String which may contain line separator characters
	 */
	public String getStderr() {
		return stderr;
	}

	/**
	 * Get the return value for the call
	 * 
	 * @return return value for the call
	 */
	public int getExitValue() {
		return exitValue;
	}

	/**
	 * This considers an error to be a non-zero exit code or a non-empty stderr. This will not be
	 * valid for all commands.
	 * 
	 * @return true if it appears that the command failed else false
	 */
	public boolean isError() {
		return exitValue != 0 || !stderr.isEmpty();
	}

	/**
	 * Returns an error message based on the exit code and what was returned in stderr if it appears
	 * that there was an error else an empty string.
	 * 
	 * @return an error message
	 */
	public String getMessage() {
		if (isError()) {
			return "code " + exitValue + ": " + stderr;
		} else {
			return "";
		}
	}

}