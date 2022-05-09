package minicraft.level.tile;

import minicraft.entity.Entity;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.level.Level;

/// This class is for tiles WHILE THEY ARE EXPLODING
public class ExplodedTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(ExplodedTile.class, new Sprite(6, 22, 3, 3, 1, 3), new Sprite(9, 22, 2, 2, 1)) {
		@Override
		public boolean connectsTo(Tile tile, boolean isSide) {
			return !isSide || tile.connectsToLiquid();
		}
	};

	private boolean light = true;
	private int r;

	protected ExplodedTile(String name) {
		super(name, sprite);
		connectsToSand = true;
		connectsToFluid = true;
	}

	public boolean tick(Level level, int xt, int yt) {
		if (light) {
			r = 3;
			return true;
		}
		return false;
	}

	@Override
	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	@Override
	public int getLightRadius(Level level, int x, int y) {
		return r;
	}
}
