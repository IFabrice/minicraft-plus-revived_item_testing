import minicraft.item.FishingRodItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

	// commented out due to issue with tiles not being water tiles, affecting output
/* 	@Test
	public void testFishingRodItemCanFish() {
		Player mockPlayer = mock(Player.class);
		Tile mockTile = mock(Tile.class);
		when(mockTile.connectsToFluid(any(Level.class), anyInt(), anyInt())).thenReturn(true);
		mockTile.connectsToFluid(null, 0, 0);
        for (FishingRodItem fishingRodItem : fishingRodItems) {
            assertTrue(fishingRodItem.interactOn(mockTile, null, 0, 0, mockPlayer, Direction.NONE));
        }
	} */
}
