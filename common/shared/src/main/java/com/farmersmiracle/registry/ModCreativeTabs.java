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
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(FarmersMiracle.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> FARMERS_MIRACLE_TAB = TABS.register(
            "farmersmiracle",
            () -> CreativeTabRegistry.create(builder ->
                    builder.title(Component.translatable("itemGroup.farmersmiracle.farmersmiracle"))
                            .icon(() -> new ItemStack(ModItems.WHEAT_ORB.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.WHEAT_ORB.get());
                                output.accept(ModItems.PUMPKIN_ORB.get());
                                output.accept(ModItems.MELON_ORB.get());
                            })
            )
    );

    public static void register() {
        TABS.register();
        FarmersMiracle.LOGGER.debug("Registered ModCreativeTabs");
    }
}
