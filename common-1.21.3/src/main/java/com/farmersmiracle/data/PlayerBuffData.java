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
package com.farmersmiracle.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * World-level SavedData that stores per-player buff information.
 * Works across both Fabric and NeoForge without platform-specific APIs.
 */
public class PlayerBuffData extends SavedData {
    private static final String DATA_NAME = "farmersmiracle_player_buffs";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_GROWTH_LEVEL = "grain_growth_level";
    private static final String TAG_RANGE_EXPANDED = "grain_range_expanded";

    private final Map<UUID, PlayerBuff> playerBuffs = new HashMap<>();

    public static PlayerBuffData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(PlayerBuffData::new, PlayerBuffData::load, null),
                DATA_NAME
        );
    }

    public static PlayerBuffData load(CompoundTag tag, HolderLookup.Provider registries) {
        PlayerBuffData data = new PlayerBuffData();
        CompoundTag playersTag = tag.getCompound(TAG_PLAYERS);
        for (String key : playersTag.getAllKeys()) {
            UUID uuid = UUID.fromString(key);
            CompoundTag playerTag = playersTag.getCompound(key);
            PlayerBuff buff = new PlayerBuff(
                    playerTag.getInt(TAG_GROWTH_LEVEL),
                    playerTag.getBoolean(TAG_RANGE_EXPANDED)
            );
            data.playerBuffs.put(uuid, buff);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag playersTag = new CompoundTag();
        for (Map.Entry<UUID, PlayerBuff> entry : playerBuffs.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putInt(TAG_GROWTH_LEVEL, entry.getValue().growthLevel);
            playerTag.putBoolean(TAG_RANGE_EXPANDED, entry.getValue().rangeExpanded);
            playersTag.put(entry.getKey().toString(), playerTag);
        }
        tag.put(TAG_PLAYERS, playersTag);
        return tag;
    }

    public int getGrowthLevel(ServerPlayer player) {
        PlayerBuff buff = playerBuffs.get(player.getUUID());
        return buff != null ? buff.growthLevel : 0;
    }

    public void setGrowthLevel(ServerPlayer player, int level) {
        PlayerBuff buff = playerBuffs.computeIfAbsent(player.getUUID(), k -> new PlayerBuff(0, false));
        buff.growthLevel = Math.min(level, 4);
        setDirty();
    }

    public boolean isRangeExpanded(ServerPlayer player) {
        PlayerBuff buff = playerBuffs.get(player.getUUID());
        return buff != null && buff.rangeExpanded;
    }

    public void setRangeExpanded(ServerPlayer player, boolean expanded) {
        PlayerBuff buff = playerBuffs.computeIfAbsent(player.getUUID(), k -> new PlayerBuff(0, false));
        buff.rangeExpanded = expanded;
        setDirty();
    }

    public int getEffectiveRadius(ServerPlayer player) {
        return isRangeExpanded(player) ? 24 : 16;
    }

    private static class PlayerBuff {
        int growthLevel;
        boolean rangeExpanded;

        PlayerBuff(int growthLevel, boolean rangeExpanded) {
            this.growthLevel = growthLevel;
            this.rangeExpanded = rangeExpanded;
        }
    }
}
