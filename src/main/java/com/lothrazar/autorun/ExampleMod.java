package com.lothrazar.autorun;

import com.lothrazar.autorun.setup.ClientProxy;
import com.lothrazar.autorun.setup.IProxy;
import com.lothrazar.autorun.setup.ServerProxy;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
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
      this.move(p);
      //      Minecraft.getInstance().gameSettings.keyBindForward.isPressed()
    }
  }

  private void move(PlayerEntity p) {
    p.moveForward = 1.68F;
    //p.travel(new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward));
    Vec3d p_213352_1_ = new Vec3d(p.moveStrafing, p.moveVertical, p.moveForward);
    // ok go
    IAttributeInstance gravity = p.getAttribute(LivingEntity.ENTITY_GRAVITY);
    double d0 = gravity.getValue();
    World world = p.world;
    BlockPos blockpos = new BlockPos(p.posX, p.getBoundingBox().minY - 1.0D, p.posZ);
    float f5 = p.world.getBlockState(blockpos).getSlipperiness(world, blockpos, p);
    float f7 = p.onGround ? f5 * 0.91F : 0.91F;
    p.moveRelative(func_213335_r(p, f5), p_213352_1_);
    p.setMotion(func_213362_f(p, p.getMotion()));
    p.move(MoverType.SELF, p.getMotion());
    Vec3d vec3d5 = p.getMotion();
    //    if ((p.collidedHorizontally || p.isJumping) && p.isOnLadder()) {
    //      vec3d5 = new Vec3d(vec3d5.x, 0.2D, vec3d5.z);
    //    }
    double d10 = vec3d5.y;
    if (p.isPotionActive(Effects.LEVITATION)) {
      d10 += (0.05D * (p.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1) - vec3d5.y) * 0.2D;
      p.fallDistance = 0.0F;
    }
    else if (p.world.isRemote && !p.world.isBlockLoaded(blockpos)) {
      if (p.posY > 0.0D) {
        d10 = -0.1D;
      }
      else {
        d10 = 0.0D;
      }
    }
    else if (!p.hasNoGravity()) {
      d10 -= d0;
    }
    p.setMotion(vec3d5.x * f7, d10 * 0.98F, vec3d5.z * f7);
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
