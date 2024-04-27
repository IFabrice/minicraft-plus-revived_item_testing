import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.furniture.Furniture;
import minicraft.entity.mob.Player;
import minicraft.item.FurnitureItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FurnitureItemBlackBoxTest {
	private static final ArrayList<FurnitureItem> furnitureItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof FurnitureItem) {
				furnitureItems.add((FurnitureItem) item);
			}
		}
	}

	// Testing for expected properties; lantern is used within world
	@Test
	public void testFurnitureItemHasExpectedProperties() {
		for (FurnitureItem furniture : furnitureItems) {
			assertFalse(furniture.canAttack());
			assertTrue(furniture.interactsWithWorld());
		}
	}

	// Tests expected behavior; furniture can be placed where it is expected to be placed
	@Test
 	public void testFurnitureItemIsPlaced() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		Level level = new Level(5, 8, 3, null);
		assertTrue(furnitureItems.get(0).interactOn(grassTile, level, 0, 0, player, Direction.NONE));
		assertTrue(furnitureItems.get(0).placed);
	}

	// Tests case where furniture is expected not to be able to be placed
	@Test
	public void testFurnitureItemPlacementOnBadTile() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile waterTile = Tiles.get("water");
		Level level = new Level(5, 8, 3, null);
		assertFalse(furnitureItems.get(0).interactOn(waterTile, level, 0, 0, player, Direction.NONE));
		assertFalse(furnitureItems.get(0).placed);
	}

	// This is one of the very few classes where the constructor is public; so we partition with valid furniture and
	// null furniture.
	@Test
	public void testFurnitureConstructor() {
		Furniture futon = new Furniture("Couch", null, null);
		FurnitureItem f = new FurnitureItem(futon);
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		Level level = new Level(5, 8, 3, null);
		assertFalse(f.canAttack());
		assertTrue(f.interactsWithWorld());
		assertFalse(f.placed);
		assertFalse(f.isDepleted());
		assertTrue(f.interactOn(grassTile, level, 0, 0, player, Direction.NONE));
		assertTrue(f.isDepleted());
		assertTrue(f.placed);
	}

	// Now that we know that furniture constructor works, check if it has expected behavior of other furniture
	@Test
	public void testNewFurnitureWithNullConstructor() {
		Furniture futon = new Furniture(null, null, null);
		FurnitureItem f = new FurnitureItem(futon);
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		Level level = new Level(5, 8, 3, null);
		assertFalse(f.interactOn(grassTile, level, 0, 0, player, Direction.NONE));
	}

	@Test
	public void testFurnitureCopyHasExpectedBehavior() {
		for (FurnitureItem furniture : furnitureItems) {
			Item item = furniture.copy();
			assertTrue(item.equals(furniture));
		}
	}

	// Partitioning for same/different objects for equals
	@Test
	public void testFurnitureEqualsTrue() {
		for (FurnitureItem furniture : furnitureItems) {
			FurnitureItem f = furniture.copy();
			assertTrue(furniture.equals(f));
		}
	}

	@Test
	public void testFurnitureEqualsFalse() {
		Item item = Items.get("grass");
		for (FurnitureItem furniture : furnitureItems) {
			assertFalse(furniture.equals(item));
		}
	}
}
