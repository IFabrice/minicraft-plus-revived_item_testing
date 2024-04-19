import minicraft.core.Game;
import minicraft.core.World;
import minicraft.entity.Direction;
import minicraft.entity.ItemEntity;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteAnimation;
import minicraft.gfx.SpriteLinker;
import minicraft.item.ArmorItem;
import minicraft.item.BookItem;
import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static minicraft.entity.particle.SandParticle.sprite;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ArmorItemBlackBoxTest {
	// check if:
	//armor actually reduces instance of damage
	//armor breaks after sustaining a set amount of damage
	private static ArrayList<ArmorItem> armorItems = new ArrayList<>();
	private static final ArrayList<Integer> indices = new ArrayList<>();

	// assume null and broken armor values will cause issues
	@BeforeEach
	public void setUp() {
		ArrayList<Item> items = Items.getAll();
		for (Item item : items) {
			if (item instanceof ArmorItem) {
				armorItems.add((ArmorItem) item);
			}
		}
	}

	@Test
	public void testArmorReducesDamageInstance() {
		/* ArmorItem armor = armorItems.get();
		//armo */
	}

	//
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

	@Test
	public void testArmorCannotBeEquippedIfPlayerHasArmorOn() {
		Player mockPlayer = mock(Player.class);
		int health = mockPlayer.health;
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
