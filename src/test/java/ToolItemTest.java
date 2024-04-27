import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class ToolItemTest {

	private static final ArrayList<ToolItem> toolItems = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof ToolItem) {
				toolItems.add((ToolItem) item);
			}
		}
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(36,toolItems.size());
	}

	@Test
	public void testTrueNoLevel() {
		ToolItem item = toolItems.get(0);
		ToolType initialType = item.type;
        item.type = ToolType.Shears;
		String localizedString = "Localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			assertEquals(" " + localizedString,item.getDisplayName());
		}
		item.type = initialType;
	}

	@Test
	public void testTrueLevel() {
		ToolItem item = toolItems.get(0);
		ToolType initialType = item.type;
        item.type = ToolType.Axe;
		String localizedString = "Localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(anyString()))
				.thenReturn(localizedString);
			assertEquals(" " + localizedString + " " + localizedString,item.getDisplayName());
		}
		item.type = initialType;
	}

	@Test
	public void testIsDepletedFalse() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		assertFalse(item.isDepleted());
		item.dur = initalDur;
	}

	@Test
	public void testIsDepletedTrue() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 0;
		assertTrue(item.isDepleted());
		item.dur = initalDur;
	}

	@Test
	public void testPayDurability0Dur() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 0;
		assertFalse(item.payDurability());
		item.dur = initalDur;
	}

	@Test
	public void testPayDurabilityNone0DurCreative() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 1;
		try (MockedStatic<Game> utilities = mockStatic(Game.class)) {
			utilities.when(() -> Game.isMode("minicraft.settings.mode.creative"))
				.thenReturn(true);
			assertTrue(item.payDurability());
			assertEquals(1,item.dur);
		}
		item.dur = initalDur;
	}

	@Test
	public void testPayDurabilityNone0DurNoneCreative() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 1;
		try (MockedStatic<Game> utilities = mockStatic(Game.class)) {
			utilities.when(() -> Game.isMode("minicraft.settings.mode.creative"))
				.thenReturn(false);
			assertTrue(item.payDurability());
			assertEquals(0,item.dur);
		}
		item.dur = initalDur;
	}

	@Test
	public void testCanAttackFalse() {
		ToolItem item = toolItems.get(0);
		ToolType initalType = item.type;
		item.type = ToolType.Axe;
		assertTrue(item.canAttack());
		item.type = initalType;
	}

	@Test
	public void testCanAttackTrue() {
		ToolItem item = toolItems.get(0);
		ToolType initalType = item.type;
		item.type = ToolType.Shears;
		assertFalse(item.canAttack());
		item.type = initalType;
	}

	@Test
	public void testGetDamageChanges() {
		ToolItem item = toolItems.get(0);
		double averageDamage = 0;
		int initialDamage = item.getDamage();
		for (int i =0; i < 100; i++) {
			averageDamage += item.getDamage();
		}
		averageDamage /= 100.00;
		assertNotEquals(initialDamage,averageDamage);
	}

	@Test
	public void testGetAttackDamageBonusNoDur() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 0;
		assertEquals(0,item.getAttackDamageBonus(mock(Entity.class)));
		item.dur = initalDur;
	}

	@Test
	public void testGetAttackDamageBonusDurNoneMob() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		assertEquals(0,item.getAttackDamageBonus(mock(Entity.class)));
		item.dur = initalDur;
	}

	@Test
	public void testGetAttackDamageBonusDurMobAxe() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		ToolType initalType = item.type;
		item.type = ToolType.Axe;
		int damageBonus = item.getAttackDamageBonus(mock(Mob.class));
		assertTrue(damageBonus >= 2 && damageBonus <= 5);
		item.dur = initalDur;
		item.type = initalType;
	}

	@Test
	public void testGetAttackDamageBonusDurMobSword() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		ToolType initalType = item.type;
		item.type = ToolType.Sword;
		int damageBonus = item.getAttackDamageBonus(mock(Mob.class));
		assertTrue(damageBonus >= 3 && damageBonus <= 5);
		item.dur = initalDur;
		item.type = initalType;
	}

	@Test
	public void testGetAttackDamageBonusDurMobClaymore() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		ToolType initalType = item.type;
		item.type = ToolType.Claymore;
		int damageBonus = item.getAttackDamageBonus(mock(Mob.class));
		assertTrue(damageBonus >= 3 && damageBonus <= 6);
		item.dur = initalDur;
		item.type = initalType;
	}

	@Test
	public void testGetAttackDamageBonusDurMobPickaxe() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		ToolType initalType = item.type;
		item.type = ToolType.Pickaxe;
		int damageBonus = item.getAttackDamageBonus(mock(Mob.class));
		assertTrue(damageBonus >= 3 && damageBonus <= 6); // Should be between 3 and 6
		item.dur = initalDur;
		item.type = initalType;
	}

	@Test
	public void testGetAttackDamageBonusDurMobOther() {
		ToolItem item = toolItems.get(0);
		int initalDur = item.dur;
		item.dur = 5;
		ToolType initalType = item.type;
		item.type = ToolType.Shovel;
		int damageBonus = item.getAttackDamageBonus(mock(Mob.class));
		assertEquals(1,damageBonus);
		item.dur = initalDur;
		item.type = initalType;
	}

	@Test
	public void testGetData() {
		assertTrue(toolItems.get(0).getData().contains("_"));
	}

	@Test
	public void testEqualsNoneToolItem() {
		ToolItem item = toolItems.get(0);
		assertFalse(item.equals(mock(Item.class)));
	}

	@Test
	public void testEqualsDiffLevel() {
		ToolItem item = toolItems.get(0);
		ToolItem mock = mock(ToolItem.class);
		mock.level = -1;
		mock.type = item.type;
		assertFalse(item.equals(mock));
	}

	@Test
	public void testEqualsDiffType() {
		ToolItem item = toolItems.get(0);
		ToolItem mock = mock(ToolItem.class);
		mock.type = null;
		mock.level = item.level;
		assertFalse(item.equals(mock));
	}

	@Test
	public void testEqualsSameTypeAndLevel() {
		ToolItem item = toolItems.get(0);
		ToolItem mock = mock(ToolItem.class);
		mock.type = item.type;
		mock.level = item.level;
		assertTrue(item.equals(mock));
	}

	@Test
	public void testHashCodeDifferentItems() {
		assertNotEquals(toolItems.get(0).hashCode(),toolItems.get(1).hashCode());
	}

	@Test
	public void testHashCodeSameItems() {
		assertEquals(toolItems.get(0).hashCode(),toolItems.get(0).hashCode());
	}


}
