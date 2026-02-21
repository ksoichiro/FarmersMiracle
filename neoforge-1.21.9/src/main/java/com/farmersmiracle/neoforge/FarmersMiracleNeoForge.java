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
package com.farmersmiracle.neoforge;

import com.farmersmiracle.FarmersMiracle;
import com.farmersmiracle.client.particle.GrainGrowthParticle;
import com.farmersmiracle.registry.ModParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod(FarmersMiracle.MOD_ID)
public class FarmersMiracleNeoForge {
    public FarmersMiracleNeoForge(IEventBus modEventBus) {
        FarmersMiracle.init();
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            modEventBus.addListener(FarmersMiracleNeoForge::onRegisterParticleProviders);
        }
    }

    private static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.GRAIN_GROWTH.get(), GrainGrowthParticle.Provider::new);
    }
}
