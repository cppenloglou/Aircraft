package minicraft.entity;

import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;

public class Spark extends Entity {

	private Mob owner; // The mob that created this spark
	private final int lifeTime; // how much time until the spark disappears
	private double xa, ya; // the x and y acceleration
	private double xx, yy; // the x and y positions
	private int durTime = 15 * 13 + random.nextInt(30);
	private int time; // the amount of time that has passed
	private int type;

	/**
	 * Creates a new spark. Owner is the AirWizard which is spawning this spark.
	 * 
	 * @param owner The AirWizard spawning the spark.
	 * @param xa	X velocity.
	 * @param ya	Y velocity.
	 */
	public Spark(Mob owner, double xa, double ya, int type) {
		super(0, 0);

		this.owner = owner;
		xx = owner.x;
		yy = owner.y;
		this.xa = xa;
		this.ya = ya;
		this.type = type;

		lifeTime = durTime;
	}

	private void SparkCloud(int damage) {
	    durTime = 15 * 13 + random.nextInt(30);

	    // Move the spark:
	    xx += xa; x = (int) xx;
	    yy += ya; y = (int) yy;

	    Player player = getClosestPlayer();

	    if (player != null) {
	        int xd = owner.x - x;
	        int yd = owner.y - y;

	        int sig = 2; 
	        xa = 0; ya = 0;

	        if (xd < sig) xa -= random.nextInt(2);
	        if (xd > sig) xa += random.nextInt(2);
	        if (yd < sig) ya -= random.nextInt(2);
	        if (yd > sig) ya += random.nextInt(2);
	        
	        if (random.nextBoolean()) {
	        	xa -= random.nextInt(5);
	        	ya -= random.nextInt(5);
	        } else {
	        	xa += random.nextInt(5);
	        	ya += random.nextInt(5);
	        }

	        if (player.isWithin(0, this)) {
	            player.hurt(owner, damage);
	        }
	    }
	}

	private void SparkRain(int damage) {
		durTime = 25 * 10 + random.nextInt(20);

		// move the spark to the player positon:
		xx += xa; x = (int) xx;
		yy += ya; y = (int) yy;

		Player player = getClosestPlayer();
		if (player != null) { // avoid NullPointer if player dies
			if (player.isWithin(0, this)) {
				player.hurt(owner, damage);
			}
		}
	}

	private void SparkSpiralRain(int damage) {
		durTime = 30 * 10 + random.nextInt(30);

		// move the spark:
		xx += xa; x = (int) xx;
		yy += ya; y = (int) yy;

		Player player = getClosestPlayer();
		if (player != null) { // avoid NullPointer if player dies
			if (player.isWithin(0, this)) {
				player.hurt(owner, damage);
			}
		}
	}

	@Override
	public void tick() {
		time++;

		if (type == 1) SparkCloud(1);
		if (type == 2) SparkRain(2);
		if (type == 3) SparkSpiralRain(3);

		if (time >= lifeTime) {
			remove(); // Remove this from the world
			return;
		}
	}


	@Override /** Can this entity block you? Nope. */
	public boolean isSolid() {
		return false;
	}

	@Override
	public void render(Screen screen) {
		int randmirror = 0;

		// If we are in a menu, or we are on a server.
		if (Game.getDisplay() == null) {
			// The blinking effect.
			if (time >= lifeTime - 6 * 20) {
				if (time / 6 % 2 == 0) {
					return; // If time is divisible by 12, then skip the rest of the code.
				}
			}
			randmirror = random.nextInt(4);
		}

		if ((boolean) Settings.get("shadows") == true) {
			screen.render(x - 4, y - 4 + 2, 0 + 20 * 32, randmirror, 2, -1, false, Color.BLACK); // renders the shadow on the ground
		}        
		screen.render(x - 4, y - 4 - 2, 0 + 20 * 32, randmirror, 2); // renders the spark
	}

	/**
	 * Returns the owners id as a string.
	 * 
	 * @return the owners id as a string.
	 */
	public String getData() {
		return owner.eid + "";
	}
}