package org.icatproject.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class StreamReader extends Thread {

	private String out;
	private InputStream inputStream;
	private IOException iOexception;

	StreamReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		try {
			byte[] buff = new byte[4096];
			int n;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((n = inputStream.read(buff)) >= 0) {
				baos.write(buff, 0, n);
			}
			out = baos.toString();
		} catch (IOException e) {
			this.iOexception = e;
		}
	}

	String getOut() throws IOException {
		if (iOexception != null) {
			throw iOexception;
		}
		return out;
	}

}