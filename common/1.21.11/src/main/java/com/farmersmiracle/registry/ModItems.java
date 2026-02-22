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
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/**
 * MC 1.21.3 version: Item.Properties requires setId() since 1.21.2+.
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(FarmersMiracle.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> WHEAT_ORB = ITEMS.register("wheat_orb",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
                    .setId(itemKey("wheat_orb"))));

    public static final RegistrySupplier<Item> PUMPKIN_ORB = ITEMS.register("pumpkin_orb",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
                    .setId(itemKey("pumpkin_orb"))));

    public static final RegistrySupplier<Item> MELON_ORB = ITEMS.register("melon_orb",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)
                    .setId(itemKey("melon_orb"))));

    // Icon-only items for advancement display
    public static final RegistrySupplier<Item> ROOT_ICON = ITEMS.register("root_icon",
            () -> new Item(new Item.Properties()
                    .setId(itemKey("root_icon"))));

    public static final RegistrySupplier<Item> PILGRIMAGE_ICON = ITEMS.register("pilgrimage_icon",
            () -> new Item(new Item.Properties()
                    .setId(itemKey("pilgrimage_icon"))));

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(FarmersMiracle.MOD_ID, name));
    }

    public static void register() {
        ITEMS.register();
        FarmersMiracle.LOGGER.debug("Registered ModItems");
    }
}
