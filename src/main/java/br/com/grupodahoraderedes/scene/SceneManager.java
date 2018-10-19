package br.com.grupodahoraderedes.scene;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.grupodahoraderedes.player.Player;

abstract class SceneManager
{
	private static final Path SCENES_TXT = Paths.get( "Scenes/Scenes.txt" );
	private static List<Scene> SCENES;

	private SceneManager()
	{
		throw new AssertionError();
	}

	public static void start()
	{
		SCENES = interpretScenesFromRawScenes( readRawScenes( SCENES_TXT ) );
	}

	public static Scene getStartingScene()
	{
		return SCENES.get( 0 );
	}
	
	public static Scene getSceneByID(int ID) {
		for (final Scene scene : SCENES) {
			if (scene.getId() == ID) {
				return scene;
			}
		}
		throw new RuntimeException();
	}

	public static Scene getChosenScene(
		final Player player,
		final PrintWriter writer,
		final Scene currentScene,
		final String choice
	)
	{
		if (!isInteger( choice )) {
			showTextOfDefaultChoices( player, writer, choice );
			return currentScene;
		}
		final int choiceNumber = Integer.parseInt( choice );
		if (choiceNumber < 1 || choiceNumber > currentScene.optionsSize()) {
			return null;
		}
		
		final int sceneReference = currentScene.getReference( choiceNumber );
		for (final Scene scene : SCENES) {
			if (scene.getId() == sceneReference) {
				return scene;
			}
		}
		throw new RuntimeException(
			"The " + choiceNumber + " from Scene(" + currentScene.getId() + ") does not have a valid reference." );
	}

	private static void showTextOfDefaultChoices(
		final Player player,
		final PrintWriter writer,
		final String choice
	)
	{
		final String[] splittedChoice = choice.split( " " );
		switch (splittedChoice[ 0 ]) {
			case "inventory":
				if (splittedChoice.length == 2) {
					player.addToInventory( splittedChoice[ 1 ] );
				}
				final List<String> inventory = player.getInventory();
				writer.println( inventory.size() + 2 );
				writer.println( "Inventory {" );
				for (final String item : inventory) {
					writer.println( "  " + item );
				}
				writer.println( "}" );
				break;
			case "equip":
				if (splittedChoice.length == 2) {
					player.equipItem( splittedChoice[ 1 ] );
				}
				final List<String> gear = player.getGear();
				writer.println( gear.size() + 2 );
				writer.println( "Equip {" );
				for (final String equipment : gear) {
					writer.println( "  " + equipment );
				}
				writer.println( "}" );
				break;
			default:
				writer.print( "Essa ação não pode ser executada..." );
		}
	}

	private static String readFromTag(
		final String tag,
		final String text
	)
	{
		final Pattern pattern = Pattern.compile( "<" + tag + ">(.+?)</" + tag + ">" );
		final Matcher matcher = pattern.matcher( text );
		if (matcher.find()) {
			return matcher.group( 1 );
		}
		return null;
	}

	private static List<Scene> interpretScenesFromRawScenes(
		final List<String> rawScenes
	)
	{
		final List<Scene> scenes = new LinkedList<>();
		for (final String rawScene : rawScenes) {
			final Integer id = Integer.parseInt( readFromTag( "id", rawScene ) );
			final String text = readFromTag( "t", rawScene );

			final List<String> options = new LinkedList<>();
			final List<Integer> references = new LinkedList<>();

			int optionNumber = 1;
			while (true) {
				final String option = readFromTag( "o" + optionNumber, rawScene );
				final String reference = readFromTag( "l" + optionNumber, rawScene );
				if (option == null || reference == null) {
					break;
				}
				options.add( option );
				references.add( Integer.parseInt( reference ) );
				optionNumber++;
			}
			scenes.add( Scene.of( id, text, options, references ) );
		}
		return scenes;
	}

	private static List<String> readRawScenes(
		final Path path
	)
	{
		try {
			return Files.readAllLines( path, StandardCharsets.ISO_8859_1 );
		} catch (final IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public static boolean isInteger(
		final String string
	)
	{
		return isInteger( string, 10 );
	}

	public static boolean isInteger(
		final String string,
		final int radix
	)
	{
		if (string.isEmpty()) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (i == 0 && string.charAt( i ) == '-') {
				if (string.length() == 1) {
					return false;
				} else {
					continue;
				}
			}
			if (Character.digit( string.charAt( i ), radix ) < 0) {
				return false;
			}
		}
		return true;
	}
}
