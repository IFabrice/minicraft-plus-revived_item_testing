package minicraft.level.tile;

import minicraft.entity.Entity;
import minicraft.entity.Mob;
import minicraft.entity.Player;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

public class SandTile extends Tile {
	static Sprite steppedOn, normal = Sprite.dots(Color.get(552, 550, 440, 440));
	static {
		Sprite.Px[][] pixels = new Sprite.Px[2][2];
		pixels[0][0] = new Sprite.Px(3, 1, 0);
		pixels[0][1] = new Sprite.Px(1, 0, 0);
		pixels[1][0] = new Sprite.Px(1, 0, 0);
		pixels[1][1] = new Sprite.Px(3, 1, 0);
		steppedOn = new Sprite(pixels, Color.get(552, 550, 440, 440));
	}
	
	private ConnectorSprite sprite = new ConnectorSprite(SandTile.class, new Sprite(11, 0, 3, 3, Color.get(440, 550, 440, 321), 3), normal)
	{
		public boolean connectsTo(Tile tile, boolean isSide) {
			if(!isSide) return true;
			return tile.connectsToSand;
		}
	};
	
	protected SandTile(String name) {
		super(name, (ConnectorSprite)null);
		csprite = sprite;
		connectsToSand = true;
		maySpawn = true;
	}
	
	//public static int col = Color.get(552, 550, 440, 440);
	//public static int colt = Color.get(440, 550, 440, 321);
	
	public void render(Screen screen, Level level, int x, int y) {
		/*int transitionColor = colt;
		
		boolean u = !level.getTile(x, y - 1).connectsToSand;
		boolean d = !level.getTile(x, y + 1).connectsToSand;
		boolean l = !level.getTile(x - 1, y).connectsToSand;
		boolean r = !level.getTile(x + 1, y).connectsToSand;
		*/
		boolean steppedOn = level.getData(x, y) > 0;
		
		if(steppedOn) csprite.full = SandTile.steppedOn;
		else csprite.full = Sprite.dots(Color.get(552, 550, 440, 440));
		
		csprite.render(screen, level, x, y);
		/*
		if (!u && !l) {
			if (!steppedOn) screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
			else screen.render(x * 16 + 0, y * 16 + 0, 3 + 1 * 32, col, 0);
		} else
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
		
		if (!u && !r) {
			screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
		} else
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
		
		if (!d && !l) {
			screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
		} else
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		
		if (!d && !r) {
			if (!steppedOn) screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
			else screen.render(x * 16 + 8, y * 16 + 8, 3 + 1 * 32, col, 0);
		
		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		*/
	}

	public void tick(Level level, int x, int y) {
		int d = level.getData(x, y);
		if (d > 0) level.setData(x, y, d - 1);
	}

	public void steppedOn(Level level, int x, int y, Entity entity) {
		if (entity instanceof Mob) {
			level.setData(x, y, 10);
		}
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel) {
				if (player.payStamina(4 - tool.level)) {
					level.setTile(xt, yt, Tiles.get("dirt"));
					level.dropItem(xt*16, yt*16, Items.get("sand"));
					return true;
				}
			}
		}
		return false;
	}
}
