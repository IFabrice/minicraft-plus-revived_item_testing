import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.StackableItem;
import minicraft.item.ToolItem;
import minicraft.item.UnknownItem;
import minicraft.item.WateringCanItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ItemsTest {

	@Test
	public void testGetUnknownItem() {
        assertSame(Items.get("unknown").getClass(), UnknownItem.class);
	}

	@Test
	public void testGetInvalidUnderscore() {
		assertSame(Items.get("_NotInt",true).getClass(), UnknownItem.class);
	}

	@Test
	public void testGetInvalidColumn() {
		assertSame(Items.get(";NotInt",true).getClass(), UnknownItem.class);
	}

	@Test
	public void testGetNullAllowNull() {
		assertNull(Items.get("Null",true));
	}

	@Test
	public void testGetNullDontAllowNull() {
		Item returnValue = Items.get("Null",false);
		assertSame(returnValue.getClass(), UnknownItem.class);
		assertEquals(returnValue.getName(),"NULL");
	}

	@Test
	public void testGetUnknown() {
		Item returnValue = Items.get("UNKNOWN",false);
		assertSame(returnValue.getClass(), UnknownItem.class);
		assertEquals(returnValue.getName(),"BLANK");
	}

	@Test
	public void testGetToolItem() {
		String toolName = "Wood Shovel";
		assertSame(Items.get(toolName,true).getClass(), ToolItem.class);
	}

	@Test
	public void testGetToolItemWithUnderScoreInvalidDur() {
		String toolName = "Wood Shovel_invalid";
		Item returnValue = Items.get(toolName,true);
		assertSame(returnValue.getClass(), ToolItem.class);
		ToolItem returnTool = (ToolItem) returnValue;
		assertEquals(1,returnTool.dur);
	}

	@Test
	public void testGetToolItemWithUnderScoreValidDur() {
		String toolName = "Wood Shovel_5";
		Item returnValue = Items.get(toolName,true);
		assertSame(returnValue.getClass(), ToolItem.class);
		ToolItem returnTool = (ToolItem) returnValue;
		assertEquals(5,returnTool.dur);
	}

	@Test
	public void testGetWateringCanItem() {
		String toolName = "Watering Can";
		assertSame(Items.get(toolName,true).getClass(), WateringCanItem.class);
	}

	@Test
	public void testGetCountStackableItem() {
		StackableItem stackableItem = mock(StackableItem.class);
		stackableItem.count = 5;
		assertEquals(5,Items.getCount(stackableItem));
	}

	@Test
	public void testGetCountNullItem(){
		assertEquals(0,Items.getCount(null));
	}

	@Test
	public void testGetCountNonStackingItem(){
		assertEquals(1,Items.getCount(mock(Item.class)));
	}

	@Test
	public void testGetCreativeModeInventory() {
		assertSame(Items.CreativeModeInventory.class,Items.getCreativeModeInventory().getClass());
	}




}
