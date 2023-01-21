package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

/// this is the typical stone you see underground and on the surface, that gives coal.

public class HolyRockTile extends Tile {
    private ConnectorSprite sprite = new ConnectorSprite(HolyRockTile.class, new Sprite(37, 6, 3, 3, 1), new Sprite(40, 8, 2, 2, 1), new Sprite(40, 6, 2, 2, 1));

    private int coalLvl = 0;

    protected HolyRockTile(String name) {
        super(name, (ConnectorSprite) null);
        connectorSprite = sprite;
        connectsToSkyHighGrass = true;
        connectsToSkyGrass = true;
    }

    public void render(Screen screen, Level level, int x, int y) {
        Tiles.get("Sky High grass").render(screen, level, x, y);
        sprite.render(screen, level, x, y);
        
    }

    public boolean mayPass(Level level, int x, int y, Entity entity) {
        return false;
    }

    public boolean hurt(Level level, int x, int y, Mob source, int hurtDamage, Direction attackDir) {
        hurt(level, x, y, hurtDamage);
        return true;
    }

    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
        // creative mode can just act like survival here
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == ToolType.Pickaxe && player.payStamina(4 - tool.level) && tool.payDurability()) {
                coalLvl = 1;
                hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
                return true;
            }
        }
        return false;
    }

    public void hurt(Level level, int x, int y, int hurtDamage) {
        int damage = level.getData(x, y) + hurtDamage;
        int holyrockHealth = 50;
        if (Game.isMode("Creative")) {
        	hurtDamage = damage = holyrockHealth;
            coalLvl = 1;
        }
        
        level.add(new SmashParticle(x << 4, y << 4));
        Sound.genericHurt.playOnWorld(x << 4, y << 4);
        level.add(new TextParticle("" + hurtDamage, (x << 4) + 8, (y << 4) + 8, Color.RED));
        
        if (damage >= holyrockHealth) {
            if (coalLvl == 0) {
                level.dropItem((x << 4) + 8, (y << 4) + 8, 1, 4, Items.get("Holy Stone"));
            }
            if (coalLvl == 1) {
                level.dropItem((x << 4) + 8, (y << 4) + 8, 1, 2, Items.get("Holy Stone"));
                int mincoal = 0, maxcoal = 1;
                if (!Settings.get("diff").equals("Hard")) {
                    mincoal++;
                    maxcoal++;
                }
                level.dropItem((x << 4) + 8, (y << 4) + 8, mincoal, maxcoal, Items.get("coal"));
            }
            level.setTile(x, y, Tiles.get("Sky High grass"));
        } else {
            level.setData(x, y, damage);
        }
    }

    public boolean tick(Level level, int xt, int yt) {
        int damage = level.getData(xt, yt);
        if (damage > 0) {
            level.setData(xt, yt, damage - 1);
            return true;
        }
        return false;
    }
}
