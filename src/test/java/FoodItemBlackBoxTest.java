import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.FoodItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.tile.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodItemBlackBoxTest {
	private static final ArrayList<FoodItem> foodItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof FoodItem) {
				foodItems.add((FoodItem) item);
			}
		}
	}

	// Tests that foodItems can be retrieved - fault here: maybe there is a Food missing?
	@Test
	public void testFoodItemQuantityIsValid() {
		assert(foodItems.size() == 11);
	}

	// Error guessing for food's expected behavior
	@Test
	public void testFoodItemHaveExpectedBehavior() {
		for (FoodItem food : foodItems) {
			assertFalse(food.canAttack());
			assertFalse(food.interactsWithWorld());
		}
	}

	// Expected functionality of food for the player tested here
	@Test
	public void testFoodItemRestoresHunger() {
		Player mockPlayer = mock(Player.class);
		mockPlayer.hunger = 1;
		int hunger = 1;
		Tile mockTile = mock(Tile.class);
		foodItems.get(0).count = 2;
		when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		assertTrue(foodItems.get(0).interactOn(mockTile, null, 0, 0, mockPlayer, Direction.NONE));
		assertTrue(mockPlayer.hunger > hunger);


	}
}
