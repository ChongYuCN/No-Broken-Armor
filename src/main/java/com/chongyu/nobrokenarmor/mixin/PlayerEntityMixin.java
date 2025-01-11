package com.chongyu.nobrokenarmor.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    @Final
    PlayerInventory inventory;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack", cancellable = true)
    public void getEquippedStack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            String key = Registries.ITEM.getId(inventory.armor.get(slot.getEntitySlotId()).getItem()).toString();
            int damage = this.inventory.armor.get(slot.getEntitySlotId()).getMaxDamage() - this.inventory.armor.get(slot.getEntitySlotId()).getDamage();
            cir.setReturnValue(damage <=1 && !key.contains("modern_industrialization:quantum_")? ItemStack.EMPTY : this.inventory.armor.get(slot.getEntitySlotId()));
        }
    }
}
