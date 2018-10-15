package br.com.grupodahoraderedes.scene;

import java.util.List;

public class Scene
{
	private final int id;
	private final String text;
	private final List<String> options;
	private final List<Integer> references;

	private Scene(
		final int id,
		final String text,
		final List<String> options,
		final List<Integer> references
	)
	{
		this.id = id;
		this.text = text;
		this.options = options;
		this.references = references;
	}

	public static Scene of(
		final int id,
		final String text,
		final List<String> options,
		final List<Integer> references
	)
	{
		return new Scene( id, text, options, references );
	}

	public int getId()
	{
		return id;
	}

	public String getText()
	{
		return text;
	}

	public String getOption(
		final int option
	)
	{
		return options.get( option - 1 );
	}

	public int optionsSize()
	{
		return options.size();
	}

	public void addOption(
		final String option
	)
	{
		this.options.add( option );
	}

	public Integer getReference(
		final int reference
	)
	{
		return references.get( reference - 1 );
	}

	public void addReference(
		final int reference
	)
	{
		this.references.add( reference );
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append( "Text: " + getText() + ",\n" );
		sb.append( "Options: \n" );
		int i;
		for (i = 0; i < options.size() - 1; i++) {
			sb.append( i + 1 + ") " + options.get( i ) + "\n" );
		}
		sb.append( i + 1 + ") " + options.get( i ) );
		return sb.toString();
	}
}
