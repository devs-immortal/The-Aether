package com.legacy.aether;

import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.container.ContainerFactoryAether;
import com.legacy.aether.entities.EntityTypesAether;
import com.legacy.aether.entities.util.AetherMoaTypes;
import com.legacy.aether.item.ItemsAether;
import com.legacy.aether.network.ServerNetworkAether;
import com.legacy.aether.particle.AetherParticleTypes;
import com.legacy.aether.registry.AetherAPIRegistry;
import com.legacy.aether.sounds.SoundsAether;
//import com.legacy.aether.world.WorldAether;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aether implements ModInitializer {

    private static final Logger LOGGER = LogManager.getFormatterLogger("Aether Legacy");

    public static void log(Object object) {
        LOGGER.info(object.toString());
    }

    public static Identifier locate(String location) {
        return new Identifier("aether_legacy", location);
    }

    @Override
    public void onInitialize() {
        AetherParticleTypes.register();
        SoundsAether.registerSounds();
        ItemsAether.registerItems();

        BlocksAether.register();
        EntityTypesAether.register();

        AetherMoaTypes.initialization();
        //WorldAether.registerWorld();

        ContainerFactoryAether.registerContainers();
        ServerNetworkAether.initializePacketHandler();
        AetherEventHandler.register();
        AetherAPIRegistry.register();
    }

}