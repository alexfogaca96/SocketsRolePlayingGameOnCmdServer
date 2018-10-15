package br.com.grupodahoraderedes.player;

public class Player
{
	private Profession profession;
	private String name;
	private boolean isUndead;
	private int health;

	private final Inventory inventory;
	private final Gear gear;

	public Player()
	{
		this.profession = null;
		this.name = "";
		isUndead = false;
		health = 30;
		inventory = new Inventory();
		gear = new Gear();
	}

	public void setName(
		final String name
	)
	{
		if (this.name == null) {
			this.name = name;
		}
	}

	public void setProfession(
		final int professionChoice
	)
	{
		if (this.profession == null) {
			this.profession = Profession.getProfession( professionChoice );
		}
	}

	public String getName()
	{
		return name;
	}

	public String getProfession()
	{
		return profession.name();
	}

	private boolean isUndead()
	{
		return isUndead;
	}

	public int getHealth()
	{
		return health;
	}

	public String getInventory()
	{
		return inventory.toString();
	}

	public String getGear()
	{
		return gear.toString();
	}

	public void equipItem(
		final String item
	)
	{
		if (item == null || item.isEmpty()) {
			return;
		}
		gear.equip( item );
	}

	public void addToInventory(
		final String item
	)
	{
		if (item == null || item.isEmpty()) {
			return;
		}
		inventory.add( item );
	}

	public boolean removeFromInventory(
		final String item
	)
	{
		if (item == null || item.isEmpty()) {
			return false;
		}
		return inventory.remove( item );
	}

	public boolean loseHealthAndBecomeAnUndead(
		final int damage
	)
	{
		health -= damage;
		if (isUndead) {
			return true;
		}
		if (health <= 0) {
			this.name += ", the Undead.";
			isUndead = true;
			return true;
		}
		return false;
	}

	public void gainHealth(
		final int healing
	)
	{
		if (isUndead) {
			health -= healing;
		} else {
			health += healing;
		}
	}

	enum Profession
	{
		BAKER,
		TAILOR,
		BUTCHER,
		HEADHUNTER,
		BUSINESSMAN,
		INDIGENTE_EM_PORTUGUES;

		public static Profession getProfession(
			final int number
		)
		{
			switch (number) {
				case 1:
					return BAKER;
				case 2:
					return TAILOR;
				case 3:
					return BUTCHER;
				case 4:
					return HEADHUNTER;
				case 5:
					return BUSINESSMAN;
				default:
					return INDIGENTE_EM_PORTUGUES;
			}
		}

		public static String getAllProfessions()
		{
			final StringBuilder sb = new StringBuilder();
			final Profession[] professions = values();
			for (int i = 1; i < professions.length; i++) {
				sb.append( i + ") " + professions[ i ] + "\n" );
			}
			return sb.toString();
		}
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append( getName() + "[\n" );
		sb.append( "\tProfession: " + getProfession() + "\n" );
		sb.append( "\t" + ( isUndead() ? "SUPREME HEALTH: " : "Health: " ) + getHealth() + "\n" );
		sb.append( "\tGear: " + getGear() + "\n" );
		sb.append( "\tInventory: " + getInventory() + "\n" );
		sb.append( "]\n" );
		return sb.toString();
	}
}
