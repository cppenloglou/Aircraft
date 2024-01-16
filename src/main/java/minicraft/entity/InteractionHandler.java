package minicraft.entity;

import org.jetbrains.annotations.Nullable;

import minicraft.entity.mob.Player;
import minicraft.item.Item;

public class InteractionHandler {

    public void handleInteraction(Entity entity, Player player, @Nullable Item item, Direction attackDir) {
        if (entity.interact(player, item, attackDir)) {
            // Additional logic if needed after successful interaction
        }
    }

    public void handleTouch(Entity entity, Entity otherEntity) {
        if (entity == otherEntity) {
            return; // Cannot touch itself
        }

        if (otherEntity instanceof Player) {
            if (!(entity instanceof Player)) {
                entity.touchedBy(otherEntity);
            }
        } else {
            otherEntity.touchedBy(entity);
        }
    }
}

