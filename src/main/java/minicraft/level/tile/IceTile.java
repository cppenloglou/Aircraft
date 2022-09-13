package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

public class IceTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(IceTile.class, new Sprite(30, 9, 3, 3, 1), new Sprite(33, 9, 2, 2, 1)) {
		
		@Override
		public boolean connectsTo(Tile tile, boolean isSide) {
			if(!isSide) {
				return true;
			}
			return tile.connectsToIce;
		}
	};

	protected IceTile(String name) {
		super(name, sprite);
		csprite.sides = csprite.sparse;
		connectsToIce = true;
		connectsToFluid = true;
		maySpawn = true;
	}
	
	@Override
	public boolean tick(Level level, int xt, int yt) {
		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) {
			xn += random.nextInt(2) * 2 - 1;
		} else {
			yn += random.nextInt(2) * 2 - 1;
		}

		if (level.getTile(xn, yn) == Tiles.get("Hole")) {
			level.setTile(xn, yn, "Water");
		}
		
		// TODO: torch stuff....

		for (int x = -1; x < 2; x++) {
			if (level.getTile(xt + x, yt) == Tiles.get("Lava")) {
				level.setTile(xt + x, yt, Tiles.get("Raw Obsidian"));
			}
		}

		for (int y = -1; y < 2; y++) {
			if (level.getTile(xt, yt + y) == Tiles.get("lava")) {
				level.setTile(xt, yt + y, Tiles.get("Raw Obsidian"));
			}
		}

		return false;
	}
	
	@Override
	public void render(Screen screen, Level level, int x, int y) {
		Tiles.get("Water").render(screen, level, x, y);
		sprite.render(screen, level, x, y);
	}
	
	@Override
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Water"));
					Sound.Tile_generic_hurt.play();
					//level.dropItem(xt *16 + 8, yt * 16 + 8, 0, 3, Items.get("Ice"));
					return true;
				}
			}
		}
		return false;
	}

}
