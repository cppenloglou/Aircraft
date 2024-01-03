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
import minicraft.graphic.Screen;
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
	
	/** Random value for all the entities instances **/
    protected static final Random random = new Random();

    // x, y entity coordinates on the map
    private int x;
    private int y;

    // x, y radius of entity (hitbox)
    private int xr;
    private int yr;

	private boolean removed; // If the entity is to be removed from the level.
	protected Level level; // The level that the entity is on.
    public final int color; // Current color. (deprecated for some cases)

    private int eid; // Entity id

	/**
	 * Default constructor for the Entity class.
	 * Assings null/none values to the instace variables.
	 * The exception is removed which is set to true, and
	 * lastUpdate which is set to System.nanoTime().
	 * @param xr X radius of entity.
	 * @param yr Y radius of entity.
	 */
	protected Entity(int xr, int yr) { // Add color to this later, in color update
		this.xr = xr;
		this.yr = yr;
		
		level = null;
		removed = true;
		color = 0;

		eid = ThreadLocalRandom.current().nextInt();
    }

    public abstract void render(Screen screen); /// used to render the entity on screen.
    /// used to update the entity.

    /**
     * Returns true if the entity is removed from the level, otherwise false.
     * 
     * @return removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Returns the level which this entity belongs in.
     * 
     * @return level
     */
    public Level getLevel() {
        return level;
    }

    /** @return a Rectangle instance using the defined bounds of the entity. */
    protected Rectangle getBounds() {
        return new Rectangle(x, y, xr * 2, yr * 2, Rectangle.CENTER_DIMS);
    }

    /**
     * @return true if this entity is found in the rectangle specified by given two
     * coordinates.
     */
    public boolean isTouching(Rectangle area) {
        return area.intersects(getBounds());
    }

    /** @return if this entity stops other solid entities from moving. */
    public boolean isSolid() {
        return true;
    } // most entities are solid

    /** Determines if the given entity should prevent this entity from moving. */
    public boolean blocks(Entity entity) {
        return isSolid() && entity.isSolid();
    }

    /** Determines if the entity can swim (extended in sub-classes)*/
    public boolean canSwim() {
        return false;
    } 

    // This, strangely enough, determines if the entity can walk on wool; among some other things..?
    public boolean canWool() {
        return false;
    } 

    // used for lanterns... and player? that might be about it, though, so idk if I want to put it here.
    public int getLightRadius() {
        return 0;
    }
    
    protected void setHitboxSize(int w, int h) {
    	this.xr = w;
    	this.yr = h;
    }

    /** if this entity is touched by another entity (extended by sub-classes) */
    protected void touchedBy(Entity entity) {
    }

    /**
     * Interacts with the entity this method is called on
     * 
     * @param player    The player attacking
     * @param item      The item the player attacked with
     * @param attackDir The direction to interact
     * @return If the interaction was successful
     */
    public boolean interact(Player player, @Nullable Item item, Direction attackDir) {
        return false;
    }


	/** Moves an entity horizontally and vertically. Returns whether entity was unimpeded in it's movement.  */
	public boolean move(int xd, int yd) {
		if (Updater.saving || (xd == 0 && yd == 0)) return true; // Pretend that it kept moving
		boolean stopped = !move2(xd, 0); // Used to check if the entity has BEEN stopped, COMPLETELY; below checks for a lack of collision.
        // Becomes false if horizontal movement was successful.
        if (move2(0, yd)) stopped = false; // Becomes false if vertical movement was successful.
		if (!stopped) {
			int xt = x >> 4; // The x tile coordinate that the entity is standing on.
			int yt = y >> 4; // The y tile coordinate that the entity is standing on.
			level.getTile(xt, yt).steppedOn(level, xt, yt, this); // Calls the steppedOn() method in a tile's class. (used for tiles like sand (footprints) or lava (burning))
		}
		return !stopped;
	}

    /**
     * Moves the entity a long only one direction. If xd != 0 then ya should be 0.
     * If xd = 0 then ya should be != 0. Will throw exception otherwise.
     * 
     * @param xd Horizontal move.
     * @param yd Vertical move.
     * @return true if the move was successful, false if not.
     */
    protected boolean move2(int xd, int yd) {
        if (xd == 0 && yd == 0) {
            return true; // Was not stopped
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

        if (checkEntityBlocks(isInside)) {
            return false;
        }

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

    private boolean checkEntityBlocks(List<Entity> isInside) {
        for (Entity entity : isInside) {
            if (entity == this || !entity.blocks(this)) {
                continue;
            }
            return true;
        }
        return false;
    }

    private void updateEntityPosition(int xd, int yd) {
        x += xd;
        y += yd;
    }


    /**
     * This exists as a way to signify that the entity has been removed through
     * player action and/or world action; basically, it's actually gone, not just
     * removed from a level because it's out of range or something. Calls to this
     * method are used to, say, drop items.
     */
    public void die() {
        remove();
    }

    /** Removes the entity from the level. */
    public void remove() {

        removed = true;

        if (level == null) {
        	Logger.warn("Note: remove() called on entity with no level reference: " + getClass());
        } else {
            level.remove(this);
        }
    }

    /**
     * This should ONLY be called by the Level class. To properly remove an entity
     * from a level, use level.remove(entity)
     */
    public void remove(Level level) {
        if (level != this.level) {
            if (Game.debug) Logger.info("Tried to remove entity " + this + " from level it is not in: " + level + "; in level " + this.level);
        } else {
            removed = true; // Should already be set.
            this.level = null;
        }
    }

    /**
     * This should ONLY be called by the Level class. To properly add an entity to a
     * level, use level.add(entity)
     */
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
            return false; // Obviously, if they are on different levels, they can't be next to each other.
        }

        // Calculate the distance between the two entities, in entity coordinates.
        double distance = Math.abs(Math.hypot(x - other.x, y - other.y));

        // Compare the distance (converted to tile units) with the specified radius.
        return Math.round(distance) >> 4 <= tileRadius; 
    }

    /**
     * Returns the closest player to this entity.
     * 
     * @return the closest player.
     */
    protected Player getClosestPlayer() {
        return getClosestPlayer(true);
    }

    /**
     * Returns the closes player to this entity. If this is called on a player it
     * can return itself.
     * 
     * @param returnSelf determines if the method can return itself.
     * @return The closest player to this entity.
     */
    protected Player getClosestPlayer(boolean returnSelf) {
        if (this instanceof Player && returnSelf) {
            return (Player) this;
        }

        if (level == null) return null;
        return level.getClosestPlayer(x, y);
    }

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
