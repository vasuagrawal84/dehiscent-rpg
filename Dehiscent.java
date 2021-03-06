import classes.*;
import core.IO;
import core.Player;
import map.Map;
import map.cells.*;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This is the main class for the project where all
 * core objects are initialised (player, map, cells).
 */
public class Dehiscent {

  /**
   * Entry point for the game. This contains the main
   * loop which consists of:
   *
   *    1. Get player input
   *    2. Execute event based on input
   *    3. If necessary, update player position
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {

    // Suppress console output during setup
    OutputStream realSystemOut = System.out;
    System.setOut(IO.getNullPrintStream());

    Map overworld = createMap();
    Player p = null;

    // Resume console output
    System.setOut(new PrintStream(realSystemOut));
	
    while(p == null) {
    	String chosenClass = chooseClass();
		if(chosenClass.contains("wanderer")) {
			p = new Wanderer();
		} else if(chosenClass.contains("shinobi")) {
			p = new Shinobi();
		}
	}

    for (; ; ) {
      overworld.printKnownMap(p);

      Cell currentCell = overworld.fetchCell(p.getPosition());
      currentCell.event(p);

      Cell previousCell = currentCell;
      while (currentCell == previousCell) {
        String decision = IO.getDecision("\nWhat will you do?\n");
        switch (decision) {
          case "w":
          case "go north":
            if (currentCell.goNorth())
              p.goNorth();
            break;
          case "a":
          case "go west":
            if (currentCell.goWest())
              p.goWest();
            break;
          case "s":
          case "go south":
            if (currentCell.goSouth())
              p.goSouth();
            break;
          case "d":
          case "go east":
            if (currentCell.goEast())
              p.goEast();
            break;
          case "v":
          case "view":
            overworld.printKnownMapAlongsideStats(p);
            break;
        }
        if (decision.startsWith("view") || decision.startsWith("v ")) {
          if (decision.contains(" map")) {
            overworld.printKnownMap(p);
          }
          if (decision.contains(" position")) {
            IO.println("You're at " + p.getPosition().toString());
          }
          if (decision.contains(" stats")) {
            IO.println(p.statsToString());
          }
          if (decision.contains(" base")) {
            IO.println(p.baseStatsToString());
          }
          if (decision.contains(" vitals")) {
            IO.println(p.vitalsToString());
          }
          if (decision.contains(" current")) {
            IO.println(p.verboseEquippedToString());
          }
          if (decision.contains(" equip")) {
            IO.println(p.equippedToString());
          }
          if (decision.contains(" inv")) {
            IO.println(p.inventoryToString());
          }
          if (decision.contains(" controls")) {
            IO.println(controlsToString());
          }
        } else if (decision.startsWith("use") || decision.startsWith("u ")) {
          String itemName = decision.substring(decision.indexOf(" ")).trim();
          p.attemptToUse(itemName);
        } else if (decision.startsWith("inspect") || decision.startsWith("i ")) {
          String itemName = decision.substring(decision.indexOf(" ")).trim();
          p.attemptToInspect(itemName);
        } else if (decision.startsWith("explore") || decision.equals("x")) {
          currentCell.explore(p);
        } else {
          if (decision.startsWith("equip") || decision.startsWith("e ")) {
            String itemName = decision.substring(decision.indexOf(" ")).trim();
            p.attemptToEquip(itemName);
          } else if (decision.startsWith("unequip") || decision.startsWith("ue ")) {
            String itemName = decision.substring(decision.indexOf(" ")).trim();
            p.attemptToUnequip(itemName);
          } else if (decision.startsWith("quit")) {
            if (IO.getAffirmative("Saving isn't implemented yet! Are you sure you want to quit? ('yes' to quit)\n")) {
              System.exit(0);
            }
          }
        }
        currentCell = overworld.fetchCell(p.getPosition());
      }
    }
  }

  /**
   * This is where the map is initialised and where new cells
   * can be added to it.
   *
   * @see map.Map
   * @see map.Quadrant
   * @see map.cells.Cell
   *
   * @return a new map containing blank cells and all currently
   * created cells.
   */
  public static Map createMap() {
    Map overworld = new Map();
    overworld.setCell(0, 0, new HomeCell());
    overworld.setCell(0, -1, new FromRuggedToRiches());
    overworld.setCell(1, 1, new LittleGrocerShop());
    overworld.setCell(-1, 1, new ALittleSomethinSomethin());
    overworld.setCell(1, -1, new NuisancePig());
    overworld.setCell(1, 2, new DrunkenDog());
    return overworld;
  }

