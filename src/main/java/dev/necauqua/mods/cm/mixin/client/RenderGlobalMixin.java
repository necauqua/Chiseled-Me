/*
 * Copyright (c) 2017-2021 Anton Bulakh <self@necauqua.dev>
 * Licensed under MIT, see the LICENSE file for details.
 */

package dev.necauqua.mods.cm.mixin.client;

import dev.necauqua.mods.cm.api.IWorldPreciseEvents;
import dev.necauqua.mods.cm.api.IWorldPreciseSounds;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

import static dev.necauqua.mods.cm.size.EntitySizeInteractions.*;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin implements IWorldPreciseEvents {

    private double $cm$size = 1.0;
    private BlockPos $cm$blockPos;
    private Vec3d $cm$pos;

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPos, int data, double size, Vec3d pos) {
        $cm$size = size;
        $cm$blockPos = blockPos;
        $cm$pos = pos;
        playEvent(null, type, blockPos, data);
        $cm$blockPos = null;
    }

    @Redirect(method = "playEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;playSound(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"))
    void playSound(WorldClient self, BlockPos blockPos, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
        Vec3d pos = $cm$pos;
        if (pos == null) {
            self.playSound(blockPos, soundIn, category, volume, pitch, distanceDelay);
            return;
        }
        if ($cm$size != 1.0) {
            ((IWorldPreciseSounds) self).playSound(pos, soundIn, category, volume, pitch, distanceDelay, $cm$size);
        } else {
            self.playSound(pos.x, pos.y, pos.z, soundIn, category, volume, pitch, distanceDelay);
        }
    }

    @ModifyConstant(method = "spawnParticle0(IZZDDDDDD[I)Lnet/minecraft/client/particle/Particle;", constant = @Constant(doubleValue = 1024.0))
    double spawnParticle0(double constant, int particleId, boolean ignoreRange, boolean minParticles, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
        return constant * extractSize(particleId, parameters) * getViewerSize();
    }

    @Redirect(method = "playEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;spawnParticle0(IZDDDDDD[I)Lnet/minecraft/client/particle/Particle;"))
    Particle spawnParticle0(RenderGlobal renderGlobal, int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters) {
        BlockPos blockPos = $cm$blockPos;
        Vec3d pos = $cm$pos;
        if (blockPos == null || pos == null) {
            return spawnParticle0(particleID, ignoreRange, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        }
        return spawnParticle0(
                particleID,
                ignoreRange,
                pos.x + (xCoord - blockPos.getX()) * $cm$size,
                pos.y + (yCoord - blockPos.getY()) * $cm$size,
                pos.z + (zCoord - blockPos.getZ()) * $cm$size,
                xSpeed, ySpeed, zSpeed,
                appendSize(parameters, $cm$size)
        );
    }

    @Redirect(method = "playEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V"))
    void spawnParticle(RenderGlobal self, EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters) {
        BlockPos blockPos = $cm$blockPos;
        Vec3d pos = $cm$pos;
        if (blockPos == null || pos == null) {
            spawnParticle(particleIn, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            return;
        }
        spawnParticle(
                particleIn,
                pos.x + (xCoord - blockPos.getX()) * $cm$size,
                pos.y + (yCoord - blockPos.getY()) * $cm$size,
                pos.z + (zCoord - blockPos.getZ()) * $cm$size,
                xSpeed, ySpeed, zSpeed,
                appendSize(parameters, $cm$size)
        );
    }

    @Redirect(method = "playEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V"))
    void spawnParticle(WorldClient self, EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters) {
        BlockPos blockPos = $cm$blockPos;
        Vec3d pos = $cm$pos;
        if (blockPos == null || pos == null) {
            self.spawnParticle(particleType, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            return;
        }
        self.spawnParticle(
                particleType,
                pos.x + (xCoord - blockPos.getX()) * $cm$size,
                pos.y + (yCoord - blockPos.getY()) * $cm$size,
                pos.z + (zCoord - blockPos.getZ()) * $cm$size,
                xSpeed, ySpeed, zSpeed,
                appendSize(parameters, $cm$size)
        );
    }

    @Redirect(method = "playEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;ZDDDDDD[I)V"))
    void spawnParticle(WorldClient self, EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters) {
        BlockPos blockPos = $cm$blockPos;
        Vec3d pos = $cm$pos;
        if (blockPos == null || pos == null) {
            self.spawnParticle(particleType, ignoreRange, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            return;
        }
        self.spawnParticle(
                particleType,
                ignoreRange,
                pos.x + (xCoord - blockPos.getX()) * $cm$size,
                pos.y + (yCoord - blockPos.getY()) * $cm$size,
                pos.z + (zCoord - blockPos.getZ()) * $cm$size,
                xSpeed, ySpeed, zSpeed,
                appendSize(parameters, $cm$size)
        );
    }

    @Shadow
    public abstract void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data);

    @Shadow
    protected abstract void spawnParticle(EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters);

    @Shadow
    @Nullable
    protected abstract Particle spawnParticle0(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters);
}
