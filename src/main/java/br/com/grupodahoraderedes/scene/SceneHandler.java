package br.com.grupodahoraderedes.scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import br.com.grupodahoraderedes.player.Player;
import br.com.grupodahoraderedes.users.UserConnectionServer;

public class SceneHandler
	extends
		Thread
{
	private final Player player = new Player();

	private final UserConnectionServer server;
	private final String playerIp;
	private final BufferedReader reader;
	private final PrintWriter writer;

	public SceneHandler(
		final UserConnectionServer server,
		final String playerIp,
		final InputStream input,
		final OutputStream output
	)
	{
		this.server = server;
		this.playerIp = playerIp;
		this.reader = new BufferedReader( new InputStreamReader( input ) );
		this.writer = new PrintWriter( output, true );
	}

	@Override
	public void run()
	{
		try {
			SceneManager.start();
			gameLoop();
		} catch (final Exception e) {
			System.out.println( playerIp + ": it's game has been closed." );
		}
	}

	private void gameLoop()
		throws IOException
	{
		Scene nextScene = null;
		Scene scene = SceneManager.getStartingScene();
		while (true) {
			writeSceneToPlayer( scene );
			while (true) {
				final String choice = waitForChoiceInput( scene.optionsSize() );
				nextScene = SceneManager.getChosenScene( player, writer, scene, choice );
				if (nextScene != null && !nextScene.equals( scene )) {
					break;
				}
			}
			scene = nextScene;
			applySceneConsequencesOnPlayer( scene );
		}
	}

	private void writeSceneToPlayer(
		final Scene scene
	)
	{
		final String[] textLines = scene.getText().split( "\n" );
		final int numberOfOptions = scene.optionsSize();
		writer.println( textLines.length + numberOfOptions );

		for (final String textLine : textLines) {
			writer.println( textLine );
		}
		for (int i = 1; i <= numberOfOptions; i++) {
			writer.println( i + ") " + scene.getOption( i ) );
		}
	}

	private void applySceneConsequencesOnPlayer(
		final Scene scene
	)
	{
		/**
		 * Para cena atual, aplicar consequências no player.
		 */
	}

	private String waitForChoiceInput(
		final int numberOfChoices
	)
		throws IOException
	{
		while (true) {
			final String choice = reader.readLine();
			if (isInvalidChoice( choice )) {
				askToEnterAValidChoice();
				continue;
			}
			if (choice.equals( "exit" )) {
				server.shutDownTheseIps( Arrays.asList( playerIp ) );
				throw new RuntimeException();
			}
			if (isOneOfDefaultChoices( choice )) {
				return choice;
			}
			final Integer numberChoice = Integer.valueOf( choice );
			if (numberChoice < 1 || numberChoice > numberOfChoices) {
				askToEnterAValidChoice();
				continue;
			}
			return choice;
		}
	}

	private boolean isInvalidChoice(
		final String choice
	)
	{
		return choice == null || choice.isEmpty();
	}

	private void askToEnterAValidChoice()
	{
		writer.println( "Please enter a valid choice." );
	}

	private boolean isOneOfDefaultChoices(
		final String choice
	)
	{
		switch (choice.split( " " )[ 0 ]) {
			case "inventory":
			case "equip":
				return true;
			default:
				return false;
		}
	}
}
