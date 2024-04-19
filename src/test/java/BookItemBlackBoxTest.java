import minicraft.entity.mob.Player;
import minicraft.item.BookItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

public class BookItemBlackBoxTest {
	private static final ArrayList<BookItem> bookItems = new ArrayList<>();


	@BeforeEach
	public void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof BookItem) {
				bookItems.add((BookItem) item);
			}
		}
	}

	// Error guessing on book properties
	@Test
	public void testBookHasExpectedProperties() {
		for (BookItem book : bookItems) {
			assertFalse(book.canAttack());
			assertFalse(book.interactsWithWorld());
		}
	}

	// Tests interaction between player and books; should be able to read books
	@Test
	public void testBookCanBeInteractedWithByPlayer() {
		Player mockPlayer = mock(Player.class);
		bookItems.get(0).interactOn(null, null, 0, 0, mockPlayer, null);
	}
}
