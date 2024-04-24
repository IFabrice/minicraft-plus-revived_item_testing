import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.Recipe;
import minicraft.level.Level;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeTest {
	@Test
	public void testConstructor() {
		// Arrange
		String createdItem = "Apple_5";
		String[] reqItems = {"Orange_3", "Banana_2"};

		// Act
		Recipe recipe = new Recipe(createdItem, reqItems);

		// Assert
		assertNotNull(recipe);
		assertEquals("Apple", recipe.getProduct().getName());
		assertEquals(5, recipe.getAmount());
		assertTrue(recipe.getCosts().containsKey("ORANGE"));
		assertEquals(3, recipe.getCosts().get("ORANGE"));
		assertTrue(recipe.getCosts().containsKey("BANANA"));
		assertEquals(2, recipe.getCosts().get("BANANA"));
	}

	@Test
	public void testEmptyReqItems() {
		// Arrange
		String createdItem = "Apple_5";
		String[] reqItems = {};

		// Act
		Recipe recipe = new Recipe(createdItem, reqItems);

		// Assert
		assertNotNull(recipe);
		assertEquals("Apple", recipe.getProduct().getName());
		assertEquals(5, recipe.getAmount());
		assertTrue(recipe.getCosts().isEmpty());
	}

	@Test
	public void testSingleReqItem() {
		// Arrange
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2"};

		// Act
		Recipe recipe = new Recipe(createdItem, reqItems);

		// Assert
		assertNotNull(recipe);
		assertEquals("Apple", recipe.getProduct().getName());
		assertEquals(5, recipe.getAmount());
		assertTrue(recipe.getCosts().containsKey("BANANA"));
		assertEquals(2, recipe.getCosts().get("BANANA"));
	}

	@Test
	public void testDuplicateReqItems() {
		// Arrange
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};

		// Act
		Recipe recipe = new Recipe(createdItem, reqItems);

		// Assert
		assertNotNull(recipe);
		assertEquals("Apple", recipe.getProduct().getName());
		assertEquals(5, recipe.getAmount());
		assertTrue(recipe.getCosts().containsKey("BANANA"));
		assertEquals(5, recipe.getCosts().get("BANANA"));
	}

	@Test
	public void testGetProduct() {
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};
		Recipe recipe = new Recipe(createdItem, reqItems);
		assertEquals("Apple", recipe.getProduct().getName());
	}

	@Test
	public void testGetCosts() {
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};
		Recipe recipe = new Recipe(createdItem, reqItems);
		assertTrue(recipe.getCosts().containsKey("BANANA"));
	}

	@Test
	public void testGetAmount() {
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};
		Recipe recipe = new Recipe(createdItem, reqItems);
		assertEquals(5, recipe.getCosts().get("BANANA"));
	}

	@Test
	public void testGetCanCraft() {
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};
		Recipe recipe = new Recipe(createdItem, reqItems);
		assertFalse(recipe.getCanCraft());
	}

	@Test
	public void testCheckCanCraftCreative() {
		String createdItem = "Apple_5";
		String[] reqItems = {"Banana_2", "Banana_3"};
		String oldMode = (String) Settings.get("mode");
		Settings.set("mode","minicraft.settings.mode.creative");
		Recipe recipe = new Recipe(createdItem, reqItems);
		assertTrue(recipe.checkCanCraft(null));
		Settings.set("mode",oldMode);
	}

	@Test
	public void testGetCanCraftInsufficientItems() {
		// Arrange
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3", "Banana_2");
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.count(any())).thenReturn(0);
		boolean canCraft = recipe.checkCanCraft(player);
		assertFalse(canCraft);
	}

	@Test
	public void testGetCanCraftSufficientItems() {
		// Arrange
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3", "Banana_2");
		when(player.getInventory()).thenReturn(inventory);
		Item orangeMock = mock(Item.class);
		when(orangeMock.getName()).thenReturn("Orange");
		Item bananaMock = mock(Item.class);
		when(bananaMock.getName()).thenReturn("Banana");
		when(inventory.count(any())).thenReturn(100);

		// Act
		boolean canCraft = recipe.checkCanCraft(player);

		// Assert
		assertTrue(canCraft);
	}

	@Test
	public void testConstructorInvalidCreatedItems() {
		new Recipe("Apple_null","Orange_3"); //Doesn't handle
	}

	@Test
	public void testConstructInvalidReqItems() {
		new Recipe("Apple_3","Orange_null"); //Doesn't handle
	}

	@Test
	public void testCraftNoneCraftPlayer() {
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.count(any())).thenReturn(0);
		assertFalse(recipe.craft(player));
	}

	@Test
	public void testCraftCreativeMode() {
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.count(any())).thenReturn(100);
		when(inventory.add(any())).thenReturn(0);
		Level mockLevel = mock(Level.class);
		when(player.getLevel()).thenReturn(mockLevel);
		when(mockLevel.dropItem(anyInt(),anyInt(),any(Item.class))).thenReturn(null);
		try (MockedStatic<Game> utilities = mockStatic(Game.class)) {
			utilities.when(() -> Game.isMode("minicraft.settings.mode.creative"))
				.thenReturn(true);
			assertTrue(recipe.craft(player));
		}
		verify(mockLevel,times(5)).dropItem(anyInt(),anyInt(),any(Item.class));

	}

	@Test
	public void testCraftCreativeModeCantAdd() {
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.count(any())).thenReturn(100);
		when(inventory.add(any())).thenReturn(1);
		Level mockLevel = mock(Level.class);
		when(player.getLevel()).thenReturn(mockLevel);
		when(mockLevel.dropItem(anyInt(),anyInt(),any(Item.class))).thenReturn(null);
		try (MockedStatic<Game> utilities = mockStatic(Game.class)) {
			utilities.when(() -> Game.isMode("minicraft.settings.mode.creative"))
				.thenReturn(true);
			assertTrue(recipe.craft(player));
		}
		verify(mockLevel,times(0)).dropItem(anyInt(),anyInt(),any(Item.class));

	}

	@Test
	public void testCraftNotCreativeModeCantAdd() {
		Player player = mock(Player.class);
		Inventory inventory = mock(Inventory.class);
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		when(player.getInventory()).thenReturn(inventory);
		when(inventory.count(any())).thenReturn(100);
		when(inventory.add(any())).thenReturn(1);
		doNothing().when(inventory).removeItems(any(Item.class),anyInt());
		Level mockLevel = mock(Level.class);
		when(player.getLevel()).thenReturn(mockLevel);
		when(mockLevel.dropItem(anyInt(),anyInt(),any(Item.class))).thenReturn(null);
		try (MockedStatic<Game> utilities = mockStatic(Game.class)) {
			utilities.when(() -> Game.isMode("minicraft.settings.mode.creative"))
				.thenReturn(false);
			assertTrue(recipe.craft(player));
		}
		verify(mockLevel,times(0)).dropItem(anyInt(),anyInt(),any(Item.class));
		verify(inventory,times(1)).removeItems(any(Item.class),anyInt());

	}

	@Test
	public void testEqualsNoneRecipe() {
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
        assertNotEquals(recipe, mock(Item.class));
	}

	@Test
	public void testEqualsRecipeDifferentAmount() {
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		Recipe recipe2 = new Recipe("Apple_6", "Orange_3");
        assertNotEquals(recipe, recipe2);
	}

	@Test
	public void testEqualsRecipeDifferentProduct() {
		Recipe recipe = new Recipe("Apple_5", "Orange_3");
		Recipe recipe2 = new Recipe("banana_5", "Orange_3");
        assertNotEquals(recipe, recipe2);
	}

	@Test
	public void testEqualsRecipeDifferentCost() {
		Recipe recipe = new Recipe("Apple_5", "banana_3");
		Recipe recipe2 = new Recipe("apple_5", "Orange_3");
		assertNotEquals(recipe, recipe2);
	}

	@Test
	public void testEqualsRecipe() {
		Recipe recipe = new Recipe("Apple_5", "orange_3");
		Recipe recipe2 = new Recipe("apple_5", "Orange_3");
		assertEquals(recipe, recipe2);
	}

	@Test
	public void testEqualsHashcode() {
		Recipe recipe = new Recipe("Apple_5", "orange_3");
		Recipe recipe2 = new Recipe("apple_5", "Orange_3");
		assertEquals(recipe.hashCode(), recipe2.hashCode());
	}

	@Test
	public void testNotEqualsHashcode() {
		Recipe recipe = new Recipe("Apple_4", "orange_3");
		Recipe recipe2 = new Recipe("apple_5", "Orange_3");
		assertNotEquals(recipe.hashCode(), recipe2.hashCode());
	}




}
