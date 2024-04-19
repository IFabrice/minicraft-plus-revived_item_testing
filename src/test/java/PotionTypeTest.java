import minicraft.core.Game;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.item.ClothingItem;
import minicraft.item.Items;
import minicraft.item.PotionType;
import minicraft.level.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PotionTypeTest {

	private static PotionType awkward;
	private static int awkwardDispColor = 1;
	private static PotionType speed;
	private static int speedDispColor = 2;
	private static PotionType light;
	private static int lightDispColor = 3;
	private static PotionType swim;
	private static int swimDispColor = 4;
	private static PotionType energy;
	private static int energyDispColor = 5;
	private static PotionType regen;
	private static int regenDispColor = 6;
	private static PotionType health;
	private static int healthDispColor = 7;
	private static PotionType escape;
	private static int escapeColor = 8;


	@BeforeAll
	public static void setUp() {
		try (MockedStatic<Color> utilities = mockStatic(Color.class)) {
			utilities.when(() -> Color.get(1, 41, 51, 255))
				.thenReturn(awkwardDispColor);
			awkward = PotionType.Awkward;
			utilities.when(() -> Color.get(1, 105, 209, 105))
				.thenReturn(speedDispColor);
			speed = PotionType.Speed;
			utilities.when(() -> Color.get(1, 183, 183, 91))
				.thenReturn(lightDispColor);
			light = PotionType.Light;
			utilities.when(() -> Color.get(1, 51, 51, 255))
				.thenReturn(swimDispColor);
			swim = PotionType.Swim;
			utilities.when(() -> Color.get(1, 237, 110, 78))
				.thenReturn(energyDispColor);
			energy = PotionType.Energy;
			utilities.when(() -> Color.get(1, 219, 70, 189))
				.thenReturn(regenDispColor);
			regen = PotionType.Regen;
			utilities.when(() -> Color.get(1, 194, 56, 84))
				.thenReturn(healthDispColor);
			health = PotionType.Health;
			utilities.when(() -> Color.get(1, 222, 162, 162))
				.thenReturn(escapeColor);
			escape = PotionType.Escape;
		}
	}

	@Test
	public void testAwkwardVariables() {
		assertEquals(0,awkward.duration);
	}

	@Test
	public void testAwkwardToggleEffect() {
		assertFalse(awkward.toggleEffect(null,true));
	}

	@Test
	public void testSpeedVariables() {
		assertEquals(4200,speed.duration);
	}

	@Test
	public void testSpeedToggleEffectNoAddEffectFast() {
		Player mockPlayer = mock(Player.class);
		mockPlayer.moveSpeed = 10;
		assertTrue(speed.toggleEffect(mockPlayer,false));
		assertEquals(9,mockPlayer.moveSpeed);
	}

	@Test
	public void testSpeedToggleEffectNoAddEffectSlow() {
		Player mockPlayer = mock(Player.class);
		mockPlayer.moveSpeed = 0.5;
		assertTrue(speed.toggleEffect(mockPlayer,false));
		assertEquals(0.5,mockPlayer.moveSpeed);
	}

	@Test
	public void testSpeedToggleEffectAddEffect() {
		Player mockPlayer = mock(Player.class);
		mockPlayer.moveSpeed = 10;
		assertTrue(speed.toggleEffect(mockPlayer,true));
		assertEquals(11,mockPlayer.moveSpeed);
	}

	@Test
	public void testLightVariables() {
		assertEquals(6000,light.duration);
	}

	@Test
	public void testLightToggleEffect() {
		assertTrue(light.toggleEffect(null,true));
	}


	@Test
	public void testSwimVariables() {
		assertEquals(4800,swim.duration);
	}

	@Test
	public void testSwimToggleEffect() {
		assertTrue(swim.toggleEffect(null,true));
	}

	@Test
	public void testEnergyVariables() {
		assertEquals(8400,energy.duration);
	}

	@Test
	public void testEnergyToggleEffect() {
		assertTrue(energy.toggleEffect(null,true));
	}

	@Test
	public void testRegenVariables() {
		assertEquals(1800,regen.duration);
	}

	@Test
	public void testRegenToggleEffect() {
		assertTrue(light.toggleEffect(null,true));
	}

	@Test
	public void testHealthVariables() {
		assertEquals(0,health.duration);
	}

	@Test
	public void testHealthToggleEffectAddEffect() {
		ArgumentCaptor<Integer> valueCapture = ArgumentCaptor.forClass(Integer.class);
		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).heal(valueCapture.capture());
		assertTrue(health.toggleEffect(mockPlayer,true));
		assertEquals(5,valueCapture.getValue());
	}

	@Test
	public void testHealthToggleEffectNoAddEffect() {
		Player mockPlayer = mock(Player.class);
		doNothing().when(mockPlayer).heal(anyInt());
		assertTrue(health.toggleEffect(mockPlayer,false));
		verify(mockPlayer,times(0)).heal(anyInt());
	}

	@Test
	public void testEscapeVariables() {
		assertEquals(0,escape.duration);
	}

	@Test
	public void testEscapeNoToggleEffect() {
		Player mockPlayer = mock(Player.class);
		assertTrue(escape.toggleEffect(mockPlayer,false));
	}

	@Test
	public void testEscapeToggleEffectWithAddEffectOverWorldDepth() {
		Player mockPlayer = mock(Player.class);
		Level mockLevel = mock(Level.class);
		when(mockPlayer.getLevel()).thenReturn(mockLevel);
		assertFalse(escape.toggleEffect(mockPlayer,true));
	}


	@Test
	public void testTransmitEffect() {
		assertTrue(escape.transmitEffect());
	}














}
