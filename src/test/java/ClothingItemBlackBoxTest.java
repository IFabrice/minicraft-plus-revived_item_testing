import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.BucketItem;
import minicraft.item.ClothingItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ClothingItemBlackBoxTest {
	private static final ArrayList<ClothingItem> clothingItems = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof ClothingItem) {
				clothingItems.add((ClothingItem) item);
			}
		}
	}

	@Test
	public void testClothingItemsHaveExpectedBehavior() {
		for (ClothingItem clothing : clothingItems) {
			assertFalse(clothing.canAttack());
			assertFalse(clothing.interactsWithWorld());
		}
	}

	@Test
	public void testClothingSuccessfullyChanged() {
		Player mockPlayer = mock(Player.class);
		Tile mockTile = mock(Tile.class);
		Level mockLevel = mock(Level.class);
		for (ClothingItem clothing : clothingItems) {
			assertTrue(clothing.interactOn(mockTile, mockLevel, 0, 0, mockPlayer, Direction.NONE));
		}


	}
}
