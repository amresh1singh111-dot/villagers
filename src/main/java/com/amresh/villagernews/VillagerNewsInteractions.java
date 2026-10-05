package com.amresh.villagernews;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public final class VillagerNewsInteractions {
    private VillagerNewsInteractions() {}

    public static void register() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
            if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
            if (!(entity instanceof Villager villager)) return InteractionResult.PASS;

            var held = player.getItemInHand(hand);
            if (held.is(VillagerNewsMod.MICROPHONE)) {
                player.sendSystemMessage(Component.literal("§6[Villager News] §f" + reporterLine(villager)));
                return InteractionResult.SUCCESS;
            }
            if (held.is(VillagerNewsMod.HANDBOOK)) {
                player.sendSystemMessage(Component.literal("§eVillager News Handbook"));
                player.sendSystemMessage(Component.literal("§7Use a microphone on a villager to interview them."));
                player.sendSystemMessage(Component.literal("§7News bulletins appear automatically from time to time."));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

    private static String reporterLine(Villager v) {
        var profession = v.getVillagerData().profession();
        if (profession.is(VillagerProfession.FARMER)) return "Live from the farms: everything is about carrots today.";
        if (profession.is(VillagerProfession.LIBRARIAN)) return "We have reports of a suspiciously large bookshelf.";
        if (profession.is(VillagerProfession.CLERIC)) return "The cleric says the village is completely fine.";
        if (profession.is(VillagerProfession.FLETCHER)) return "An arrow has been fired. More at eleven.";
        return "This villager has declined to comment. Hrrm.";
    }
}
