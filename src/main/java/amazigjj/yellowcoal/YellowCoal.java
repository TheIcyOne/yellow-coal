package amazigjj.yellowcoal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid="yellowcoal", name = "Yellow Coal")
public class YellowCoal {

    static Coal coal = new Coal();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void clientInit(FMLInitializationEvent event){
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new CoalColour(), coal);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        OreDictionary.registerOre("yellorium", new ItemStack(coal, 1, 16));
        OreDictionary.registerOre("ingotYellorium", new ItemStack(coal, 1, 16));
    }

    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event){
        event.getRegistry().register(coal);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event){
        coal.registerModel();
    }

    public static class Coal extends Item {

        public Coal(){
            this.setRegistryName("coal");
            this.setUnlocalizedName("coal");
            this.setHasSubtypes(true);
            this.setCreativeTab(CreativeTabs.MATERIALS);
        }

        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            if(this.getCreativeTab().equals(tab)) for(int i = 0; i < 18; i++) items.add(new ItemStack(this, 1, i));
        }

        @SideOnly(Side.CLIENT)
        public void registerModel(){
            if(CoalConfig.coalDepth) for(int i = 0; i < 17; i++) ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("yellowcoal:coalwithdepth", "inventory"));
            else for(int i = 0; i < 17; i++) ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("yellowcoal:coal", "inventory"));
            ModelLoader.setCustomModelResourceLocation(this, 17, new ModelResourceLocation("yellowcoal:steelcoal", "inventory"));
        }

        @Override
        public int getItemBurnTime(ItemStack itemStack) {
            if(itemStack.getMetadata()!=16) return 6400;
            return 25600;
        }
    }

    public static class CoalColour implements IItemColor {

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            if(stack.getMetadata()<16){
                return EnumDyeColor.byDyeDamage(stack.getMetadata()).getColorValue();
            }else{
                return 0xFFFF00;
            }
        }
    }

    @Config(modid="yellowcoal")
    public static class CoalConfig{
        @Config.Name("Coal Depth")
        @Config.Comment("Give the coal more depth")
        @Config.RequiresMcRestart
        public static boolean coalDepth = false;
    }
    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event){
        if("yellowcoal".equals(event.getModID())){
            ConfigManager.sync("yellowcoal", Config.Type.INSTANCE);
        }
    }

}
