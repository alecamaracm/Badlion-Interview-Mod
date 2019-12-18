package com.alecamar.badlionmod;


import com.alecamar.badlionmod.BadlionMod.UIState;
import com.alecamar.badlionmod.mainoverlay.IndividualOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "badlionmod", bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
    

	
    @SubscribeEvent
	public static void onTickEvent(TickEvent.PlayerTickEvent evt) {
    	//badlionmodxx.LOGGER.debug("dasd");
	}

	

    @SubscribeEvent
     public static void onRenderGui(RenderGameOverlayEvent.Post event)
     {
    	if (event.getType() != ElementType.EXPERIENCE) return;
    	
    	for(IndividualOverlay gui: BadlionMod.guis.values())
    	{
    		//Only render UIs here if user is not editing them
    		if(BadlionMod.currentState==UIState.Default) gui.render();
    	}   	
     }
        

}
