package map.cells;

import java.util.stream.IntStream;

import items.Item;
import items.SlotType;
import core.CombatResolver;
import core.IO;
import core.Player;
import enemies.Enemy;

public class DrunkenDog implements Cell {

	private int encounterValue = 0;
	private static final int KARMA_VALUE = 5;
	private static final int GOLD_VALUE = 20;

	@Override
	public char getMapIcon() {
		return '.';
	}

	@Override
	public void explore(Player player) {
		if(encounterValue == 1) {
			IO.printAsPara("The dog stands infront of the drunk, snarling.\n");
			if(IO.getAffirmative("Do you wish to attack the Dog? ")) {
				Enemy germanShepherd = new Enemy("German Shepherd", 16, 15, 25, 0);
				if(CombatResolver.resolveCombat(player, germanShepherd)) {
					encounterValue = 2;
				}
			} else if(IO.getAffirmative("Distracting it might work. Would you like to give it something? ")) {
				//TODO give the dog a consumable
				Item[] possibleConsumables = player.getAllKnownItems()
						.parallelStream()
						.filter(i -> i.getSlotType() == SlotType.ACCESSORY)
						.sorted()
						.toArray(Item[]::new);
				IntStream.range(0, possibleConsumables.length)
	              .forEachOrdered(i -> IO.println(i + ": " + possibleConsumables[i].getName()));
				
				if(possibleConsumables.length < 1) {
					IO.println("You have no accessories to distract the dog with!");
				} else {
					double d = IO.getNumberWithinRange(
				              "\nChoose an item to distract the dog with. \n",
				              0, possibleConsumables.length - 1, true);
				    if (d > Double.NEGATIVE_INFINITY) {
				        player.lose(possibleConsumables[(int) d]);
				        encounterValue = 2;
				    } else {
							IO.printAsPara("You back away slowly. ");
					}
				}
			} else {
				IO.printAsPara("You back away slowly. ");
			}
		}
		if(encounterValue == 2) {
			IO.printAsPara("You inspect the drunk's right pocket and find a leather wallet with a " +
					"organ donor card, picture of two small children and a reciept for a donation to " +
					"a cancer research charity. There are also some gold coins in there.");
			if(IO.getAffirmative("\nDo you take the gold? ")) {
				IO.printAsPara("You become slightly richer but the guilt of taking from a decent person " +
						"eats away at you. What goes around, comes around. ");
				player.addGold(GOLD_VALUE);
				player.subKarma(KARMA_VALUE);
			} else {
				IO.printAsPara("You walk away telling yourself that you made the right decision. But would the" +
						"the gold have come in handy? What goes around, comes around. ");
				player.addKarma(KARMA_VALUE);
			}
			encounterValue = -1;
		}
	}

	@Override
	public void event(Player player) {
		if (encounterValue < 0) {
			IO.println("\nThe wind whistles past your ears as if to remind you of your past. ");
		} else if (encounterValue == 0) {
			IO.println("\nJust a old, bearded drunk sleeping, with his German Shepherd, on the floor. "
					+ "Who are you to judge his life choices? ");
		} else {
			IO.println("\nThe drunk is still asleep. ");
		}
		if (encounterValue == 0) {
			IO.printAsPara("As you approach to inspect, the dog wakes up.");
			encounterValue = 1;
		}
	}

	@Override
	public boolean goNorth() {
		return true;
	}

	@Override
	public boolean goSouth() {
		return true;
	}

	@Override
	public boolean goEast() {
		return true;
	}

	@Override
	public boolean goWest() {
		return true;
	}

}
