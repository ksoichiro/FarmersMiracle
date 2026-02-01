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
package com.farmersmiracle.mixin;

import com.farmersmiracle.effects.GrainGrowthHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.level.block.StemBlock.class)
public abstract class StemBlockMixin extends Block {
    @Unique
    private static boolean farmersmiracle$inBonusTick = false;

    protected StemBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "randomTick", at = @At("TAIL"))
    private void farmersmiracle$onRandomTick(BlockState state, ServerLevel level, BlockPos pos,
                                              RandomSource random, CallbackInfo ci) {
        if (farmersmiracle$inBonusTick) {
            return;
        }
        if (GrainGrowthHandler.shouldApplyBonusGrowth(level.dimension(), pos, random)) {
            farmersmiracle$inBonusTick = true;
            try {
                this.randomTick(state, level, pos, random);
            } finally {
                farmersmiracle$inBonusTick = false;
            }
        }
    }
}
