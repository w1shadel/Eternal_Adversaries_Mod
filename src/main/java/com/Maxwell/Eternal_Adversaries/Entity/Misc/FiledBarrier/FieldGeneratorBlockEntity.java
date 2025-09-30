package com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier;

import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities.FieldGeneratorSavedData;
import com.Maxwell.Eternal_Adversaries.Entity.Misc.FiledBarrier.capabilities.ModCapabilities;
import com.Maxwell.Eternal_Adversaries.Register.ModBlockEntities;
import com.Maxwell.Eternal_Adversaries.Register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldGeneratorBlockEntity extends BlockEntity {
    private boolean isWorldGenInitialized = false;
    private boolean isActive = false; // デフォルトは「無効」。範囲設定後に有効になる
    @Nullable
    private BlockPos pos1;
    @Nullable
    private BlockPos pos2;
    public static void tick(Level level, BlockPos pos, BlockState state, FieldGeneratorBlockEntity entity) {
        // サーバーサイドでのみ、かつワールド生成後の初期化がまだの場合のみ実行
        if (!level.isClientSide && !entity.isWorldGenInitialized) {
            // 少し遅延させることで、チャンク全体の読み込みを待つ（安定性のため）
            if (level.getGameTime() % 20 == 0) {
                entity.tryWorldGenSetup();
            }
        }
    }
    private void tryWorldGenSetup() {
        if (this.level == null) return;

        // すでにプレイヤーによって範囲が設定されている場合は、何もしない
        if (this.isActive) {
            this.isWorldGenInitialized = true;
            setChanged();
            return;
        }

        System.out.println("Field Generator at " + this.worldPosition + " is attempting worldgen setup...");

        // ★★★ ここで、ダンジョンを構成する「目印」となるブロックを指定します ★★★
        // 例として、あなたのMODの特殊なレンガブロックを指定
        Block dungeonMarkerBlock = ModBlocks.FILED_BARRIER_ICON.get();

        // 1. スキャンを実行し、ダンジョンの範囲(AABB)を見つける
        AABB dungeonBounds = findDungeonBounds(dungeonMarkerBlock);

        // 2. 範囲が見つかった場合
        if (dungeonBounds != null) {
            System.out.println("Dungeon bounds found! Activating protection field.");
            // 3. 範囲を設定してフィールドを有効化
            this.setRange(
                    new BlockPos((int)dungeonBounds.minX, (int)dungeonBounds.minY, (int)dungeonBounds.minZ),
                    new BlockPos((int)dungeonBounds.maxX - 1, (int)dungeonBounds.maxY - 1, (int)dungeonBounds.maxZ - 1)
            );
        } else {
            System.out.println("No dungeon structure found nearby. Generator remains inactive.");
        }

        // 4. この初期化処理は一度しか実行しないようにフラグを立てる
        this.isWorldGenInitialized = true;
        setChanged();
    }
    @Nullable
    private AABB findDungeonBounds(Block markerBlock) {
        if (this.level == null) return null;

        List<BlockPos> foundParts = new ArrayList<>();
        List<BlockPos> toScan = new ArrayList<>();
        toScan.add(this.worldPosition);
        Set<BlockPos> scanned = new HashSet<>();
        scanned.add(this.worldPosition);

        int scanLimit = 5000; // 無限ループを防ぐためのスキャン回数上限

        // 幅優先探索で、繋がっているダンジョンブロックを全て見つける
        while (!toScan.isEmpty() && scanLimit > 0) {
            scanLimit--;
            BlockPos currentPos = toScan.remove(0);

            // 目印ブロックか、このジェネレーター自身なら、構造物の一部とみなす
            if (level.getBlockState(currentPos).is(markerBlock) || currentPos.equals(this.worldPosition)) {
                foundParts.add(currentPos);

                // 周囲6方向のブロックをスキャン対象に追加
                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(dir);
                    if (scanned.add(neighborPos)) {
                        toScan.add(neighborPos);
                    }
                }
            }
        }


        if (foundParts.size() < 10) {
            return null;
        }

        // ▼▼▼ ここからが encapsulatingFullBlocks の代替処理です ▼▼▼

        // 1. 最小座標と最大座標を初期化する
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        // 2. 見つかった全てのブロックをループし、最小/最大の座標を更新していく
        for (BlockPos pos : foundParts) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX());
            maxY = Math.max(maxY, pos.getY());
            maxZ = Math.max(maxZ, pos.getZ());
        }

        // 3. 最小/最大の座標から、ブロック全体を完全に覆うAABBを作成して返す
        // AABBの最大点は、ブロックの最大座標に+1した位置になることに注意
        return new AABB(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);
    }
    public FieldGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FILED_BARRIER.get(), pPos, pBlockState); // あなたのBEタイプに
    }

    // --- ゲッター ---
    public boolean isActive() {
        return this.isActive;
    }

    public AABB getProtectionBox() {
        if (this.pos1 != null && this.pos2 != null) {
            return new AABB(this.pos1, this.pos2).expandTowards(1, 1, 1);
        }
        return AABB.ofSize(Vec3.ZERO, 0, 0, 0);
    }

    // --- メインロジック ---

    /**
     * ワンドから範囲情報を受け取り、フィールドを有効化する
     */
    public void setRange(BlockPos pos1, BlockPos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        // ▼▼▼ levelがnullでないことを確認してからsetActiveを呼ぶ ▼▼▼
        if (this.level != null) {
            this.setActive(true);
        }
    }
    /**
     * レッドストーン信号などで、フィールドの有効/無効を切り替える
     */
    public void setActive(boolean active) {
        if (this.level == null || this.level.isClientSide) return;
        if (this.isActive != active && this.level instanceof ServerLevel serverLevel) {
            this.isActive = active;

            // ★★★ ワールドのSavedDataを取得し、リストを更新 ★★★
            FieldGeneratorSavedData savedData = FieldGeneratorSavedData.get(serverLevel);
            if (this.isActive) {
                savedData.addGenerator(this.worldPosition);
            } else {
                savedData.removeGenerator(this.worldPosition);
            }

            setChangedAndSync();
        }
    }

    // --- ライフサイクルメソッド ---
    // onLoadとsetRemovedは、データが失われるのを防ぐために残しておくのが安全
    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level instanceof ServerLevel serverLevel && this.isActive) {
            FieldGeneratorSavedData.get(serverLevel).addGenerator(this.worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        if (this.level instanceof ServerLevel serverLevel) {
            FieldGeneratorSavedData.get(serverLevel).removeGenerator(this.worldPosition);
        }
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putBoolean("is_active", this.isActive);
        if (this.pos1 != null) pTag.put("pos1", NbtUtils.writeBlockPos(this.pos1));
        if (this.pos2 != null) pTag.put("pos2", NbtUtils.writeBlockPos(this.pos2));
        pTag.putBoolean("worldgen_init", this.isWorldGenInitialized);
        super.saveAdditional(pTag);
    }
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.isActive = pTag.getBoolean("is_active");
        this.pos1 = pTag.contains("pos1") ? NbtUtils.readBlockPos(pTag.getCompound("pos1")) : null;
        this.pos2 = pTag.contains("pos2") ? NbtUtils.readBlockPos(pTag.getCompound("pos2")) : null;
        this.isWorldGenInitialized = pTag.getBoolean("worldgen_init");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void setChangedAndSync() {
        setChanged();
        if(level != null && !level.isClientSide) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
}