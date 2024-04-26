import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.FishingRodItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FishingRodItemBlackBoxTest {
	private static final ArrayList<FishingRodItem> fishingRodItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof FishingRodItem) {
				fishingRodItems.add((FishingRodItem) item);
			}
		}
	}

	// Error guessing: Checking that boolean states of Item are valid
	@Test
	public void testFishingRodItemHaveExpectedBehavior() {
		for (FishingRodItem fishingRod : fishingRodItems) {
			assertFalse(fishingRod.canAttack());
			assertTrue(fishingRod.interactsWithWorld());
		}
	}

	// Error guesing: Checking that all available types of Item are retrieved
	@Test
	public void testFishingRodQuantityMatchesExpected() {
		assert(fishingRodItems.size() == 4);
	}

	// Testing expected functionality on water tiles
	@Test
	public void testFishingRodItemCanFish() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile waterTile = Tiles.get("water");
        for (FishingRodItem fishingRodItem : fishingRodItems) {
            assertTrue(fishingRodItem.interactOn(waterTile, null, 0, 0, player, Direction.NONE));
        }
	}

	// Testing expected functionality on grass tiles
	@Test
	public void testFishingRodItemCannotFish() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		for (FishingRodItem fishingRodItem : fishingRodItems) {
			assertFalse(fishingRodItem.interactOn(grassTile, null, 0, 0, player, Direction.NONE));
		}
	}

	// Looking for expected behavior
	@Test
	public void testFishingRodCopy() {
		for (FishingRodItem rod : fishingRodItems) {
			Item item = rod.copy();
			assertTrue(item.equals(rod));
		}
	}

	// Partitioning again with same/different fishing rod
	@Test
	public void testFishingRodEqualsFalse() {
		Item item = Items.get("Water");
		for (FishingRodItem rod : fishingRodItems) {
			assertFalse(rod.equals(item));
		}
	}

	@Test
	public void testFishingRodEqualsTrue() {
		for (FishingRodItem rod : fishingRodItems) {
			FishingRodItem f = rod;
			assertTrue(rod.equals(f));
		}
	}

	//  Partitioning - valid input, invalid input (null input not accepted)
	@Test
	public void testFishingRodGetChancesWithValidInput() {
		assertDoesNotThrow(()-> FishingRodItem.getChance(0, 0));
	}

	// partial error guessing here - 4 fishing rods would mean index 4 is out of bounds
	@Test
	public void testFishingRodGetChancesWithInvalidInput() {
		assertThrows(IndexOutOfBoundsException.class, ()-> FishingRodItem.getChance(4, 0));
	}

	@Test
	public void testFishingRodBreaksAfterManyUses() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile waterTile = Tiles.get("water");
		for (int i = 0; i < 1000; i++) {
			fishingRodItems.get(0).interactOn(waterTile, null, 0, 0, player, Direction.NONE);
		}
		assertTrue(fishingRodItems.get(0).isDepleted());
	}

	@Test
	public void testFishingRodIsNotDepletedAfterOneUse() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile waterTile = Tiles.get("water");
		for (FishingRodItem fishingRodItem : fishingRodItems) {
			fishingRodItem.interactOn(waterTile, null, 0, 0, player, Direction.NONE);
			assertFalse(fishingRodItem.isDepleted());
		}
	}
}
