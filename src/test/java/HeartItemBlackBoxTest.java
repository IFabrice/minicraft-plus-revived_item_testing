import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.HeartItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeartItemBlackBoxTest {

	private final ArrayList<HeartItem> heartItems = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof HeartItem) {
				heartItems.add((HeartItem) item);
			}
		}
	}

	// There exists 4 hearts that increase player health by 5 each
	@Test
	public void testHeartHasExpectedQuantity() {
        assertEquals(1, heartItems.size());
	}

	@Test
	public void testHeartIncreasesMaxPlayerHealth() {
		Player p1 = new Player(null, new InputHandler());
		int health = p1.health;
		assertTrue(heartItems.get(0).interactOn(null, null, 0, 0, p1, Direction.NONE));
		assertTrue(p1.health > health);
        assertEquals(health + 5, p1.health);
	}

	// Documentation states that player health cannot be increased beyond 30
	@Test
	public void testHeartHealthIncreaseHasLimit() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		for (int i = 0; i < 50; i++) {
			heartItems.get(0).interactOn(grassTile, null, 0, 0, player, Direction.NONE);
		}
		assertFalse(heartItems.get(0).interactOn(grassTile, null, 0, 0, player, Direction.NONE));
        assertEquals(30, player.health);
	}

	@Test
	public void testHeartCopyHasExpectedBehavior() {
		for (HeartItem heart : heartItems) {
			Item item = heart.copy();
			assertTrue(item.equals(heart));
		}
	}

	@Test
	public void testHeartEqualsFalse() {
		Item item = Items.get("grass");
		for (HeartItem heart : heartItems) {
			assertFalse(heart.equals(item));
		}
	}

	// Partitioning between same/different object for equals method
	@Test
	public void testHeartEqualsTrue() {
		for (HeartItem heart : heartItems) {
			HeartItem h = heart.copy();
			assertTrue(heart.equals(h));
		}
	}

	@Test
	public void testHeartHasExpectedBehavior() {
	assertFalse(heartItems.get(0).interactsWithWorld());
	assertFalse(heartItems.get(0).canAttack());
	}
}
