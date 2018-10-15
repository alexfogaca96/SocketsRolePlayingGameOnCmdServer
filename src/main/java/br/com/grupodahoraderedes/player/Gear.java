package br.com.grupodahoraderedes.player;

import java.util.Collections;
import java.util.List;

public class Gear
{
	private final List<String> itensEquiped = Collections.emptyList();

	public void equip(
		final String item
	)
	{
		itensEquiped.add( item );
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
