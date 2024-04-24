import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.TileItem;
import minicraft.level.Level;
import minicraft.level.tile.GrassTile;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.debugging.Localized;

import java.util.ArrayList;

import static minicraft.level.tile.Tiles.initTileList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TileItemTest {

	private static final ArrayList<TileItem> tileItems = new ArrayList<>();
	private static TileItem wall;
	private static TileItem door;
	private static TileItem brick;
	private static TileItem plank;
	private static TileItem stone;
	private static TileItem ornate;

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof TileItem) {
				TileItem tileItem = (TileItem) item;
				tileItems.add(tileItem);
				TileItem.TileModel model = tileItem.model;
				if(model != null) {
					if (model.tile.contains("WALL")) {
						wall = tileItem;
					} else if (model.tile.contains("DOOR")) {
						door = tileItem;
					} else if (model.tile.contains("BRICK")) {
						brick = tileItem;
					} else if (model.tile.contains("PLANK")) {
						plank = tileItem;
					} else if (model.tile.equals("STONE")) {
						stone = tileItem;
					} else if (model.tile.contains("ORNATE")) {
						ornate = tileItem;
					}
				}
			}
		}
		initTileList();
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(43,tileItems.size());
	}

	@Test
	public void testNullGetTile() {
		Tile resultTile = TileItem.TileModel.getTile(null);
		assertEquals(GrassTile.class,resultTile.getClass());
		assertEquals("GRASS",resultTile.name);
	}

	@Test
	public void testNotNullGetTile() {
		TileItem.TileModel model0 = tileItems.get(0).model;
		Tile resultTile = TileItem.TileModel.getTile(model0);
		Tile modelTile = Tiles.get(model0.tile);
		assertEquals(resultTile,modelTile);
	}

	@Test
	public void tesNullGetTileData() {
		int result = TileItem.TileModel.getTileData(null,mock(Tile.class),mock(Tile.class),mock(Level.class),0,0,mock(Player.class), Direction.DOWN);
		assertEquals(0, result);
	}

	@Test
	public void tesGetTileData() {
		TileItem.TileModel model0 = tileItems.get(0).model;
		int result = TileItem.TileModel.getTileData(model0,mock(Tile.class),mock(Tile.class),mock(Level.class),0,0,mock(Player.class), Direction.DOWN);
		assertEquals(0, result);
	}

	@Test
	public void tesInteractOnMatch() {
		TileItem tileItem = tileItems.get(0);
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(true);
		int initialSize = Game.notifications.size();
		assertTrue(tileItem.interactOn(mockTile,mockLevel,0,0,null,null));
		assertEquals(initialSize,Game.notifications.size());
		verify(mockLevel,times(1)).setTile(eq(0),eq(0),any(Tile.class),anyInt());
	}

	@Test
	public void tesInteractOnNoMatch() {
		TileItem tileItem = tileItems.get(0);
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		assertFalse(tileItem.interactOn(mockTile,mockLevel,0,0,null,null));
		assertEquals(initialSize,Game.notifications.size());
	}

	@Test
	public void tesInteractOnNoMatchWall() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.invalid_placement"), any()))
				.thenReturn(localizedString);
			assertFalse(wall.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void tesInteractOnNoMatchDoor() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.invalid_placement"), any()))
				.thenReturn(localizedString);
			assertFalse(door.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void tesInteractOnNoMatchBrick() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.dig_hole"), any()))
				.thenReturn(localizedString);
			assertFalse(brick.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void tesInteractOnNoMatchPlank() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.dig_hole"), any()))
				.thenReturn(localizedString);
			assertFalse(plank.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void tesInteractOnNoMatchStone() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.dig_hole"), any()))
				.thenReturn(localizedString);
			assertFalse(stone.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void tesInteractOnNoMatchOrnate() {
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(5);
		Tile mockTile = mock(Tile.class);
		when(mockTile.matches(anyInt(),anyString())).thenReturn(false);
		int initialSize = Game.notifications.size();
		String localizedString = "localized";
		try (MockedStatic<Localization> utilities = mockStatic(Localization.class)) {
			utilities.when(() -> Localization.getLocalized(eq("minicraft.notification.dig_hole"), any()))
				.thenReturn(localizedString);
			assertFalse(ornate.interactOn(mockTile,mockLevel,0,0,null,null));
		}
		assertNotEquals(initialSize,Game.notifications.size());
		assertTrue(Game.notifications.contains(localizedString));
		Game.notifications.remove(localizedString); // undo side effect
	}

	@Test
	public void testEqualsDifferentType() {
		assertFalse(brick.equals(mock(Item.class)));
	}

	@Test
	public void testEqualsSameItem() {
		assertTrue(brick.equals(brick));
	}

	@Test
	public void testHashCode() {
		assertNotEquals(brick.hashCode(),stone.hashCode());
	}
}
