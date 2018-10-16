package br.com.grupodahoraderedes.player;

import java.util.LinkedList;
import java.util.List;

public class Inventory
{
	private final List<String> inventoryItens = new LinkedList<>();

	public Inventory()
	{
		inventoryItens.add( "Torch" );
		inventoryItens.add( "Rope" );
		inventoryItens.add( "restinho do marmitex" );
	}

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

	public List<String> inventoryItens()
	{
		return inventoryItens;
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
