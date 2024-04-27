import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.FoodItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
		assert(foodItems.size() == 10);
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
		Player player = new Player(null, new InputHandler());
		player.hunger = 1;
		int hunger = 1;
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		foodItems.get(0).count = 2;
		assertTrue(foodItems.get(0).interactOn(grassTile, null, 0, 0, player, Direction.NONE));
		assertTrue(player.hunger > hunger);
	}

	// Error guessing: Health and hunger are similar
	@Test
	public void testFoodDoesNotAffectPlayerBaseHealth() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		foodItems.get(0).count = 2;
		int health = player.health;
		foodItems.get(0).interactOn(grassTile, null, 0, 0, player, Direction.NONE);
        assertEquals(player.health, health);
	}

	@Test
	public void testFoodCopyHasExpectedBehavior() {
		for (FoodItem food : foodItems) {
			Item item = food.copy();
			assertTrue(item.equals(food));
		}
	}

	// Partitioning for equals - same/different object
	@Test
	public void testFoodEqualsTrue() {
		for (FoodItem food : foodItems) {
			FoodItem f = food;
			assertTrue(food.equals(f));
		}
	}

	@Test
	public void testFoodEqualsFalse() {
		Item item = Items.get("grass");
		for (FoodItem food : foodItems) {
			assertFalse(food.equals(item));
		}
	}
}
