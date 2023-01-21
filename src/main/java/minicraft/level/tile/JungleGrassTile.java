package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

public class JungleGrassTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(JungleGrassTile.class, new Sprite(16, 34, 3, 3, 1), new Sprite(19, 34, 2, 2, 1)) {

		@Override
		public boolean connectsTo(Tile tile, boolean isSide) {
			if (!isSide) {
				return true;
			}
			return tile.connectsToJungleGrass;
		}

	};

	protected JungleGrassTile(String name) {
		super(name, sprite);
		connectorSprite.sides = connectorSprite.sparse;
		connectsToJungleGrass = true;
		maySpawn = true;
	}

	@Override
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Dirt"));
					Sound.genericHurt.playOnGui();
					if (random.nextInt(5) == 0) { // 20% chance to drop seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, 2, Items.get("Seeds"));
					}
					return true;
				}
			}
			if (tool.type == ToolType.Hoe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Dirt"));
					Sound.genericHurt.playOnGui();
					if (random.nextInt(15) == 0) { // 80% chance to drop seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Seeds"));
					}
					if (random.nextInt(64) == 0) { // 80% chance to drop seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Dirt"));
					}
					return true;
				}
			}
			if (tool.type == ToolType.Pickaxe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("path"));
					Sound.genericHurt.playOnGui();
					if (random.nextInt(5) == 0) { // 20% chance to drop seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, 2, Items.get("Seeds"));
					}
				}
			}
		}
		return false;
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		sprite.sparse.color = DirtTile.dCol(level.depth);
		sprite.render(screen, level, x, y);
	}

	@Override
	public boolean tick(Level level, int xt, int yt) {

		if (random.nextInt(40) != 0)  {
			return false;
		}

		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) {
			xn += random.nextInt(2) * 2 - 1;
		} else  {
			yn += random.nextInt(2) * 2 - 1;
		}

		if (level.getTile(xn, yn) == Tiles.get("Dirt")) {
			level.setTile(xn, yn, this);
		}
		return false;
	}
}
