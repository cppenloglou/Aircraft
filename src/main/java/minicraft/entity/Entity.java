package minicraft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.entity.mob.Player;
import minicraft.graphic.Rectangle;
import minicraft.item.Item;
import minicraft.level.Level;
import minicraft.network.Network;

public abstract class Entity implements Tickable {

	/* I guess I should explain something real quick. The coordinates between tiles and entities are different.
	 * The world coordinates for tiles is 128x128
	 * The world coordinates for entities is 2048x2048
	 * This is because each tile is 16x16 pixels big
	 * 128 x 16 = 2048.
	 * When ever you see a ">>", it means that it is a right shift operator. This means it shifts bits to the right (making them smaller)
	 * x >> 4 is the equivalent to x / (2^4). Which means it's dividing the X value by 16. (2x2x2x2 = 16)
	 * xt << 4 is the equivalent to xt * (2^4). Which means it's multiplying the X tile value by 16.
	 *
	 * These bit shift operators are used to easily get the X & Y coordinates of a tile that the entity is standing on (have better performance than / or *)
	 */

	// Entity coordinates are per pixel, not per tile; each tile is 16x16 entity pixels.

    // Fields related to entity coordinates
    protected int x;
    protected int y;
    protected int xr;
    protected int yr;

    // Fields related to entity state
    private boolean removed;
    protected Level level;
    public final int color;
    private int eid;

    // Random value for all entity instances
    protected static final Random random = new Random();

    protected Entity(int xr, int yr) {
        this.xr = xr;
        this.yr = yr;

        level = null;
        removed = true;
        color = 0;

        eid = ThreadLocalRandom.current().nextInt();
    }

    // Methods related to entity state
    public boolean isRemoved() {
        return removed;
    }

    public Level getLevel() {
        return level;
    }

    protected void setHitboxSize(int w, int h) {
        this.xr = w;
        this.yr = h;
    }

    protected Rectangle getBounds() {
        return new Rectangle(x, y, xr * 2, yr * 2, Rectangle.CENTER_DIMS);
    }

    // Methods related to entity behavior
    public boolean isTouching(Rectangle area) {
        return area.intersects(getBounds());
    }

    public abstract int getLightRadius();

    protected abstract void touchedBy(Entity entity);

    public abstract boolean interact(Player player, @Nullable Item item, Direction attackDir);

    // Methods related to entity movement
    public boolean move(int xd, int yd) {
        if (Updater.saving || (xd == 0 && yd == 0)) return true;
        boolean stopped = !move2(xd, 0);
        if (move2(0, yd)) stopped = false;
        if (!stopped) {
            int xt = x >> 4;
            int yt = y >> 4;
            level.getTile(xt, yt).steppedOn(level, xt, yt, this);
        }
        return !stopped;
    }

    protected boolean move2(int xd, int yd) {
        if (xd == 0 && yd == 0) {
            return true;
        }

        int[] currentTileCoords = getCurrentTileCoords();
        int[] newTileCoords = getNewTileCoords(xd, yd);

        for (int yt = newTileCoords[1]; yt <= newTileCoords[3]; yt++) {
            for (int xt = newTileCoords[0]; xt <= newTileCoords[2]; xt++) {
                if (isSpriteTouchingTile(xt, yt, currentTileCoords)) {
                    continue;
                }

                level.getTile(xt, yt).bumpedInto(level, xt, yt, this);

                if (!level.getTile(xt, yt).mayPass(level, xt, yt, this)) {
                    return false;
                }
            }
        }

        List<Entity> isInside = getEntitiesInNewRect(xd, yd);

        handleEntityInteractions(isInside);

        updateEntityPosition(xd, yd);
        return true;
    }


    private List<Entity> getEntitiesInNewRect(int xd, int yd) {
        int xr = this.xr;
        int yr = this.yr;
        Rectangle newRect = new Rectangle(x + xd, y + yd, xr * 2, yr * 2, Rectangle.CENTER_DIMS);
        return level.getEntitiesInRect(newRect);
    }


    private int[] getCurrentTileCoords() {
        int xto0 = ((x) - xr) >> 4;
        int yto0 = ((y) - yr) >> 4;
        int xto1 = ((x) + xr) >> 4;
        int yto1 = ((y) + yr) >> 4;
        return new int[]{xto0, yto0, xto1, yto1};
    }

    private int[] getNewTileCoords(int xd, int yd) {
        int xt0 = ((x + xd) - xr) >> 4;
        int yt0 = ((y + yd) - yr) >> 4;
        int xt1 = ((x + xd) + xr) >> 4;
        int yt1 = ((y + yd) + yr) >> 4;
        return new int[]{xt0, yt0, xt1, yt1};
    }

    private boolean isSpriteTouchingTile(int xt, int yt, int[] currentTileCoords) {
        return xt >= currentTileCoords[0] && xt <= currentTileCoords[2] &&
                yt >= currentTileCoords[1] && yt <= currentTileCoords[3];
    }

    private void handleEntityInteractions(List<Entity> isInside) {
        for (Entity entity : isInside) {
            if (entity == this || (entity instanceof Player && !(this instanceof Player))) {
                continue;
            }
            entity.touchedBy(this);
        }
    }

    private void updateEntityPosition(int xd, int yd) {
        x += xd;
        y += yd;
    }

    // Methods related to entity removal
    public void die() {
        remove();
    }

    public void remove() {
        removed = true;

        if (level == null) {
            Logger.warn("Note: remove() called on entity with no level reference: " + getClass());
        } else {
            level.remove(this);
        }
    }

    public void remove(Level level) {
        if (level != this.level) {
            if (Game.debug) Logger.info("Tried to remove entity " + this + " from level it is not in: " + level + "; in level " + this.level);
        } else {
            removed = true;
            this.level = null;
        }
    }

    // Methods related to entity level and player interactions
    public void setLevel(Level level, int x, int y) {
        if (level == null) {
            Logger.warn("Tried to set level of entity " + this + " to a null level; Should use remove(level)");
            return;
        }

        this.level = level;
        removed = false;
        this.x = x;
        this.y = y;

        if (eid < 0) eid = Network.generateUniqueEntityId();
    }

    public boolean isWithin(int tileRadius, Entity other) {
        if (level == null || other.getLevel() == null) {
            return false;
        }
        if (level.depth != other.getLevel().depth) {
            return false;
        }

        double distance = Math.abs(Math.hypot(x - other.x, y - other.y));

        return Math.round(distance) >> 4 <= tileRadius;
    }

    protected Player getClosestPlayer() {
        return getClosestPlayer(true);
    }

    protected Player getClosestPlayer(boolean returnSelf) {
        if (this instanceof Player && returnSelf) {
            return (Player) this;
        }

        if (level == null) return null;
        return level.getClosestPlayer(x, y);
    }

    // Other methods
    public String toString() {
        return getClass().getSimpleName() + getDataPrints();
    }

    protected List<String> getDataPrints() {
        List<String> prints = new ArrayList<>();
        prints.add("eid=" + eid);
        return prints;
    }

    @Override
    public final boolean equals(Object other) {
        return other instanceof Entity && hashCode() == other.hashCode();
    }

    @Override
    public final int hashCode() {
        return eid;
    }
}
