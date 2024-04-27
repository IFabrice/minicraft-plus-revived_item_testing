import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.StackableItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InventoryBlackBoxTest {

	private final Inventory inventory = new Inventory();
	private final ArrayList<Item> items = Items.getAll();

	private final ArrayList<StackableItem> sItems = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		for (Item item : items) {
			if (item instanceof StackableItem) {
				sItems.add((StackableItem) item);
			}
		}
	}
	// Inventory is stated to have 27 slots available
	@Test
	public void testInventoryGetMaxSlots() {
        assertEquals(27, inventory.getMaxSlots());
	}

	// Inventory when initialized should not have any items in it.
	@Test
	public void testInventoryStartsOutEmpty() {
		assertTrue(inventory.getItems().isEmpty());
        assertEquals(0, inventory.invSize());
	}

	// Error guessing: Performing remove on an empty structure could cause an error
	@Test
	public void testInventoryClearingOnEmptyInventory() {
		assertDoesNotThrow(inventory::clearInv);
	}

	// Partitioning with invalid input
	@Test
	public void testInventoryGetWithInvalidIndex() {
		assertThrows(IndexOutOfBoundsException.class, ()-> inventory.get(-1));
	}


	// Partitioning by testing valid input
	@Test
	public void testAddingToInventorySuccessfullyAdds() {
		Random rand = new Random();
		int randomItem = rand.nextInt(items.size() + 1);
		assertEquals(1, inventory.add(items.get(randomItem)));
		assertEquals(1, inventory.invSize());
	}

	// Partitioning by testing null input
	@Test
	public void testAddingNullToInventory() {
		assertEquals(0, inventory.add(null));
	}

	// error guessing - bound errors may happen here
	@Test
	public void testAddingItemsInSameIndex() {
		inventory.add(0, items.get(1));
		assertEquals(1, inventory.add(0, items.get(2)));
		assertEquals(items.get(1), inventory.get(1));
	}

	// Testing duplicate allowance in inventory
	@Test
	public void testAddingDuplicateItems() {
		inventory.add(items.get(6));
		assertEquals(1, inventory.add(items.get(6)));
	}

	// Testing bizarre case where 0 of an item could be added - should reject call
	@Test
	public void testAddingNoItemsToInventory() {
		assertEquals(0, inventory.add(sItems.get(2), 0));
	}

	// Testing adding an invalid number of items to inventory (i.e. negative)
	@Test
	public void testAddingInvalidNumberOfItemsToInventory() {
		assertEquals(0, inventory.add(sItems.get(5), -1));
	}

	// Testing when a slot is called that is outside of current inventory size
	@Test
	public void testAddingItemsInInvalidIndex() {
		inventory.add(0, items.get(13));
		assertThrows(IndexOutOfBoundsException.class, ()-> inventory.add(2, items.get(20)));
	}

	// BVA Testing for when capacity is reached
	@Test
	public void testAddingItemsPastCapacity() {
		for (int i = 0; i < 27; i++) {
			inventory.add(items.get(i + 1));
		}
		assertEquals(0, inventory.add(items.get(30)));
	}

	// BVA Testing for the last possible item before capacity is full
	@Test
	public void testAddingItemsToCapacity() {
		for (int i = 0; i < 26; i++) {
			inventory.add(items.get(i + 10));
		}
		assertEquals(1, inventory.add(items.get(73)));
	}

	// Tests expected behavior of the quantity stacking add
	@Test
	public void testAddingMultipleOfItemToInventory() {
		inventory.add(sItems.get(40), 4);
		assertEquals(4, inventory.count(sItems.get(40)));
	}

	// Tests that clear actually makes inventory empty
	@Test
	public void testClearInventoryClearsInventory() {
		inventory.add(items.get(41));
		inventory.clearInv();
		assertEquals(0, inventory.invSize());
	}

	// Tests that getItems gets every item
	@Test
	public void testGetItemsRetrievesAllItems() {
		inventory.add(items.get(21));
		inventory.add(items.get(7));
		assertFalse(inventory.getItems().isEmpty());
	}


	// BVA when inventory has no items
	@Test
	public void testRemoveWhenThereAreNoElements() {
		assertThrows(IndexOutOfBoundsException.class, ()-> inventory.remove(0));
	}

	// Tests expected behavior with remove
	@Test
	public void testRemoveWithValidElementToRemove() {
		inventory.add(items.get(30));
		inventory.remove(0);
		assertEquals(0, inventory.invSize());
	}

	// Also checks if inventory positions are modified by a remove
	@Test
	public void testRemoveWithInBetweenValue() {
		inventory.add(items.get(50));
		inventory.add(items.get(27));
		inventory.add(items.get(8));
		inventory.remove(1);
		assertEquals(2, inventory.invSize());
		assertEquals(items.get(50).getName(), inventory.get(0).getName());
		assertEquals(items.get(8).getName(), inventory.get(1).getName());
	}

	// BVA with invalid index, 1 less than zero
	@Test
	public void testRemoveWithInvalidIndex() {
		assertThrows(IndexOutOfBoundsException.class, ()-> inventory.remove(-1));
	}

	// Partitioning; set of items not in inventory
	@Test
	public void testRemoveItemWhenItemNotInInventory() {
		inventory.add(items.get(30));
		inventory.removeItem(items.get(20));
		assertEquals(1, inventory.invSize());
	}

	// Tests remove; documentation supposedly removes off stacks or individual items
	@Test
	public void testRemoveItemWithStackedItem() {
		inventory.add(items.get(11));
		inventory.removeItem(items.get(11));
		assertEquals(0, inventory.invSize());
		inventory.add(items.get(10), 15);
		inventory.removeItem(items.get(10));
		assertEquals(14, inventory.invSize());
	}

	// Partitioning with null input
	@Test
	public void testRemoveItemWithNull() {
		assertThrows(NullPointerException.class, ()-> inventory.removeItem(null));
	}


	// Testing expected behavior
	@Test
	public void testRemoveItemsOnIndividualItem() {
		inventory.add(items.get(19));
		inventory.removeItems(items.get(19), 1);
		assertEquals(0, inventory.invSize());
	}

	// BVA/error guessing case - zero objects to be removed, but function called anyways
	@Test
	public void testRemoveItemsWithCountOfZero() {
		inventory.add(items.get(19));
		inventory.removeItems(items.get(19), 0);
		assertEquals(1, inventory.invSize());
	}

	// BVA with invalid count (i.e. negative numbers)
	@Test
	public void testRemoveItemsWithInvalidCount() {
		inventory.add(items.get(19));
		inventory.removeItems(items.get(19), -1);
		assertEquals(1, inventory.invSize());
	}

	// Partitioning with null items
	@Test
	public void testRemoveItemsWithNullItem() {
		inventory.add(items.get(19));
		inventory.removeItems(null, 5);
		assertEquals(1, inventory.invSize());
	}

	// Partitioning with items not in set of inventory
	@Test
	public void testRemoveItemsWithItemsNotInInventory() {
		inventory.add(items.get(19));
		inventory.removeItems(items.get(20), 15);
		assertEquals(1, inventory.invSize());
	}

	// BVA with one over the count provided in the inventory
	@Test
	public void testRemoveItemsWithCountExceedingInventoryQuantity() {
		inventory.add(sItems.get(19), 20);
		assertDoesNotThrow(()-> inventory.removeItems(sItems.get(19), 21));
		assertEquals(0, inventory.invSize());
	}

	// BVA - testing call when inventory is empty
	@Test
	public void testUpdateInventoryWithEmptyInventory() {
		inventory.updateInv(items.get(5).getName());
		assertEquals(1, inventory.invSize());
	}

	// Testing expected behavior
	@Test
	public void testUpdateInventoryWithValidInventory() {
		inventory.add(items.get(20));
		inventory.add(items.get(13));
		inventory.updateInv(items.get(23).getName());
		assertEquals(1, inventory.invSize());
		assertEquals(items.get(23).getName(), inventory.get(0).getName());
	}

	// Partitioning with null item
	@Test
	public void testUpdateInventoryWithNullInput() {
		assertThrows(NullPointerException.class, ()-> inventory.updateInv(null));
	}

	// Partitioning with invalid input
	@Test
	public void testUpdateInventoryWithNonexistentItem() {
		inventory.add(items.get(13));
		inventory.updateInv("toothbrush");
		assertEquals("TOOTHBRUSH", inventory.get(0).getName());
	}

	// Partitioning input - chance at zero
 	@Test
 	public void testTryAddWithZeroPercentChance() {
		Random rand = new Random();
		assertThrows(IllegalArgumentException.class, ()-> inventory.tryAdd(rand, 0, sItems.get(5), 4));
    }

	// partitioning input - negative chance
	@Test
	public void testTryAddWithNegativeChance() {
		Random rand = new Random();
		assertThrows(IllegalArgumentException.class, ()-> inventory.tryAdd(rand, -4, sItems.get(5), 4));
	}

	// Partitioning with null input
	@Test
	public void testTryAddWithNullItem() {
		Random rand = new Random();
		inventory.tryAdd(rand, 1, null, 4);
		assertEquals(0, inventory.invSize());
	}

	// Testing expected behavior (same as tryAdd without allOrNothingField)
	// For these tests, I needed to combine testing true and false because they had undefined behavior otherwise.
	@Test()
	public void testTryAddWithAllOrNothingTrueAndFalse() {
		Random rand = new Random();
		inventory.tryAdd(rand, 5, sItems.get(10), 8, true);
		assertTrue(inventory.invSize() == 8 || inventory.invSize() == 0);
		inventory.clearInv();
		inventory.tryAdd(rand, 5, sItems.get(5), 4, false);
		assertTrue(inventory.invSize() <= 4 && inventory.invSize() >= 0);
	}

	// Partitioned with invalid input (negative number)
	@Test
	public void testTryAddWithNegativeNum() {
		Random rand = new Random();
		inventory.tryAdd(rand, 5, sItems.get(5), -3);
		assertEquals(0, inventory.invSize());
	}
}