  /**
   * Prints out all controls currently assigned which can
   * be used during the main loop.
   *
   * @return a string containing all the controls
   */
  public static String controlsToString() {
    return IO.formatBanner(IO.BOX_WIDTH) +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "COMMAND", "HOTKEY", "EFFECT") +
            IO.formatBanner(IO.BOX_WIDTH) +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "go north", "w", "Go north") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "go south", "a", "Go south") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "go west", "s", "Go west") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "go east", "d", "Go east") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "explore", "x", "Explore the area") +
            IO.formatBanner(IO.BOX_WIDTH) +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "use <item>", "u", "Use an item") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "equip <item>", "e", "Equip an item") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "unequip <item>", "ue", "Unequip an item") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "inspect <item>", "i", "Unequip an item") +
            IO.formatBanner(IO.BOX_WIDTH) +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "view <options>", "v", "View summary of all") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  map", "", "Check the map") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  inv", "", "View inventory") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  equip", "", "See all equipped") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  current", "", "Inspect all equipped") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  position", "", "Check position") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  stats", "", "View stats") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  base", "", "View base stats") +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "  controls", "", "See this menu") +
            IO.formatBanner(IO.BOX_WIDTH) +
            IO.formatColumns(IO.BOX_WIDTH, true, true, "[0-9]", "", "Select menu options") +
            IO.formatBanner(IO.BOX_WIDTH);
  }
    
  /**
   * Prints a table of the classes (Wanderer, Shinobi etc.) and asks one the player wants to use. 
   * Asks if the player wants to view the items for the class and if they want to confirm the choice.
   * 
   * @return a string containing the confirmed choice of class.
   */
    public static String chooseClass() {
    
      //Disable console output so temp player object initialisation isn't shown to player
      OutputStream realSystemOut = System.out;
      System.setOut(IO.getNullPrintStream());
	  Player wanderer = new Wanderer();
	  Player shinobi = new Shinobi();
	  System.setOut(new PrintStream(realSystemOut));
	  
	  //Create a string that contains a new table showing the stats of the class
	  String classTable = IO.formatBanner(IO.BOX_WIDTH) + 
	  IO.formatColumns(IO.BOX_WIDTH, true, true, "CLASS", "VITALITY", "DEXTERITY", "STRENGTH", "INTELLIGENCE", "PHYS DEFENCE") + 
	  IO.formatBanner(IO.BOX_WIDTH) + 
	  IO.formatColumns(IO.BOX_WIDTH, true, true, "Wanderer", 
			  checkStatLevel(wanderer.getBaseVit()), 
			  checkStatLevel(wanderer.getBaseDex()), 
			  checkStatLevel(wanderer.getBaseStr()), 
			  checkStatLevel(wanderer.getBaseInt()), 
			  checkStatLevel(wanderer.getPhysDef())) +
	  IO.formatColumns(IO.BOX_WIDTH, true, true, "Shinobi", 
			  checkStatLevel(shinobi.getBaseVit()), 
			  checkStatLevel(shinobi.getBaseDex()), 
			  checkStatLevel(shinobi.getBaseStr()), 
			  checkStatLevel(shinobi.getBaseInt()), 
			  checkStatLevel(shinobi.getPhysDef())) +
	  IO.formatBanner(IO.BOX_WIDTH);
	  
	  // Confirm whether the player wants to view the starting items and if they
	  // want to choose the class
	  for( ; ; ) {
		  IO.println(classTable);
		  
		  String chosenClass = IO.getDecision("Choose a class... ");
		  
		  while(!(chosenClass.toLowerCase().contains("wanderer") || chosenClass.toLowerCase().contains("shinobi"))) {
			  IO.println("Class not recognised.");
			  chosenClass = IO.getDecision("Choose a class... ");
		  }
		  
		  if(IO.getAffirmative("Would you like to view " + chosenClass + "'s starting items? ")) {
			  if(chosenClass.contains("wanderer")) {
				  IO.println(wanderer.equippedToString());
				  IO.println(wanderer.inventoryToString());
			  } else if(chosenClass.contains("shinobi")) {
				  IO.println(shinobi.equippedToString());
				  IO.println(shinobi.inventoryToString());
			  }
		  }
		  
		  if(IO.getAffirmative("Choose " + chosenClass + "?")) {
			  return chosenClass;
		  }
	  }
	  
  }
  
    /**
     * Checks which range (High, Average, Low) the player's base stat is in.
     * @param stat is the integer value of the stat in question.
     * @return a string containing the range (High, Average, Low).
     */
  public static String checkStatLevel(int stat) {
	  if(stat > 7) {
		  return "High";
	  } else if(stat < 3) {
		  return "Low";
	  } else {
		  return "Average";
	  }
  }
}
