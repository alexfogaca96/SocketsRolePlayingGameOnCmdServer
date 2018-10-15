package br.com.grupodahoraderedes.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WeatherBroadcastServer
	extends
		Thread
{
	private static final int PORT = 23413;
	private static final String INET_ADDRESS = "230.0.0.0";
	private static final int TIME_INTERVAL_IN_MILIS = 30_000;

	@Override
	public void run()
	{
		while (true) {
			try {
				Thread.sleep( TIME_INTERVAL_IN_MILIS );
				multicast( WeatherReport.getRandomWeatherReportMessage() );
			} catch (final IOException | InterruptedException e) {
				System.out.println( "Ninguém vai saber do clima." );
			}
		}
	}

	public void multicast(
		final String multicastMessage
	)
		throws IOException
	{
		final DatagramSocket socket = new DatagramSocket();
		final InetAddress group = InetAddress.getByName( INET_ADDRESS );
		final byte[] buf = multicastMessage.getBytes();

		final DatagramPacket packet = new DatagramPacket( buf, buf.length, group, PORT );
		socket.send( packet );
		socket.close();
	}

	enum WeatherReport
	{
		SOL
		{
			@Override
			String message()
			{
				return "SOL: todos ficam alegres!";
			}
		},
		CHUVA
		{
			@Override
			String message()
			{
				return "CHUVA: todos ficam tristes!";
			}
		},
		VENTO
		{
			@Override
			String message()
			{
				return "VENTO: ninguém liga!";
			}
		},
		CHAO_PEGANDO_FOGO
		{
			@Override
			String message()
			{
				return "CHAO_PEGANDO_FOGO: fodeu!";
			}
		};

		abstract String message();

		public static String getRandomWeatherReportMessage()
		{
			final int randomMessage = ( int ) ( Math.random() * 4 ) + 1;
			switch (randomMessage) {
				case 1:
					return SOL.message();
				case 2:
					return CHUVA.message();
				case 3:
					return VENTO.message();
				case 4:
					return CHAO_PEGANDO_FOGO.message();
				default:
					return SOL.message();
			}
		}
	}
}
