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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * World-level SavedData that stores per-player buff information.
 * Works across both Fabric and NeoForge without platform-specific APIs.
 *
 * MC 1.21.5: SavedData uses SavedDataType with Codec instead of Factory + manual save/load.
 */
public class PlayerBuffData extends SavedData {
    private static final String DATA_NAME = "farmersmiracle_player_buffs";

    private final Map<UUID, PlayerBuff> playerBuffs = new HashMap<>();

    private static final Codec<PlayerBuff> PLAYER_BUFF_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("grain_growth_level").forGetter(b -> b.growthLevel),
                    Codec.BOOL.fieldOf("grain_range_expanded").forGetter(b -> b.rangeExpanded)
            ).apply(instance, PlayerBuff::new)
    );

    private static final Codec<PlayerBuffData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, PLAYER_BUFF_CODEC)
                            .fieldOf("players")
                            .forGetter(data -> {
                                Map<String, PlayerBuff> map = new HashMap<>();
                                data.playerBuffs.forEach((uuid, buff) -> map.put(uuid.toString(), buff));
                                return map;
                            })
            ).apply(instance, playersMap -> {
                PlayerBuffData data = new PlayerBuffData();
                playersMap.forEach((key, buff) -> data.playerBuffs.put(UUID.fromString(key), buff));
                return data;
            })
    );

    public static final SavedDataType<PlayerBuffData> TYPE = new SavedDataType<>(
            DATA_NAME,
            PlayerBuffData::new,
            CODEC,
            null
    );

    public static PlayerBuffData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
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
