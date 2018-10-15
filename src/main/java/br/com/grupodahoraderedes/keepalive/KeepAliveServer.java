package br.com.grupodahoraderedes.keepalive;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class KeepAliveServer
	extends
		Thread
{
	private static final int PORT = 12312;
	private static final KeepAliveChecker keepAliveChecker = new KeepAliveChecker();

	@Override
	public void run()
	{
		keepAliveChecker.start();
		while (true) {
			try (ServerSocket socket = new ServerSocket( PORT )) {
				final Socket clientSocket = socket.accept();
				final String connectedSocketIp = clientSocket.getInetAddress().getHostAddress();
				System.out.println( "New socket connection: " + connectedSocketIp );
				new KeepAliveReceiver( connectedSocketIp, clientSocket.getInputStream() ).start();
				keepAliveChecker.addSocket( clientSocket );
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateSocketKeepAliveTime(
		final String ip,
		final LocalDateTime time
	)
	{
		keepAliveChecker.updateLastKeepAliveTime( ip, time );
		System.out.println( ip + " updated keep alive time to " + time );
	}
}
