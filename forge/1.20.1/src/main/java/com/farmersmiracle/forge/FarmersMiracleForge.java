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
package com.farmersmiracle.forge;

import com.farmersmiracle.FarmersMiracle;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod(FarmersMiracle.MOD_ID)
public class FarmersMiracleForge {
    public FarmersMiracleForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        registerAll(modBus);
        registerEvents();
        FarmersMiracle.init();
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
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener((TickEvent.ServerTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) {
                var server = ServerLifecycleHooks.getCurrentServer();
                if (server != null) {
                    BuffedPlayerCache.updateCache(server);
                }
            }
        });
        bus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer sp) {
                FarmersMiracleEvents.onPlayerJoin(sp);
            }
        });
        bus.addListener((PlayerEvent.PlayerRespawnEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer sp) {
                FarmersMiracleEvents.onPlayerJoin(sp);
            }
        });
    }
}
