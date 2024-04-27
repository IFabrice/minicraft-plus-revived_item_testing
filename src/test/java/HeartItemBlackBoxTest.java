import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.HeartItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HeartItemBlackBoxTest {

	private static final ArrayList<HeartItem> heartItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof HeartItem) {
				heartItems.add((HeartItem) item);
			}
		}
	}

	@Test
	public void testHeartHasExpectedQuantity() {
        assertEquals(1, heartItems.size());
	}

	// Test that hearts increase max health but caps at 30
	@Test
	public void testHeartIncreasesMaxPlayerHealth() {
		Player player = new Player(null, new InputHandler());
		heartItems.get(0).interactOn(null, null, 0, 0, player, Direction.NONE);
		assertTrue(player.health > 10);
        assertEquals( 15, player.health);
		for (int i = 0; i < 3; i++) {
			heartItems.get(0).interactOn(null, null, 0, 0, player, Direction.NONE);
		}
		assertFalse(heartItems.get(0).interactOn(null, null, 0, 0, player, Direction.NONE));
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
