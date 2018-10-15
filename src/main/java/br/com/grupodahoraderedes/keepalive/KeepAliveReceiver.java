package br.com.grupodahoraderedes.keepalive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

public class KeepAliveReceiver
	extends
		Thread
{
	private final String ip;
	private final BufferedReader reader;

	public KeepAliveReceiver(
		final String ip,
		final InputStream reader
	)
	{
		this.ip = ip;
		this.reader = new BufferedReader( new InputStreamReader( reader ) );
	}

	@Override
	public void run()
	{
		boolean running = true;
		while (running) {
			try {
				while (reader.readLine() != null) {
					KeepAliveServer.updateSocketKeepAliveTime( ip, LocalDateTime.now() );
				}
			} catch (final IOException e) {
				System.out.println( ip + " has fallen into the abyss." );
				running = false;
			}
		}
	}
}
