package br.com.grupodahoraderedes.users;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.grupodahoraderedes.scene.SceneHandler;

public class UserConnectionServer
	extends
		Thread
{
	private static final int PORT = 42333;
	private final List<Socket> sockets = new LinkedList<>();

	@Override
	public void run()
	{
		try (ServerSocket socket = new ServerSocket( PORT )) {
			final Socket clientSocket = socket.accept();
			sockets.add( clientSocket );
			new SceneHandler( this, socket.getInetAddress().getHostAddress(), clientSocket.getInputStream(),
				clientSocket.getOutputStream() ).start();
		} catch (final Exception e) {
			System.out.println( "Server has fallen;" );
		}
	}

	public void shutDownTheseIps(
		final List<String> ips
	)
	{
		final List<Socket> invalidSockets = sockets.stream()
			.filter( socket -> ips.contains( socket.getInetAddress().getHostAddress() ) )
			.collect( Collectors.toList() );
		sockets.removeAll( invalidSockets );
		closeSockets( invalidSockets );
	}

	private static void closeSockets(
		final List<Socket> sockets
	)
	{
		try {
			for (final Socket socket : sockets) {
				System.out.println( socket.getInetAddress().getHostAddress() + " is no longer between us." );
				if (!socket.isClosed()) {
					socket.close();
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
