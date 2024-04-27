import minicraft.core.io.Localization;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.Particle;
import minicraft.entity.particle.WaterParticle;
import minicraft.gfx.Point;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.WateringCanItem;
import minicraft.level.Level;
import minicraft.level.tile.DirtTile;
import minicraft.level.tile.GrassTile;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import minicraft.level.tile.WaterTile;
import minicraft.level.tile.farming.CropTile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WateringCanItemTest {
	private static final ArrayList<WateringCanItem> wateringCanItems = new ArrayList<>();
	private static WateringCanItem wateringCanItem;

	@BeforeAll
	public static void setUp() {
		ArrayList<Item> allItems = Items.getAll();
		for (Item item : allItems) {
			if (item instanceof WateringCanItem) {
				wateringCanItems.add((WateringCanItem) item);
			}
		}
		wateringCanItem = wateringCanItems.get(0);
	}

	@Test
	public void testGetAllInstances() {
		assertEquals(1,wateringCanItems.size());
	}

	@Test
	public void testConstructor() {
		assertEquals(0,wateringCanItem.content);
		assertEquals(1800,wateringCanItem.CAPACITY);
		assertEquals("Watering Can",wateringCanItem.getName());
	}

	@Test
	public void testInteractOnInstanceOfWaterTile() {
		Tile mockTile = mock(WaterTile.class);
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		assertTrue(testWateringCan.interactOn(mockTile,null,0,0,null,null));
		assertEquals(1800,testWateringCan.content);
	}

	@Test
	public void testInteractOnInstanceOfNotWaterTile0Content() {
		Tile mockTile = mock(Tile.class);
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		assertFalse(testWateringCan.interactOn(mockTile,null,0,0,null,null));
	}

	@Test
	public void testInteractOnInstanceOfCropTilePositiveContent() {
		CropTile mockTile = mock(CropTile.class);
		when(mockTile.getFertilization(anyInt())).thenReturn(50);
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(3);
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		testWateringCan.content = 1;
		assertTrue(testWateringCan.interactOn(mockTile,mockLevel,0,0,null,null));
		assertEquals(0,testWateringCan.content);
		verify(mockTile,times(1)).fertilize(any(),anyInt(),anyInt(),anyInt());
	}

	@Test
	public void testInteractOnInstanceOfTilePositiveContentRenderingTick8() {
		Tile mockTile = mock(Tile.class);
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(3);
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		testWateringCan.content = 100;
		Direction mockDirection = mock(Direction.class);
		when(mockDirection.getX()).thenReturn(5);
		when(mockDirection.getY()).thenReturn(10);
		Player mockPlayer = mock(Player.class);
		for(int i = 0; i < 8; i++) {
			testWateringCan.interactOn(mockTile,mockLevel,0,0,mockPlayer,mockDirection);
		}
		verify(mockLevel,times(4)).add(any(WaterParticle.class));
	}

	@Test
	public void testInteractOnInstanceOfCropTilePositiveContentForceRandom() {
		CropTile mockTile = mock(CropTile.class);
		Level mockLevel = mock(Level.class);
		when(mockLevel.getData(anyInt(),anyInt())).thenReturn(3);
		when(mockTile.getFertilization(anyInt())).thenReturn(500);
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		testWateringCan.content = 100;
		Direction mockDirection = mock(Direction.class);
		when(mockDirection.getX()).thenReturn(5);
		when(mockDirection.getY()).thenReturn(10);
		Player mockPlayer = mock(Player.class);
		for(int i = 0; i < 100; i++) {
			testWateringCan.interactOn(mockTile,mockLevel,0,0,mockPlayer,mockDirection);
		}
		verify(mockLevel,times(48)).add(any(WaterParticle.class));
		verify(mockLevel,atLeast(49)).add(any(Particle.class));
	}

	@Test
	public void testInteractOnGrassTileNoDirtTileExists() {
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		testWateringCan.content = 100;
		GrassTile mockTile = mock(GrassTile.class);
		Tile returnTile = mock(Tile.class);
		Level mockLevel = mock(Level.class);
		Point[] mockPoints = {mock(Point.class)};
		when(mockLevel.getAreaTilePositions(anyInt(),anyInt(),anyInt())).thenReturn(mockPoints);
		when(mockLevel.getTile(anyInt(),anyInt())).thenReturn(returnTile);
		try (MockedStatic<Tiles> utilities = mockStatic(Tiles.class)) {
			utilities.when(() -> Tiles.get(any()))
				.thenReturn(returnTile);
			assertTrue(testWateringCan.interactOn(mockTile,mockLevel,0,0,null,null));
		}
	}

	@Test
	public void testInteractOnDirtTileNoGrassTileExists() {
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		testWateringCan.content = 100;
		DirtTile mockTile = mock(DirtTile.class);
		Tile returnTile = mock(Tile.class);
		Level mockLevel = mock(Level.class);
		Point[] mockPoints = {mock(Point.class)};
		when(mockLevel.getAreaTilePositions(anyInt(),anyInt(),anyInt())).thenReturn(mockPoints);
		when(mockLevel.getTile(anyInt(),anyInt())).thenReturn(returnTile);
		try (MockedStatic<Tiles> utilities = mockStatic(Tiles.class)) {
			utilities.when(() -> Tiles.get(any()))
				.thenReturn(returnTile);
			assertTrue(testWateringCan.interactOn(mockTile,mockLevel,0,0,null,null));
		}
	}

	@Test
	public void testInteractOnGrassTileDirtTileExistsForceRandom() {
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		int content = 10000;
		testWateringCan.content = content;
		GrassTile mockTile = mock(GrassTile.class);
		DirtTile returnTile = mock(DirtTile.class);
		DirtTile returnShort = mock(DirtTile.class);
		DirtTile returnString = mock(DirtTile.class);
		Level mockLevel = mock(Level.class);
		Point[] mockPoints = {mock(Point.class)};
		when(mockLevel.getAreaTilePositions(anyInt(),anyInt(),anyInt())).thenReturn(mockPoints);
		when(mockLevel.getTile(anyInt(),anyInt())).thenReturn(returnTile);
		Direction mockDirection = mock(Direction.class);
		when(mockDirection.getX()).thenReturn(5);
		when(mockDirection.getY()).thenReturn(10);
		Player mockPlayer = mock(Player.class);
		try (MockedStatic<Tiles> utilities = mockStatic(Tiles.class)) {
			utilities.when(() -> Tiles.get(anyString()))
				.thenReturn(returnString);
			utilities.when(() -> Tiles.get(anyShort()))
				.thenReturn(returnShort);
			for(int i = 0; i < content; i++) {
				testWateringCan.interactOn(mockTile,mockLevel,0,0,mockPlayer,mockDirection);
			}
			verify(mockLevel,atLeast(1)).add(any(Particle.class));
			verify(mockLevel,atLeast(1)).setTile(anyInt(),anyInt(),eq(returnShort),anyInt());
			verify(mockLevel,atLeast(1)).setTile(anyInt(),anyInt(),eq(returnString));
		}
	}

	@Test
	public void testInteractOnDirtTileGrassTileExistsForceRandom() {
		WateringCanItem testWateringCan = (WateringCanItem) wateringCanItem.copy();
		int content = 10000;
		testWateringCan.content = content;
		DirtTile mockTile = mock(DirtTile.class);
		GrassTile returnTile = mock(GrassTile.class);
		DirtTile returnString = mock(DirtTile.class);
		Level mockLevel = mock(Level.class);
		Point[] mockPoints = {mock(Point.class)};
		when(mockLevel.getAreaTilePositions(anyInt(),anyInt(),anyInt())).thenReturn(mockPoints);
		when(mockLevel.getTile(anyInt(),anyInt())).thenReturn(returnTile);
		Direction mockDirection = mock(Direction.class);
		when(mockDirection.getX()).thenReturn(5);
		when(mockDirection.getY()).thenReturn(10);
		Player mockPlayer = mock(Player.class);
		try (MockedStatic<Tiles> utilities = mockStatic(Tiles.class)) {
			utilities.when(() -> Tiles.get(anyString()))
				.thenReturn(returnString);
			for(int i = 0; i < content; i++) {
				testWateringCan.interactOn(mockTile,mockLevel,0,0,mockPlayer,mockDirection);
			}
			verify(mockLevel,atLeast(1)).add(any(Particle.class));
			verify(mockLevel,atLeast(1)).setTile(anyInt(),anyInt(),eq(returnString));
		}
	}

	@Test
	public void testGetData() {
		assertEquals(wateringCanItem.getName() + "_0",wateringCanItem.getData());
	}


}
