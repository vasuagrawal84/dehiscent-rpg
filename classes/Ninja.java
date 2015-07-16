package classes;

import items.Item;
import items.Modifier;
import items.Rating;
import items.SlotType;
import items.Weapon;
import core.Player;
import core.Stat;

public class Ninja extends Player {

	public Ninja() {
		super();
	}
	
	@Override
	public void initBaseStats() {
		this.vitality = 6;
	    this.dexterity = 8;
	    this.strength = 4;
	    this.intelligence = 6;
	    this.physicalDefence = 0;
	    this.karma = 0;
	}

	@Override
	public void initVitals() {
		this.hp = this.getMaxHp();
	    this.xp = 0;
	    this.gold = 0;

	}

	@Override
	public void initEquipped() {
		  Item leatherTabbard = new Item("Leather Tabbard", 4, SlotType.CHEST, new Modifier(Stat.PHYS_DEF, 5));
		  Item ruggedGreaves = new Item("Rugged Greaves", 2, SlotType.LEGS, new Modifier(Stat.PHYS_DEF, 4));
		  Item ruggedGloves = new Item("Rugged Gloves", 2, SlotType.ARMS, new Modifier(Stat.PHYS_DEF, 4));
		  Item ruggedBoots = new Item("Rugged Boots", 2, SlotType.FEET, new Modifier(Stat.PHYS_DEF, 3));
		  
		  Weapon bluntShuriken = new Weapon("Blunt Shuriken", 3, SlotType.HAND, null, 7, 0, Rating.U, Rating.U, Rating.U);

		  leatherTabbard.setLoreText("A tough leather overcoat, dirty and battered by the elements. " +
		            "It features some fine embroidery along its seams but it's worn out and barely noticeable " +
		            "anymore, much like the lord who once wore it.");
		  ruggedGreaves.setLoreText("Made of an itchy wool, these are probably no use to anyone.");
		  ruggedGloves.setLoreText("Thick textile gloves, better for gardening than anything else.");
		  ruggedBoots.setLoreText("Made of a mix of hides taken from animals known to survive in " +
		            "difficult climates, these are well suited to long treks over rough terrain.");

		  bluntShuriken.setLoreText("A star-shaped steel concealed weapon that can be deadly if used correctly. Except "
		  		+ "this one isn't going to be piercing anyone anytime soon.");
		  
		  obtain(bluntShuriken);
		  attemptToEquip(leatherTabbard);
		  attemptToEquip(ruggedGreaves);
		  attemptToEquip(ruggedGloves);
		  attemptToEquip(ruggedBoots);
	}

}
