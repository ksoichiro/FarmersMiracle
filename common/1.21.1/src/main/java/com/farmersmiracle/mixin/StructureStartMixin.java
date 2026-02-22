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
package com.farmersmiracle.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin {
    @Inject(method = "placeInChunk", at = @At("RETURN"))
    private void farmersmiracle$onPlaceInChunk(WorldGenLevel level, StructureManager structureManager,
                                                ChunkGenerator chunkGenerator, RandomSource random,
                                                BoundingBox chunkBB, ChunkPos chunkPos,
                                                CallbackInfo ci) {
        StructureStart self = (StructureStart) (Object) this;
        ResourceKey<Structure> structureKey = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getResourceKey(self.getStructure())
                .orElse(null);
        if (structureKey == null) {
            return;
        }
        ResourceLocation structureId = structureKey.location();
        if (!"farmersmiracle".equals(structureId.getNamespace())) {
            return;
        }

        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> lootTableKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath("farmersmiracle", "chests/" + structureId.getPath()));

        long seed = random.nextLong();

        for (StructurePiece piece : self.getPieces()) {
            BoundingBox pieceBB = piece.getBoundingBox();
            if (!pieceBB.intersects(chunkBB)) {
                continue;
            }
            int minX = Math.max(pieceBB.minX(), chunkBB.minX());
            int minY = Math.max(pieceBB.minY(), chunkBB.minY());
            int minZ = Math.max(pieceBB.minZ(), chunkBB.minZ());
            int maxX = Math.min(pieceBB.maxX(), chunkBB.maxX());
            int maxY = Math.min(pieceBB.maxY(), chunkBB.maxY());
            int maxZ = Math.min(pieceBB.maxZ(), chunkBB.maxZ());
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
                            chest.setLootTable(lootTableKey, seed);
                        }
                    }
                }
            }
        }
    }
}
