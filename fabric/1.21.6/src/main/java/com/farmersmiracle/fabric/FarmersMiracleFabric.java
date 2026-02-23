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
package com.farmersmiracle.fabric;

import com.farmersmiracle.FarmersMiracle;
import com.farmersmiracle.effects.BuffedPlayerCache;
import com.farmersmiracle.effects.GrainGrowthEffect;
import com.farmersmiracle.effects.GrainSpreadEffect;
import com.farmersmiracle.events.FarmersMiracleEvents;
import com.farmersmiracle.registry.ModCreativeTabs;
import com.farmersmiracle.registry.ModEffects;
import com.farmersmiracle.registry.ModItems;
import com.farmersmiracle.registry.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FarmersMiracleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        registerAll();
        registerEvents();
        FarmersMiracle.init();
    }

    private void registerAll() {
        Item wheatOrb = ModItems.createWheatOrb();
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "wheat_orb"), wheatOrb);
        ModItems.WHEAT_ORB = () -> wheatOrb;

        Item pumpkinOrb = ModItems.createPumpkinOrb();
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "pumpkin_orb"), pumpkinOrb);
        ModItems.PUMPKIN_ORB = () -> pumpkinOrb;

        Item melonOrb = ModItems.createMelonOrb();
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "melon_orb"), melonOrb);
        ModItems.MELON_ORB = () -> melonOrb;

        Item rootIcon = ModItems.createRootIcon();
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "root_icon"), rootIcon);
        ModItems.ROOT_ICON = () -> rootIcon;

        Item pilgrimageIcon = ModItems.createPilgrimageIcon();
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "pilgrimage_icon"), pilgrimageIcon);
        ModItems.PILGRIMAGE_ICON = () -> pilgrimageIcon;

        MobEffect grainGrowth = new GrainGrowthEffect();
        Registry.register(BuiltInRegistries.MOB_EFFECT,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "grain_growth"), grainGrowth);
        ModEffects.GRAIN_GROWTH = () -> grainGrowth;

        MobEffect grainSpread = new GrainSpreadEffect();
        Registry.register(BuiltInRegistries.MOB_EFFECT,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "grain_spread"), grainSpread);
        ModEffects.GRAIN_SPREAD = () -> grainSpread;

        SimpleParticleType grainGrowthParticle = new SimpleParticleType(false);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "grain_growth"), grainGrowthParticle);
        ModParticles.GRAIN_GROWTH = () -> grainGrowthParticle;

        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.farmersmiracle.farmersmiracle"))
                .icon(() -> new ItemStack(ModItems.WHEAT_ORB.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.WHEAT_ORB.get());
                    output.accept(ModItems.PUMPKIN_ORB.get());
                    output.accept(ModItems.MELON_ORB.get());
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath(FarmersMiracle.MOD_ID, "farmersmiracle"), tab);
        ModCreativeTabs.FARMERS_MIRACLE_TAB = () -> tab;
    }

    private void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(BuffedPlayerCache::updateCache);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                FarmersMiracleEvents.onPlayerJoin(handler.player));
    }
}
