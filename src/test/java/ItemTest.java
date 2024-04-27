import minicraft.core.io.Localization;
import minicraft.entity.mob.Player;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteLinker;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemTest {

	private Item mockItem;

	@BeforeEach
	public void init() {
		mockItem = Items.get("copy");
	}

	@Test
	public void testRenderHud() {
		String localizedString = "localized string";
		Screen mockScreen = mock(Screen.class);
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			mockItem.renderHUD(mockScreen,0,0,5);
		}
		verify(mockScreen,times(1)).render(eq(0),eq(0),any(SpriteLinker.LinkedSprite.class));
	}

	@Test
	public void testInteractOn() {
		assertFalse(mockItem.interactOn(null,null,0,0,null,null));
	}

	@Test
	public void testIsDepleted() {
		assertFalse(mockItem.isDepleted());
	}

	@Test
	public void testCanAttack() {
		assertFalse(mockItem.canAttack());
	}

	@Test
	public void testNullEquals() {
		assertFalse(mockItem.equals(null));
	}

	@Test
	public void testDifferentClassEquals() {
		assertFalse(mockItem.equals(mock(Player.class)));
	}

	@Test
	public void testDifferentName() {
		Item item2 = Items.get("orig");
		assertFalse(mockItem.equals(item2));
	}

	@Test
	public void testSameEquals() {
		assertTrue(mockItem.equals(mockItem));
	}

	@Test
	public void testHashCodeReproducible() {
		assertEquals(mockItem.hashCode(),mockItem.hashCode());
	}

	@Test
	public void testGetDescription() {
		assertEquals("COPY",mockItem.getDescription());
	}

	@Test
	public void testGetDisplayName() {
		String localizedString = "localized string";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			assertEquals(" 1 " + localizedString,mockItem.getDisplayName());
		}
	}

	@Test
	public void testInteractsWithWorld() {
		assertTrue(mockItem.interactsWithWorld());
	}


}
