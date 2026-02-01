/*
 * Copyright (C) 2025 ksoichiro
 *
 * This file is part of Farmer's Miracle.
 *
 * Farmer's Miracle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * Farmer's Miracle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Farmer's Miracle. If not, see <https://www.gnu.org/licenses/>.
 */
package com.farmersmiracle.events;

import com.farmersmiracle.FarmersMiracle;
import com.farmersmiracle.data.PlayerBuffData;
import com.farmersmiracle.effects.BuffedPlayerCache;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Registers and handles mod events:
 * - Server tick: updates buffed player cache
 * - Player advancement: grants buffs based on achievement
 */
public class FarmersMiracleEvents {
    private static final ResourceLocation ADV_THE_FIRST_HARVEST =
            ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "the_first_harvest");
    private static final ResourceLocation ADV_FRUIT_OF_THE_EARTH =
            ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "fruit_of_the_earth");
    private static final ResourceLocation ADV_A_JUICY_REWARD =
            ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "a_juicy_reward");
    private static final ResourceLocation ADV_PILGRIMAGE_OF_GRAINS =
            ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "pilgrimage_of_grains");

    public static void register() {
        TickEvent.SERVER_POST.register(BuffedPlayerCache::updateCache);
        PlayerEvent.PLAYER_ADVANCEMENT.register(FarmersMiracleEvents::onAdvancement);
    }

    private static void onAdvancement(ServerPlayer player, AdvancementHolder advancementHolder) {
        ResourceLocation id = advancementHolder.id();
        PlayerBuffData data = PlayerBuffData.get(player.serverLevel());

        if (id.equals(ADV_THE_FIRST_HARVEST)
                || id.equals(ADV_FRUIT_OF_THE_EARTH)
                || id.equals(ADV_A_JUICY_REWARD)) {
            int currentLevel = data.getGrowthLevel(player);
            data.setGrowthLevel(player, currentLevel + 1);
            FarmersMiracle.LOGGER.debug("Granted grain growth level {} to {}",
                    currentLevel + 1, player.getName().getString());
        }

        if (id.equals(ADV_PILGRIMAGE_OF_GRAINS)) {
            data.setRangeExpanded(player, true);
            FarmersMiracle.LOGGER.debug("Granted grain range expansion to {}",
                    player.getName().getString());
        }
    }
}
