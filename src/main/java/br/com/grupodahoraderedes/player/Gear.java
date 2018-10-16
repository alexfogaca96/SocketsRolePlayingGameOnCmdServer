package br.com.grupodahoraderedes.player;

import java.util.LinkedList;
import java.util.List;

public class Gear
{
	private final List<String> itensEquiped = new LinkedList<>();

	public Gear()
	{
		itensEquiped.add( "Tunic" );
		itensEquiped.add( "vozerão do cacete" );
	}

	public void equip(
		final String item
	)
	{
		itensEquiped.add( item );
	}

	public List<String> equipments()
	{
		return itensEquiped;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append( "Gear: [" );
		for (final String item : itensEquiped) {
			sb.append( "\t" + item + "\n" );
		}
		sb.append( "]" );
		return sb.toString();
	}
}
