package ru.tpsd.eatinganimationmod.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

	// Without an entity the eat/eating predicates stay at 0, so food is drawn in GUIs with its base model.
	@ModifyArg(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"), index = 2)
	private @Nullable LivingEntity hideFoodAnimation(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {
		if (stack.contains(DataComponentTypes.FOOD) && stack.get(DataComponentTypes.CUSTOM_MODEL_DATA) == null) {
			return null;
		}
		return entity;
	}
}
