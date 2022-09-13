package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.mob.boss.AirWizard;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.screen.AchievementsDisplay;

public class GoldenCloudTreeTile extends Tile {

    protected GoldenCloudTreeTile(String name) {
        super(name, (ConnectorSprite) null);
        connectsToSkyHighGrass = true;
        connectsToSkyGrass = true;
    }

    private int LIGHT = 5;
    private int tickc = 0;

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        Tiles.get("Sky High Grass").render(screen, level, x, y);

        boolean u = level.getTile(x, y - 1) == this;
        boolean l = level.getTile(x - 1, y) == this;
        boolean r = level.getTile(x + 1, y) == this;
        boolean d = level.getTile(x, y + 1) == this;
        boolean ul = level.getTile(x - 1, y - 1) == this;
        boolean ur = level.getTile(x + 1, y - 1) == this;
        boolean dl = level.getTile(x - 1, y + 1) == this;
        boolean dr = level.getTile(x + 1, y + 1) == this;

        if (u && ul && l) {
            screen.render(x * 16 + 0, y * 16 + 0, 3 + 33 * 32, 0, 1);// x2
        } else {
            screen.render(x * 16 + 0, y * 16 + 0, 2 + 32 * 32, 0, 1);// x1
        }
        if (u && ur && r) {
            screen.render(x * 16 + 8, y * 16 + 0, 3 + 34 * 32, 0, 1); // x2
        } else {
            screen.render(x * 16 + 8, y * 16 + 0, 3 + 32 * 32, 0, 1);// x2
        }
        if (d && dl && l) {
            screen.render(x * 16 + 0, y * 16 + 8, 3 + 34 * 32, 0, 1); // x2
        } else {
            screen.render(x * 16 + 0, y * 16 + 8, 2 + 33 * 32, 0, 1);// x1
        }
        if (d && dr && r) {
            screen.render(x * 16 + 8, y * 16 + 8, 3 + 33 * 32, 0, 1);// x2
        } else {
            screen.render(x * 16 + 8, y * 16 + 8, 3 + 35 * 32, 0, 1); // x2
        }
    }

    @Override
    public boolean tick(Level level, int xt, int yt) {
        int damage = level.getData(xt, yt);
        if (damage > 0) {
            level.setData(xt, yt, damage - 1);
            return true;
        }

        tickc++;

        if (tickc >= 16) {

            if (random.nextInt(5) == 5) {
                LIGHT = 6;
            } else
                LIGHT = 5;

            tickc = 0;
        }
        return false;
    }

    @Override
    public boolean mayPass(Level level, int x, int y, Entity e) {
        return e instanceof AirWizard;
    }

    @Override
    public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
        hurt(level, x, y, dmg);
        return true;
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
        if (Game.isMode("Creative")) {
            return false; // Go directly to hurt method
        }
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == ToolType.Axe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					hurt(level, xt, yt, tool.getDamage());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void hurt(Level level, int x, int y, int dmg) {
        if (random.nextInt(100) == 0)
            level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Apple"));

        int damage = level.getData(x, y) + dmg;
        int treeHealth = 20;
        if (Game.isMode("Creative"))
            dmg = damage = treeHealth;

        level.add(new SmashParticle(x * 16, y * 16));
        Sound.Tile_generic_hurt.play();

        level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
        if (damage >= treeHealth) {
            level.dropItem(x * 16 + 8, y * 16 + 8, 1, 2, Items.get("Oak Wood"));
            level.setTile(x, y, Tiles.get("Sky High Grass"));
            
            AchievementsDisplay.setAchievement("minicraft.achievement.woodcutter", true);
            
        } else {
            level.setData(x, y, damage);
        }
    }

    @Override
    public int getLightRadius(Level level, int x, int y) {
        return LIGHT;
    }
}
