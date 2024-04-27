import minicraft.core.io.InputHandler;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.BucketItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BucketItemBlackBoxTest {
	private static final ArrayList<BucketItem> bucketItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
        ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof BucketItem) {
				bucketItems.add((BucketItem) item);
			}
		}
	}

	// Confirming that all three bucket states are collected
	@Test
	public void testBucketItemHasAllThreeStates() {
		assert(bucketItems.size() == 3);
		assert(bucketItems.stream().distinct().count() == 3);

	}

	// Error guessing on bucket properties
	@Test
	public void testBucketItemHasExpectedProperties() {
		for (BucketItem bucket : bucketItems) {
			assertFalse(bucket.canAttack());
			assertTrue(bucket.interactsWithWorld());
		}
	}

	// potential fault?? Contained field not set when using getAll for buckets
	@Test
	public void testBucketSuccessfullyFilled() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile waterTile = Tiles.get("water");
		assertTrue(bucketItems.get(0).interactOn(waterTile, null, 0, 0, player, Direction.NONE));
	}

	@Test
	public void testBucketCannotBeFilled() {
		Player player = new Player(null, new InputHandler());
		Tiles.initTileList();
		Tile grassTile = Tiles.get("grass");
		assertFalse(bucketItems.get(0).interactOn(grassTile, null, 0, 0, player, Direction.NONE));
	}

	// Confirming copy function works as intended
	@Test
	public void testBucketCopyIsSuccessful() {
		for (BucketItem bucket : bucketItems) {
			Item item = bucket.copy();
            assertTrue(item.equals(bucket));
		}
	}

	// Partitioning equals method with same/different objects
	@Test
	public void testBucketEqualsTrue() {
		for (BucketItem bucket : bucketItems) {
			BucketItem b = bucket;
			assertTrue(bucket.equals(b));
		}
	}
	@Test
	public void testBucketEqualsFalse() {
		Item item = Items.get("grass");
		for (BucketItem bucket : bucketItems) {
			assertFalse(bucket.equals(item));
		}
	}
}
