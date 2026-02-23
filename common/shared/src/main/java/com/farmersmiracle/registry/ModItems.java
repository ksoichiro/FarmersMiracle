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

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {
    public static Supplier<Item> WHEAT_ORB;
    public static Supplier<Item> PUMPKIN_ORB;
    public static Supplier<Item> MELON_ORB;
    // Icon-only items for advancement display
    public static Supplier<Item> ROOT_ICON;
    public static Supplier<Item> PILGRIMAGE_ICON;

    public static Item createWheatOrb() {
        return new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public static Item createPumpkinOrb() {
        return new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public static Item createMelonOrb() {
        return new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public static Item createRootIcon() {
        return new Item(new Item.Properties());
    }

    public static Item createPilgrimageIcon() {
        return new Item(new Item.Properties());
    }
}
