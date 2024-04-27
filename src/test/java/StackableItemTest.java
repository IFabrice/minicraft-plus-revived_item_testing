import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.item.HeartItem;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.StackableItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class StackableItemTest {
	private static final ArrayList<StackableItem> stackableItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof StackableItem) {
				stackableItems.add((StackableItem) item);
			}
		}
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(110,stackableItems.size());
	}

	@Test
	public void testStacksWithNoneStacking() {
		StackableItem item = stackableItems.get(0);
		assertFalse(item.stacksWith(mock(Item.class)));
	}

	@Test
	public void testStacksWithDifferentStacking() {
		StackableItem item = stackableItems.get(0);
		StackableItem item2 = stackableItems.get(1);
		assertFalse(item.stacksWith(item2));
	}

	@Test
	public void testStacksWithSameItem() {
		StackableItem item = stackableItems.get(0);
		StackableItem item2 = mock(StackableItem.class);
		when(item2.getName()).thenReturn(item.getName());
		assertTrue(item.stacksWith(item2));
	}

	@Test
	public void testIsDepletedNewItem() {
		StackableItem item = stackableItems.get(0);
		assertFalse(item.isDepleted());
	}

	@Test
	public void testIsDepletedModifiedItem() {
		StackableItem item = stackableItems.get(0);
		int oldCount = item.count;
		item.count = 0;
		assertTrue(item.isDepleted());
		item.count  = oldCount;
	}

	@Test
	public void testToString() {
		StackableItem item = stackableItems.get(0);
		String expected = item.getName() + "-Item-Stack_Size:" + item.count;
		assertEquals(expected,item.toString());
	}

	@Test
	public void testGetData() {
		StackableItem item = stackableItems.get(0);
		String expected = item.getName() + "_" + item.count;
		assertEquals(expected,item.getData());
	}

	@Test
	public void testGetDisplayName() {
		StackableItem item = stackableItems.get(0);
		String localizedString = "string";
		String expected = " " + item.count + " " + localizedString;
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			assertEquals(expected,item.getDisplayName());
		}
	}

	@Test
	public void testGetDisplayNameLargeCount() {
		StackableItem item = stackableItems.get(0);
		String localizedString = "string";
		int oldCount = item.count;
		item.count = 10000;
		String expected = " " + 999 + " " + localizedString;
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			assertEquals(expected,item.getDisplayName());
		}
		item.count = oldCount;
	}


}
