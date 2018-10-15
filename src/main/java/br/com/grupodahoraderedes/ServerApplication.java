package br.com.grupodahoraderedes;

import java.util.List;

import br.com.grupodahoraderedes.broadcast.WeatherBroadcastServer;
import br.com.grupodahoraderedes.keepalive.KeepAliveServer;
import br.com.grupodahoraderedes.users.UserConnectionServer;

public class ServerApplication
{
	private static UserConnectionServer userConnectionServer;
	private static KeepAliveServer keepAliveServer;
	private static WeatherBroadcastServer weatherBroadcastServer;

	public static void main(
		final String[] args
	)
	{
		userConnectionServer = new UserConnectionServer();
		keepAliveServer = new KeepAliveServer();
		weatherBroadcastServer = new WeatherBroadcastServer();
		start();
	}

	private static void start()
	{
		userConnectionServer.start();
		keepAliveServer.start();
		weatherBroadcastServer.start();
	}

	public static void tellConnectionServerToShutDownTheseIps(
		final List<String> ips
	)
	{
		userConnectionServer.shutDownTheseIps( ips );
	}
}
