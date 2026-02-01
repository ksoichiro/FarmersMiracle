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
package com.farmersmiracle;

import com.farmersmiracle.events.FarmersMiracleEvents;
import com.farmersmiracle.registry.ModCreativeTabs;
import com.farmersmiracle.registry.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmersMiracle {
    public static final String MOD_ID = "farmersmiracle";
    public static final Logger LOGGER = LoggerFactory.getLogger(FarmersMiracle.class);

    public static void init() {
        ModItems.register();
        ModCreativeTabs.register();

        FarmersMiracleEvents.register();

        LOGGER.info("Farmer's Miracle initialized");
    }
}
