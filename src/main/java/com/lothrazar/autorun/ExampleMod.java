package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("autorun")
public class ExampleMod {

  private static final String NBT = "isautorunning";
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public ExampleMod() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    proxy.init();
  }

  @SubscribeEvent
  public void onTick(PlayerTickEvent event) {
    PlayerEntity p = event.player;
    if (p.getPersistentData().getBoolean(NBT)) {
      p.setSprinting(false);
      p.moveForward = 0.85F;
      p.travel(new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward));
      //    this.move(p); 
      //      Minecraft.getInstance().gameSettings.keyBindForward.isPressed()
    }
  }

  private void move(PlayerEntity p) {
    Vec3d p_213352_1_ = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
    // ok go
    IAttributeInstance gravity = p.getAttribute(LivingEntity.ENTITY_GRAVITY);
    double d0 = gravity.getValue();
    World world = p.world;
    double d1 = p.posY;
    boolean flag = p.getMotion().y <= 0.0D;
    float f = p.isSprinting() ? 0.9F : getWaterSlowDown();
    float f1 = 0.02F;
    float f2 = EnchantmentHelper.getDepthStriderModifier(p);
    if (f2 > 3.0F) {
      f2 = 3.0F;
    }
    if (!p.onGround) {
      f2 *= 0.5F;
    }
    if (f2 > 0.0F) {
      f += (0.54600006F - f) * f2 / 3.0F;
      f1 += (p.getAIMoveSpeed() - f1) * f2 / 3.0F;
    }
    if (p.isPotionActive(Effects.DOLPHINS_GRACE)) {
      f = 0.96F;
    }
    f1 *= (float) p.getAttribute(LivingEntity.SWIM_SPEED).getValue();
    p.moveRelative(f1, p_213352_1_);
    p.move(MoverType.SELF, p.getMotion());
    Vec3d vec3d1 = p.getMotion();
    if (p.collidedHorizontally && p.isOnLadder()) {
      vec3d1 = new Vec3d(vec3d1.x, 0.2D, vec3d1.z);
    }
    p.setMotion(vec3d1.mul(f, 0.8F, f));
    if (!p.hasNoGravity() && !p.isSprinting()) {
      Vec3d vec3d2 = p.getMotion();
      double d2;
      if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) {
        d2 = -0.003D;
      }
      else {
        d2 = vec3d2.y - d0 / 16.0D;
      }
      p.setMotion(vec3d2.x, d2, vec3d2.z);
    }
    Vec3d vec3d6 = p.getMotion();
    if (p.collidedHorizontally && p.isOffsetPositionInLiquid(vec3d6.x, vec3d6.y + 0.6F - p.posY + d1, vec3d6.z)) {
      p.setMotion(vec3d6.x, 0.3F, vec3d6.z);
    }
  }

  protected float getWaterSlowDown() {
    return 0.8F;
  }

  private float func_213335_r(PlayerEntity p, float p_213335_1_) {
    return p.onGround ? p.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : p.jumpMovementFactor;
  }

  private Vec3d func_213362_f(PlayerEntity p, Vec3d p_213362_1_) {
    if (p.isOnLadder()) {
      p.fallDistance = 0.0F;
      float f = 0.15F;
      double d0 = MathHelper.clamp(p_213362_1_.x, -0.15F, 0.15F);
      double d1 = MathHelper.clamp(p_213362_1_.z, -0.15F, 0.15F);
      double d2 = Math.max(p_213362_1_.y, -0.15F);
      if (d2 < 0.0D && p.getBlockState().getBlock() != Blocks.SCAFFOLDING && p.isSneaking() && p instanceof PlayerEntity) {
        d2 = 0.0D;
      }
      p_213362_1_ = new Vec3d(d0, d2, d1);
    }
    return p_213362_1_;
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (Minecraft.getInstance().gameSettings.keyBindForward.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindBack.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindLeft.isKeyDown() ||
        Minecraft.getInstance().gameSettings.keyBindRight.isKeyDown()) {
      setAutorunState(proxy.getClientPlayer(), false);
    }
    if (ClientProxy.key != null && ClientProxy.key.isPressed()) {
      PlayerEntity player = proxy.getClientPlayer();
      boolean value = !player.getPersistentData().getBoolean(NBT);
      setAutorunState(player, value);
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static void setAutorunState(PlayerEntity player, boolean value) {
    player.getPersistentData().putBoolean(NBT, value);
    player.sendStatusMessage(new TranslationTextComponent("autorun." + value), true);
  }
}
