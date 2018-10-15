package br.com.grupodahoraderedes.player;

import java.util.Collections;
import java.util.List;

public class Inventory
{
	private final List<String> inventoryItens = Collections.emptyList();

	public void add(
		final String item
	)
	{
		inventoryItens.add( item );
	}

	public boolean remove(
		final String item
	)
	{
		return inventoryItens.remove( item );
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append( "Inventory: [" );
		for (final String item : inventoryItens) {
			sb.append( "\t" + item + "\n" );
		}
		sb.append( "]" );
		return sb.toString();
	}
}
