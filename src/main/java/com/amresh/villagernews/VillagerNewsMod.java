package com.amresh.villagernews;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.Random;
import java.util.function.Function;

public class VillagerNewsMod implements ModInitializer {
    public static final String MOD_ID = "villagernews";
    private static final Random RANDOM = new Random();
    private static long newsTimer = 0;

    public static final Item HANDBOOK = register("handbook");
    public static final Item MAYOR_HAT = register("mayor_hat");
    public static final Item MICROPHONE = register("microphone");
    public static final Item MOUSTACHE = register("moustache");
    public static final Item TESTIFICATE_HELMET = register("testificate_helmet");
    public static final Item VILLAGER_NOSE = register("villager_nose");

    private static final String[] NEWS = {
        "BREAKING NEWS! A villager has discovered a potato!",
        "Villager News: The village is having a very normal day.",
        "BREAKING NEWS! Somebody has rung a bell.",
        "Villager News: A wandering trader has been spotted nearby.",
        "IMPORTANT REPORT: A farmer is looking for carrots.",
        "BREAKING NEWS! Someone built something suspiciously large.",
        "Villager News: The weather remains completely unpredictable.",
        "LIVE REPORT: A villager has misplaced their workstation."
    };

    private static Item register(String name) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
        Function<Item.Properties, Item> factory = Item::new;
        Item item = factory.apply(new Item.Properties().stacksTo(1).setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(HANDBOOK);
            output.accept(MICROPHONE);
            output.accept(MAYOR_HAT);
            output.accept(MOUSTACHE);
            output.accept(TESTIFICATE_HELMET);
            output.accept(VILLAGER_NOSE);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            newsTimer++;
            // About every 60 seconds, send a bulletin if anyone is online.
            if (newsTimer >= 1200) {
                newsTimer = 0;
                if (!server.getPlayerList().getPlayers().isEmpty()) {
                    String bulletin = NEWS[RANDOM.nextInt(NEWS.length)];
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        player.sendSystemMessage(Component.literal("§6[Villager News] §f" + bulletin));
                    }
                }
            }
        });

        VillagerNewsInteractions.register();
    }
}
