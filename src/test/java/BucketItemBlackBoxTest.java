import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteLinker;
import minicraft.item.BookItem;
import minicraft.item.BucketItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.LavaTile;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BucketItemBlackBoxTest {
	private static final ArrayList<BucketItem> bucketItems = new ArrayList<>();

	// assume null and broken armor values will cause issues
	@BeforeEach
	public void setUp() {
        ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof BucketItem) {
				bucketItems.add((BucketItem) item);
			}
		}
	}

	// empty bucket should have different behavior than lava/water buckets
	@Test
	public void testBucketItemHasAllThreeStates() {
		assert(bucketItems.size() == 3);
		assert(bucketItems.stream().distinct().count() == 3);

	}

	@Test
	public void testBucketItemHasExpectedProperties() {
		for (BucketItem bucket : bucketItems) {
			assertFalse(bucket.canAttack());
			assertTrue(bucket.interactsWithWorld());
		}
	}

	// potential fault?? Contained field not set when using getAll for buckets
	@Test
	public void testBucketChangesTileWhenNotEmpty() {

		Tile mockTile = mock(Tile.class);
		mockTile.id = 1;
		Player mockPlayer = mock(Player.class);
		Level mockLevel = mock(Level.class);
		assertTrue(bucketItems.get(2).interactOn(mockTile, mockLevel, 0, 0, mockPlayer, Direction.NONE));

	}
}
