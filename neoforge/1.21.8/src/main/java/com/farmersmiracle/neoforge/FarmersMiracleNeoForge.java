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
import com.farmersmiracle.effects.BuffedPlayerCache;
import com.farmersmiracle.effects.GrainGrowthEffect;
import com.farmersmiracle.effects.GrainSpreadEffect;
import com.farmersmiracle.events.FarmersMiracleEvents;
import com.farmersmiracle.registry.ModCreativeTabs;
import com.farmersmiracle.registry.ModEffects;
import com.farmersmiracle.registry.ModItems;
import com.farmersmiracle.registry.ModParticles;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(FarmersMiracle.MOD_ID)
public class FarmersMiracleNeoForge {
    public FarmersMiracleNeoForge(IEventBus modEventBus) {
        registerAll(modEventBus);
        registerEvents();
        FarmersMiracle.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(FarmersMiracleNeoForge::onRegisterParticleProviders);
        }
    }

    private void registerAll(IEventBus modBus) {
        DeferredRegister<Item> items = DeferredRegister.create(Registries.ITEM, FarmersMiracle.MOD_ID);
        ModItems.WHEAT_ORB = items.register("wheat_orb", ModItems::createWheatOrb);
        ModItems.PUMPKIN_ORB = items.register("pumpkin_orb", ModItems::createPumpkinOrb);
        ModItems.MELON_ORB = items.register("melon_orb", ModItems::createMelonOrb);
        ModItems.ROOT_ICON = items.register("root_icon", ModItems::createRootIcon);
        ModItems.PILGRIMAGE_ICON = items.register("pilgrimage_icon", ModItems::createPilgrimageIcon);
        items.register(modBus);

        DeferredRegister<MobEffect> effects = DeferredRegister.create(Registries.MOB_EFFECT, FarmersMiracle.MOD_ID);
        var grainGrowth = effects.register("grain_growth", GrainGrowthEffect::new);
        var grainSpread = effects.register("grain_spread", GrainSpreadEffect::new);
        effects.register(modBus);
        ModEffects.GRAIN_GROWTH = grainGrowth::get;
        ModEffects.GRAIN_SPREAD = grainSpread::get;

        DeferredRegister<ParticleType<?>> particles = DeferredRegister.create(Registries.PARTICLE_TYPE, FarmersMiracle.MOD_ID);
        ModParticles.GRAIN_GROWTH = particles.register("grain_growth", () -> new SimpleParticleType(false));
        particles.register(modBus);

        DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersMiracle.MOD_ID);
        ModCreativeTabs.FARMERS_MIRACLE_TAB = tabs.register("farmersmiracle", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.farmersmiracle.farmersmiracle"))
                .icon(() -> new ItemStack(ModItems.WHEAT_ORB.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.WHEAT_ORB.get());
                    output.accept(ModItems.PUMPKIN_ORB.get());
                    output.accept(ModItems.MELON_ORB.get());
                })
                .build());
        tabs.register(modBus);
    }

    private void registerEvents() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener((ServerTickEvent.Post event) ->
                BuffedPlayerCache.updateCache(event.getServer()));
        bus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer sp) {
                FarmersMiracleEvents.onPlayerJoin(sp);
            }
        });
    }

    private static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.GRAIN_GROWTH.get(), GrainGrowthParticle.Provider::new);
    }
}
