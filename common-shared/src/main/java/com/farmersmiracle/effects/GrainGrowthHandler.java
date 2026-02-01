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

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

/**
 * Core logic for grain growth acceleration.
 * Called from Mixin on CropBlock/StemBlock randomTick.
 */
public class GrainGrowthHandler {
    private static final double BASE_GROWTH_RATE = 0.03;

    /**
     * Determines whether an additional growth tick should be applied.
     *
     * @param dimension the dimension where the crop is located
     * @param pos       the block position of the crop
     * @param random    the random source
     * @return true if an extra growth step should be applied
     */
    public static boolean shouldApplyBonusGrowth(ResourceKey<Level> dimension, BlockPos pos, RandomSource random) {
        for (BuffedPlayerCache.BuffedPlayerEntry entry : BuffedPlayerCache.getCachedPlayers()) {
            if (entry.isInRange(dimension, pos)) {
                double chance = entry.growthLevel() * BASE_GROWTH_RATE;
                if (random.nextDouble() < chance) {
                    return true;
                }
            }
        }
        return false;
    }
}
