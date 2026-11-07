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
package com.farmersmiracle.effects;

import com.farmersmiracle.data.PlayerBuffData;
import com.farmersmiracle.events.FarmersMiracleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Caches buffed player positions per server tick for efficient lookup from Mixin code.
 * Rebuilt every tick from online players; no login/logout handling needed.
 */
public class BuffedPlayerCache {
    // Interval for re-applying missing display effects (5 seconds)
    private static final int EFFECT_CHECK_INTERVAL_TICKS = 100;
    private static List<BuffedPlayerEntry> cachedPlayers = Collections.emptyList();
    private static int effectCheckCounter = 0;

    public static void updateCache(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        PlayerBuffData data = PlayerBuffData.get(overworld);

        boolean checkEffects = ++effectCheckCounter >= EFFECT_CHECK_INTERVAL_TICKS;
        if (checkEffects) {
            effectCheckCounter = 0;
        }

        List<BuffedPlayerEntry> entries = new ArrayList<>();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (checkEffects) {
                FarmersMiracleEvents.ensureEffects(player, data);
            }
            int growthLevel = data.getGrowthLevel(player);
            if (growthLevel > 0) {
                entries.add(new BuffedPlayerEntry(
                        player.blockPosition(),
                        player.level().dimension(),
                        growthLevel,
                        data.getEffectiveRadius(player)
                ));
            }
        }
        cachedPlayers = entries;
    }

    public static List<BuffedPlayerEntry> getCachedPlayers() {
        return cachedPlayers;
    }

    public record BuffedPlayerEntry(
            BlockPos pos,
            ResourceKey<Level> dimension,
            int growthLevel,
            int radius
    ) {
        public boolean isInRange(ResourceKey<Level> blockDimension, BlockPos blockPos) {
            if (!dimension.equals(blockDimension)) {
                return false;
            }
            double distSq = pos.distSqr(blockPos);
            return distSq <= (double) radius * radius;
        }
    }
}
