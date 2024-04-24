import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.UnknownItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnknownItemTest {

	@Test
	public void testCopy() {
		Item unknownItem = Items.get("copy");
        assertSame(unknownItem.getClass(), UnknownItem.class);
		UnknownItem copy = (UnknownItem) unknownItem.copy();
		assertEquals(unknownItem.getName(),copy.getName());
	}
}
