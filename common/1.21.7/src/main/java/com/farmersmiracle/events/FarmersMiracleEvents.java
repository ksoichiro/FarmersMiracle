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
import com.farmersmiracle.registry.ModEffects;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * Handles mod events:
 * - Player advancement: grants buffs based on achievement
 * - Player join: restores MobEffect display from saved data
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

    private static final int INFINITE_DURATION = -1;

    public static void onAdvancement(ServerPlayer player, AdvancementHolder advancementHolder) {
        ResourceLocation id = advancementHolder.id();
        // Always use overworld for SavedData to avoid per-dimension fragmentation
        PlayerBuffData data = PlayerBuffData.get(player.getServer().overworld());

        if (id.equals(ADV_THE_FIRST_HARVEST)
                || id.equals(ADV_FRUIT_OF_THE_EARTH)
                || id.equals(ADV_A_JUICY_REWARD)) {
            int currentLevel = data.getGrowthLevel(player);
            int newLevel = currentLevel + 1;
            data.setGrowthLevel(player, newLevel);
            applyGrowthEffect(player, newLevel);
            FarmersMiracle.LOGGER.debug("Granted grain growth level {} to {}",
                    newLevel, player.getName().getString());
        }

        if (id.equals(ADV_PILGRIMAGE_OF_GRAINS)) {
            data.setRangeExpanded(player, true);
            applySpreadEffect(player);
            FarmersMiracle.LOGGER.debug("Granted grain range expansion to {}",
                    player.getName().getString());
        }
    }

    public static void onPlayerJoin(ServerPlayer player) {
        PlayerBuffData data = PlayerBuffData.get(player.getServer().overworld());
        int growthLevel = data.getGrowthLevel(player);
        if (growthLevel > 0) {
            applyGrowthEffect(player, growthLevel);
        }
        if (data.isRangeExpanded(player)) {
            applySpreadEffect(player);
        }
    }

    private static void applyGrowthEffect(ServerPlayer player, int level) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.GRAIN_GROWTH.get());
        // Remove existing effect first to update amplifier
        player.removeEffect(holder);
        player.addEffect(new MobEffectInstance(
                holder,
                INFINITE_DURATION,
                level - 1, // amplifier: 0 = Level I, 1 = Level II, etc.
                true,      // ambient (no particles)
                false,     // no visible particles
                true       // show icon
        ));
    }

    private static void applySpreadEffect(ServerPlayer player) {
        Holder<MobEffect> holder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.GRAIN_SPREAD.get());
        player.addEffect(new MobEffectInstance(
                holder,
                INFINITE_DURATION,
                0,
                true,      // ambient (no particles)
                false,     // no visible particles
                true       // show icon
        ));
    }
}
