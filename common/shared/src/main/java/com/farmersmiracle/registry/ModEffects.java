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
package com.farmersmiracle.registry;

import com.farmersmiracle.FarmersMiracle;
import com.farmersmiracle.effects.GrainGrowthEffect;
import com.farmersmiracle.effects.GrainSpreadEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(FarmersMiracle.MOD_ID, Registries.MOB_EFFECT);

    public static final RegistrySupplier<MobEffect> GRAIN_GROWTH =
            EFFECTS.register("grain_growth", GrainGrowthEffect::new);

    public static final RegistrySupplier<MobEffect> GRAIN_SPREAD =
            EFFECTS.register("grain_spread", GrainSpreadEffect::new);

    public static void register() {
        EFFECTS.register();
        FarmersMiracle.LOGGER.debug("Registered ModEffects");
    }
}
