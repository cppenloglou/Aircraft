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
    public int x;
    public int y;

    // x, y radius of entity (hitbox)
    public int xr;
    public int yr;

	private boolean removed; // If the entity is to be removed from the level.
	protected Level level; // The level that the entity is on.
    public int color; // Current color. (deprecated for some cases)
    private final MovementHandler movementHandler = new MovementHandler();
    private final InteractionHandler interactionHandler = new InteractionHandler();
    private final LifecycleManager lifecycleManager = new LifecycleManager();
    public int eid; // Entity id

	/**
	 * Default constructor for the Entity class.
	 * Assings null/none values to the instace variables.
	 * The exception is removed which is set to true, and
	 * lastUpdate which is set to System.nanoTime().
	 * @param xr X radius of entity.
	 * @param yr Y radius of entity.
	 */
	public Entity(int xr, int yr) { // Add color to this later, in color update
		this.xr = xr;
		this.yr = yr;
		
		level = null;
		removed = true;
		color = 0;

		eid = ThreadLocalRandom.current().nextInt();
    }

    public abstract void render(Screen screen); /// used to render the entity on screen.

    @Override
    public abstract void tick(); /// used to update the entity.

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

    /**
     * Interacts with the entity this method is called on
     * 
     * @param player    The player attacking
     * @param item      The item the player attacked with
     * @param attackDir The direction to interact
     * @return If the interaction was successful
     */
    public boolean interact(Player player, @Nullable Item item, Direction attackDir) {
        interactionHandler.handleInteraction(this, player, item, attackDir);
        return false;
    }

    protected void touchedBy(Entity entity) {
        interactionHandler.handleTouch(this, entity);
    }


    public boolean move(int xd, int yd) {
        return movementHandler.move(this, xd, yd);
    }

    protected boolean move2(int xd, int yd) {
        return movementHandler.move2(this, xd, yd);
    }

    public void die() {
        lifecycleManager.die(this);
    }

    public void remove() {
        lifecycleManager.remove(this);
    }

    public void remove(Level level) {
        lifecycleManager.remove(this, level);
    }

    public void setLevel(Level level, int x, int y) {
        lifecycleManager.setLevel(this, level, x, y);
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

    public void setRemoved(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRemoved'");
    }

    public void setLevel(Level level2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLevel'");
    }

    public void setX(int x2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setX'");
    }

    public void setY(int y2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setY'");
    }

    public int getEid() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEid'");
    }

    public void setEid(int uniqueEntityId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEid'");
    }
}
