import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.SummonItem;
import minicraft.level.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class SummonItemTest {

	private static final ArrayList<SummonItem> summonItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof SummonItem) {
				summonItems.add((SummonItem) item);
			}
		}
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(2,summonItems.size());
	}

	@Test
	public void testInteractOnLevel0AirWizardMob() {
		SummonItem airWizardMob = summonItems.get(0);
		String localized = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized("minicraft.notification.wrong_level_sky"))
				.thenReturn(localized);
			assertFalse(airWizardMob.interactOn(null,mock(Level.class),0,0,null,null));
			assertTrue(Game.notifications.contains(localized));
		}

	}

	@Test
	public void testInteractOnLevel0ObsidianNightMob() {
		SummonItem obsidianNightMob = summonItems.get(1);
		String localized = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized("minicraft.notification.wrong_level_dungeon"))
				.thenReturn(localized);
			assertFalse(obsidianNightMob.interactOn(null,mock(Level.class),0,0,null,null));
			assertTrue(Game.notifications.contains(localized));
		}
	}

	@Test
	public void testInteractsWithWorld() {
		assertFalse(summonItems.get(0).interactsWithWorld());
		assertFalse(summonItems.get(1).interactsWithWorld());
	}
}
