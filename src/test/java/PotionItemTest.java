import minicraft.core.io.Settings;
import minicraft.entity.mob.Player;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.PotionItem;
import minicraft.item.PotionType;
import minicraft.screen.AchievementsDisplay;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PotionItemTest {
	private static ArrayList<PotionItem> potionItems = new ArrayList<>();
	private static PotionItem mockPotionItem;
	private static PotionItem lavaPotionItem;

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof PotionItem) {
				potionItems.add((PotionItem) item);
			}
		}
		mockPotionItem = potionItems.get(0);
		lavaPotionItem = potionItems.get(8);
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(12,potionItems.size());
	}

	@Test
	public void testInteractOnLava() {
		ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Item> itemValueCapture = ArgumentCaptor.forClass(Item.class);

		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).tryAddToInvOrDrop(itemValueCapture.capture());
		mockPlayer.potioneffects = new HashMap<>();
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		try (MockedStatic<AchievementsDisplay> utilities = mockStatic(AchievementsDisplay.class)) {
			utilities.when(() -> AchievementsDisplay.setAchievement(valueCapture.capture(),anyBoolean()))
				.thenReturn(true);
			assertTrue(lavaPotionItem.interactOn(null, null, 0, 0, mockPlayer, null));
			assertEquals(1,mockPlayer.potioneffects.size());
			assertEquals("minicraft.achievement.lava",valueCapture.getValue());
		}
		assertEquals("Glass Bottle",itemValueCapture.getValue().getName());
	}

	@Test
	public void testInteractOnLavaCreative() {
		ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);

		Player mockPlayer = mock(Player.class);
		String oldMode = (String) Settings.get("mode");
		Settings.set("mode","minicraft.settings.mode.creative");

		mockPlayer.potioneffects = new HashMap<>();
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		try (MockedStatic<AchievementsDisplay> utilities = mockStatic(AchievementsDisplay.class)) {
			utilities.when(() -> AchievementsDisplay.setAchievement(valueCapture.capture(),anyBoolean()))
				.thenReturn(true);
			assertTrue(lavaPotionItem.interactOn(null, null, 0, 0, mockPlayer, null));
			assertEquals(1,mockPlayer.potioneffects.size());
			assertEquals("minicraft.achievement.lava",valueCapture.getValue());
		}


		Settings.set("mode",oldMode);
	}

	@Test
	public void testInteractOnAwkward() {
		Player mockPlayer = mock(Player.class);
		mockPlayer.potioneffects = new HashMap<>();
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		assertFalse(mockPotionItem.interactOn(null, null, 0, 0, mockPlayer, null));
		assertEquals(0,mockPlayer.potioneffects.size()); // duration is 0
	}

	@Test
	public void testApplyPositionOnLavaNoneZeroTime() {
		ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Item> itemValueCapture = ArgumentCaptor.forClass(Item.class);

		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).tryAddToInvOrDrop(itemValueCapture.capture());
		HashMap<PotionType, Integer> potioneffects = new HashMap<>();
		mockPlayer.potioneffects = potioneffects;
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		doAnswer(invocation -> {
			PotionType type = invocation.getArgument(0);
			int duration = invocation.getArgument(1);
			potioneffects.put(type, duration);
			return null;
		}).when(mockPlayer).addPotionEffect(any(PotionType.class), anyInt());
		try (MockedStatic<AchievementsDisplay> utilities = mockStatic(AchievementsDisplay.class)) {
			utilities.when(() -> AchievementsDisplay.setAchievement(valueCapture.capture(),anyBoolean()))
				.thenReturn(true);
			assertTrue(PotionItem.applyPotion(mockPlayer,lavaPotionItem.type,5));
			assertEquals(1,mockPlayer.potioneffects.size());
		}
	}

	@Test
	public void testApplyPositionOnLavaZeroTime(){
		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).tryAddToInvOrDrop(any());
		HashMap<PotionType, Integer> potioneffects = new HashMap<>();
		potioneffects.put(lavaPotionItem.type,5);
		mockPlayer.potioneffects = potioneffects;
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		try (MockedStatic<AchievementsDisplay> utilities = mockStatic(AchievementsDisplay.class)) {
			utilities.when(() -> AchievementsDisplay.setAchievement(anyString(), anyBoolean()))
				.thenReturn(true);
			assertTrue(PotionItem.applyPotion(mockPlayer, lavaPotionItem.type, 0));
			assertEquals(0, mockPlayer.potioneffects.size());
			verify(mockPlayer, times(0)).addPotionEffect(any());
		}
	}

	@Test
	public void testApplyPositionOnPositiveAddEffect() {
		ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Item> itemValueCapture = ArgumentCaptor.forClass(Item.class);

		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).tryAddToInvOrDrop(itemValueCapture.capture());
		HashMap<PotionType, Integer> potioneffects = new HashMap<>();
		mockPlayer.potioneffects = potioneffects;
		when(mockPlayer.getPotionEffects()).thenReturn(new HashMap<>());
		doAnswer(invocation -> {
			PotionType type = invocation.getArgument(0);
			int duration = invocation.getArgument(1);
			potioneffects.put(type, duration);
			return null;
		}).when(mockPlayer).addPotionEffect(any(PotionType.class), anyInt());
		try (MockedStatic<AchievementsDisplay> utilities = mockStatic(AchievementsDisplay.class)) {
			utilities.when(() -> AchievementsDisplay.setAchievement(valueCapture.capture(),anyBoolean()))
				.thenReturn(true);
			assertTrue(PotionItem.applyPotion(mockPlayer,lavaPotionItem.type,true));
			assertEquals(1,mockPlayer.potioneffects.size());
		}
	}

	@Test
	public void testTrueEquals(){
		PotionItem copy = lavaPotionItem.copy();
		assertTrue(lavaPotionItem.equals(copy));
	}

	@Test
	public void testDifferentObjectEquals() {
		Item mock = mock(Item.class);
		assertFalse(lavaPotionItem.equals(mock));
	}

	@Test
	public void testFalseEquals(){
		assertFalse(lavaPotionItem.equals(mockPotionItem));
	}


	@Test
	public void testHashCode() {
		assertNotEquals(lavaPotionItem.hashCode(),mockPotionItem.hashCode());
	}

	@Test
	public void testCopyHashCode() {
		PotionItem copy = lavaPotionItem.copy();
		assertEquals(lavaPotionItem.hashCode(),copy.hashCode());
	}

	@Test
	public void testInteractsWithWorld() {
		assertFalse(lavaPotionItem.interactsWithWorld());
	}

	@Test
	public void testCopy() {
		PotionItem copy = lavaPotionItem.copy();
		assertEquals(lavaPotionItem.getName(),copy.getName());
		assertEquals(lavaPotionItem.type,copy.type);
		assertEquals(lavaPotionItem.count,copy.count);
	}

	@Test
	public void testApplyPotionAddEffect() {
		Player mockPlayer = mock(Player.class);
		PotionType mockPotionType = mock(PotionType.class);
		when(mockPotionType.toggleEffect(mockPlayer,true)).thenReturn(true);
		HashMap<PotionType, Integer> potioneffects = new HashMap<>();
		mockPlayer.potioneffects = potioneffects;
		when(mockPlayer.getPotionEffects()).thenReturn(potioneffects);
		assertTrue(PotionItem.applyPotion(mockPlayer,mockPotionType,5));
		verify(mockPlayer,times(1)).addPotionEffect(mockPotionType,5);
	}

	@Test
	public void testApplyPotionNoAddEffect() {
		Player mockPlayer = mock(Player.class);
		PotionType mockPotionType = mock(PotionType.class);
		when(mockPotionType.toggleEffect(mockPlayer,true)).thenReturn(true);
		HashMap<PotionType, Integer> potioneffects = new HashMap<>();
		mockPlayer.potioneffects = potioneffects;
		when(mockPlayer.getPotionEffects()).thenReturn(potioneffects);
		assertTrue(PotionItem.applyPotion(mockPlayer,mockPotionType,0));
		verify(mockPlayer,times(0)).addPotionEffect(any(PotionType.class),anyInt());
	}


}
