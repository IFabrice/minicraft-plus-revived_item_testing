import minicraft.core.io.InputHandler;
import minicraft.entity.mob.Player;
import minicraft.item.ArmorItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ArmorItemBlackBoxTest {

	private static final ArrayList<ArmorItem> armorItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof ArmorItem) {
				armorItems.add((ArmorItem) item);
			}
		}
	}

	// Error guessing on armor properties
	@Test
	public void testArmorHasExpectedProperties() {
		for (ArmorItem armor : armorItems) {
			assertFalse(armor.canAttack());
			assertFalse(armor.interactsWithWorld());
		}
	}

	// Testing that all available armor is retrieved
	@Test
	public void testArmorHasAllArmors() {
		assert(armorItems.size() == 5);
	}

	// Test that equipping armor works
	@Test
	public void testArmorEquipSucceeds() {
		Player player = new Player(null, new InputHandler());
		armorItems.get(0).interactOn(null, null, 0, 0, player, null);
        assertSame(player.curArmor, armorItems.get(0));
	}


	// Ensures that player cannot put on armor if they already have armor on; error guessing
	@Test
	public void testArmorCannotBeEquippedIfPlayerHasArmorOn() {
		Player player = new Player(null, new InputHandler());
		armorItems.get(0).interactOn(null, null, 0, 0, player, null);
		armorItems.get(1).interactOn(null, null, 0, 0, player, null);
		// ensure first armor equipped is not overwritten
        assertNotSame(player.curArmor, armorItems.get(1));
		assertSame(player.curArmor, armorItems.get(0));
	}

	// Armor should not modify existing player stats; error guessing
	@Test
	public void testArmorDoesNotAffectPlayerBaseHealth() {
		Player player = new Player(null, new InputHandler());
		int health = player.health;
		armorItems.get(0).interactOn(null, null, 0, 0, player, null);
		assertEquals(health, player.health);
	}

	@Test
	void testArmorCopyHasExpectedBehavior() {
		for (ArmorItem armor : armorItems) {
			Item item = armor.copy();
			assertTrue(item.equals(armor));
		}
	}

	// Partitioning on equals - same/different object
	@Test
	void testArmorEqualsTrue() {
		for (ArmorItem armor : armorItems) {
			ArmorItem a = armor;
			assertTrue(armor.equals(a));
		}
	}

	@Test
	void testArmorEqualsFalse() {
		Item item = Items.get("Grass");
		for (ArmorItem armor : armorItems) {
			assertFalse(armor.equals(item));
		}
	}
}
