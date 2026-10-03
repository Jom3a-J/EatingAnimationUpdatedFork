package ru.tpsd.eatinganimationmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Item model property {@code eatinganimationid:eat}: eating progress used to pick the bite frame.
 */
public record EatProperty() implements RangeSelectItemModelProperty {

    public static final MapCodec<EatProperty> CODEC = MapCodec.unit(new EatProperty());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity livingEntity = owner == null ? null : owner.asLivingEntity();
        if (livingEntity == null || livingEntity.getUseItem() != itemStack) {
            return 0.0F;
        }
        if (livingEntity instanceof RemotePlayer) {
            if (itemStack.getUseDuration(livingEntity) > 16) {
                return ((float) livingEntity.getTicksUsingItem() / (float) itemStack.getUseDuration(livingEntity)) % 1;
            }
            else {
                return ((float) livingEntity.getTicksUsingItem() / 32.0f) % 0.5F;
            }
        }
        return (itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 30.0F;
    }

    @Override
    public MapCodec<EatProperty> type() {
        return CODEC;
    }
}
