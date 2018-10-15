package br.com.grupodahoraderedes.keepalive;

import static br.com.grupodahoraderedes.ServerApplication.tellConnectionServerToShutDownTheseIps;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class KeepAliveChecker
	extends
		Thread
{
	private final List<SocketAvailability> keepAliveSockets = new LinkedList<>();

	private static final int TIME_INTERVAL_IN_MILIS = 5_000;
	private static final int TIME_INTERVAL_IN_SECONDS = TIME_INTERVAL_IN_MILIS / 1000;
	private static final int MAX_FAILED_RESPONSES = 3;

	@Override
	public void run()
	{
		while (true) {
			try {
				Thread.sleep( TIME_INTERVAL_IN_MILIS );
				System.out.println( "Starting to check keep alive times." );
				final List<SocketAvailability> socketsToThrowOut = updateList();
				System.out.println( socketsToThrowOut.size() + " invalid sockets." );

				tellConnectionServerToShutDownTheseIps(
					socketsToThrowOut.stream().map( socket -> socket.getSocket().getInetAddress().getHostAddress() )
						.collect( Collectors.toList() ) );
				removeAllInvalidSocketsAndCloseThem( socketsToThrowOut );
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public List<SocketAvailability> updateList()
	{
		return keepAliveSockets.stream().filter( socket -> socketTimeIsInvalid( socket.getTime() ) ).filter( socket -> {
			socket.incrementFailedResponses();
			return socket.becameInvalid();
		} ).collect( Collectors.toList() );
	}

	public boolean ipIsAlreadyConnected(
		final String ip
	)
	{
		for (final SocketAvailability socket : keepAliveSockets) {
			if (socket.isMyIp( ip )) {
				return true;
			}
		}
		return false;
	}

	public synchronized void addSocket(
		final Socket socket
	)
	{
		keepAliveSockets.add( new SocketAvailability( socket, LocalDateTime.now(), 0 ) );
		System.out.println( "Adding socket " + socket.getInetAddress().getHostAddress() + "..." );
	}

	public void updateLastKeepAliveTime(
		final String ip,
		final LocalDateTime time
	)
	{
		for (final SocketAvailability socket : keepAliveSockets) {
			if (socket.isMyIp( ip )) {
				socket.updateTime( time );
				break;
			}
		}
	}

	private void removeAllInvalidSocketsAndCloseThem(
		final List<SocketAvailability> invalidSockets
	)
	{
		keepAliveSockets.removeAll( invalidSockets );
		invalidSockets.forEach( socketAval -> {
			try {
				final Socket socket = socketAval.getSocket();
				System.out.println( "Closing socket " + socket.getInetAddress().getHostAddress() + "..." );
				if (!socket.isClosed()) {
					final PrintWriter writer = new PrintWriter( socket.getOutputStream(), true );
					writer.println( "ow, reinicia seu jogo ai" );
					socket.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} );
	}

	private static boolean socketTimeIsInvalid(
		final LocalDateTime time
	)
	{
		return time.isBefore( LocalDateTime.now().minusSeconds( TIME_INTERVAL_IN_SECONDS ) );
	}

	private static class SocketAvailability
	{
		private final Socket socket;
		private LocalDateTime time;
		private int failedResponses;

		private SocketAvailability(
			final Socket socket,
			final LocalDateTime time,
			final int failedResponses
		)
		{
			this.socket = socket;
			this.time = time;
			this.failedResponses = failedResponses;
		}

		public void updateTime(
			final LocalDateTime time
		)
		{
			this.time = time;
		}

		public void incrementFailedResponses()
		{
			failedResponses++;
		}

		public boolean becameInvalid()
		{
			return failedResponses > MAX_FAILED_RESPONSES;
		}

		public Socket getSocket()
		{
			return socket;
		}

		public LocalDateTime getTime()
		{
			return time;
		}

		public boolean isMyIp(
			final String ip
		)
		{
			return socket.getInetAddress().getHostAddress().equals( ip );
		}
	}
}
