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

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Blessing of Grains: Growth effect.
 * Display-only; actual growth logic is handled by GrainGrowthHandler via Mixin.
 */
public class GrainGrowthEffect extends MobEffect {
    public GrainGrowthEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7CFC00); // Lawn green
    }
}
