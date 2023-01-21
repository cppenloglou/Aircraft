package minicraft.entity.mob;

import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.gfx.MobSprite;
import minicraft.item.Item;
import minicraft.item.Items;

public class Pig extends PassiveMob {
    private static final MobSprite[][] sprites = MobSprite.compileMobSpriteAnimations(10, 38);
    private int tickTime = 0;

    /**
     * Creates a pig.
     */
    public Pig() {
        super(sprites);
    }

    public void tick() {
        super.tick();
        tickTime++;

		// follows to the player if holds a carrot
		followOnHold(5, "Carrot", false);
        
		// Pig sounds
		if (tickTime / 8 % 16 == 0 && random.nextInt(8) == 4) {
			if (random.nextBoolean()) {
				if (!random.nextBoolean()) {
					Sound.pigSay1.playOnWorld(x, y);
				} else {
					Sound.pigSay2.playOnWorld(x, y);
				}
			} else {
				Sound.pigSay3.playOnWorld(x, y);
			}
		}
    }

    public void die() {
        int min = 0, max = 0;
        String difficulty = (String) Settings.get("diff");
        
        if (difficulty == "Peaceful" || difficulty == "Easy") { min = 1; max = 3; }
        if (difficulty == "Normal") { min = 1; max = 2; }
        if (difficulty == "Hard") { min = 0; max = 2; }
        
        dropItem(min, max, new Item[] {
        		Items.get("raw pork")
        });

        super.die();
    }
}