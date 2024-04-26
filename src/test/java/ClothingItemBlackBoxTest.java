import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.ClothingItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClothingItemBlackBoxTest {
	private static final ArrayList<ClothingItem> clothingItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof ClothingItem) {
				clothingItems.add((ClothingItem) item);
			}
		}
	}

	// Error guessing on expected behavior
	@Test
	public void testClothingItemsHaveExpectedBehavior() {
		for (ClothingItem clothing : clothingItems) {
			assertFalse(clothing.canAttack());
			assertFalse(clothing.interactsWithWorld());
		}
	}


	// testing expected behavior; player should be able to change clothes
	@Test
	public void testClothingSuccessfullyChanged() {
		Player player = new Player(null, new InputHandler());
		for (ClothingItem clothing : clothingItems) {
			assertTrue(clothing.interactOn(null, null, 0, 0, player, Direction.NONE));
		}
	}

	// Testing expected functionality for copy
	@Test
	public void testClothingCopyIsSuccessful() {
		for (ClothingItem clothing : clothingItems) {
			Item item = clothing.copy();
			assertTrue(item.equals(clothing));
		}
	}

	// Partitioning on equals - same/different clothing
	@Test
	public void testClothingEqualsTrue() {
		for (ClothingItem clothing : clothingItems) {
			ClothingItem c = clothing;
			assertTrue(clothing.equals(c));
		}
	}

	@Test
	public void testClothingEqualsFalse() {
		Item item = Items.get("Grass");
		for (ClothingItem clothing : clothingItems) {
			assertFalse(clothing.equals(item));
		}
	}
}
