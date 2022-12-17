package minicraft.level.tile;

import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.FireParticle;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.PowerGloveItem;
import minicraft.level.Level;

public class TorchTile extends Tile {
    private static Sprite sprite = new Sprite(5, 3, 0);
    
    private static int LIGHT = 5;
    private Tile onType;

    private int tickTime = 0;
    
    int spawnX = 0;
    int spawnY = 0;

    public static TorchTile getTorchTile(Tile onTile) {
		int id = onTile.id & 0xFFFF;
		
        if (id < 16384) {
            id += 16384;
        } else {
            System.out.println("tried to place torch on torch tile...");
        }

        if (Tiles.containsTile(id)) {
            return (TorchTile) Tiles.get(id);
        } else {
            TorchTile tile = new TorchTile(onTile);
            Tiles.add(id, tile);
            return tile;
        }
    }

    private TorchTile(Tile onType) {
        super("Torch " + onType.name, sprite);
        this.onType = onType;
        this.connectsToSand = onType.connectsToSand;
        this.connectsToGrass = onType.connectsToGrass;
        this.connectsToSkyGrass = onType.connectsToSkyGrass;
        this.connectsToSkyHighGrass = onType.connectsToSkyHighGrass;
        this.connectsToSkyDirt = onType.connectsToSkyDirt;
        this.connectsToFerrosite = onType.connectsToFerrosite;
        this.connectsToSnow = onType.connectsToSnow;
        this.connectsToFluid = onType.connectsToFluid;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        onType.render(screen, level, x, y);
        sprite.render(screen, x * 16 + 4, y * 16 + 4);
        
		if (tickTime / 2 % 2 == 0 && Settings.get("particles").equals(true)) {
			if (random.nextInt(1) == 0) {
				level.add(new FireParticle(spawnX, spawnY));
			}
		}
    }
    
	public boolean tick(Level level, int x, int y) {
		tickTime++;
		
		spawnX  = x * 16 + 4;
		spawnY = (y * 16) + random.nextInt(2) - random.nextInt(1);
		
		return false;
	}

    @Override
    public int getLightRadius(Level level, int x, int y) {
        return LIGHT;
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
        if (item instanceof PowerGloveItem) {
            level.setTile(xt, yt, this.onType);
            Sound.genericHurt.playOnWorld(xt * 16, yt * 16, player.x, player.y);
            level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Torch"));
            return true;
        } else {
            return false;
        }
    }
}
