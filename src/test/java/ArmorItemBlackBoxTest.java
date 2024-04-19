import minicraft.core.Game;
import minicraft.core.World;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.ArmorItem;
import minicraft.item.BookItem;
import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArmorItemBlackBoxTest {

	private static final ArrayList<ArmorItem> armorItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof ArmorItem) {
				armorItems.add((ArmorItem) item);
			}
		}
	}

	/*
	@Test
	public void testArmorReducesDamageInstance() {

	}

	@Test
	public void testArmorDurabilityDecreasesFromDamage() {

	}
	// does not work
	@Test
	public void testArmorIsLostWhenPlayerDies() {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		armorItems.get(0).interactOn(null, null, 0, 0, mockPlayer, null);
		doCallRealMethod().when(mockPlayer).pickupItem(any(ItemEntity.class));
		ItemEntity item = mock(ItemEntity.class);
        item.item = mock(Item.class);
		Inventory inventory = mock(Inventory.class);
		when(mockPlayer.getInventory()).thenReturn(inventory);
		Level level = mock(Level.class);
		Game game = mock(Game.class);
		doCallRealMethod().when(mockPlayer).die();
		mockPlayer.die();
		assertNull(mockPlayer.curArmor);
	}

	// does not work
	@Test
	public void testArmorDisappearsWhenDurabilityIsZero() {
		Player mockPlayer = mock(Player.class);
		int health = mockPlayer.health;
		when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		armorItems.get(0).interactOn(null, null, 0, 0, mockPlayer, null);
		int durability = mockPlayer.armor;
		doCallRealMethod().when(mockPlayer).hurt(anyInt(), eq(Direction.NONE));
		mockPlayer.hurt(1000, Direction.NONE);
		assertNull(mockPlayer.curArmor);
	}

	// does not work since I cannot access parameters
	@Test
	public void testArmorHasTenPointsOfItsMaterial() {
		Player mockPlayer = mock(Player.class);
		when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		armorItems.get(3).interactOn(null, null, 0, 0, mockPlayer, null);
		final int durability = mockPlayer.armor;
		doCallRealMethod().when(mockPlayer).hurt(any(Tile.class), anyInt(), anyInt(), anyInt());
		mockPlayer.hurt(null, 0, 0, 100);
		assert(mockPlayer.armor < durability);
	}
    */

	// Error guessing on armor properties
	@Test
	public void testArmorHasExpectedProperties() {
		for (ArmorItem armor : armorItems) {
			assertFalse(armor.canAttack());
			assertFalse(armor.interactsWithWorld());
		}
	}

	// Testing that all available armor is retrieved
	// Fails when run all at once, passes when run by itself?
	@Test
	public void testArmorHasAllArmors() {
		assert(armorItems.size() == 5);
	}

	@Test
	public void testArmorCannotBeEquippedIfPlayerHasArmorOn() {
		Player mockPlayer = mock(Player.class);
        when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		armorItems.get(0).interactOn(null, null, 0, 0, mockPlayer, null);
		armorItems.get(1).interactOn(null, null, 0, 0, mockPlayer, null);
		// ensure first armor equipped is not overwritten
        assertNotSame(mockPlayer.curArmor, armorItems.get(1));
		assertSame(mockPlayer.curArmor, armorItems.get(0));
	}

	@Test
	public void testArmorDoesNotAffectPlayerBaseHealth() {
		Player mockPlayer = mock(Player.class);
		int health = mockPlayer.health;
		when(mockPlayer.payStamina(anyInt())).thenReturn(true);
		armorItems.get(0).interactOn(null, null, 0, 0, mockPlayer, null);
		assertEquals(health, mockPlayer.health);
	}

}
