import minicraft.core.io.InputHandler;
import minicraft.entity.mob.Player;
import minicraft.item.BookItem;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		Player player = new Player(null, new InputHandler());
		assertTrue(bookItems.get(0).interactOn(null, null, 0, 0, player, null));
	}

	// Error guessing
	@Test
	public void testBookCopy() {
		for (BookItem book : bookItems) {
			Item item = book.copy();
			assertTrue(item.equals(book));
		}
	}

	// Partitioning with equals - same/different object
	@Test
	public void testBookEqualsFalse() {
		Item item = Items.get("Water");
		for (BookItem book : bookItems) {
			assertFalse(book.equals(item));
		}
	}

	@Test
	public void testBookEqualsTrue() {
		for (BookItem book : bookItems) {
			BookItem b = book;
			assertTrue(book.equals(b));
		}
	}
}
