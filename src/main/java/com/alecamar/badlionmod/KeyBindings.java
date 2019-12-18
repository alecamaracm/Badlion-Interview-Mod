package com.alecamar.badlionmod;


import com.alecamar.badlionmod.BadlionMod.UIState;
import com.alecamar.badlionmod.mainoverlay.OverlayEditor;

//https://github.com/LWJGL/lwjgl/blob/master/src/java/org/lwjgl/input/Keyboard.java



import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	
    public static KeyBinding openEditor;
 
    public static void register()
    {
    	//Register the keyBind
    	openEditor = new KeyBinding("Open UI editor",50, "Badlion Interview Mod");      
        openEditor.setToDefault();
        ClientRegistry.registerKeyBinding(openEditor);
        
        MinecraftForge.EVENT_BUS.register(new ShowHideUIKey());
    }
}

class ShowHideUIKey{
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
		
		//Open the editor if when the selected key is pressed
        if (KeyBindings.openEditor.isPressed())
        {
            if(BadlionMod.currentState==UIState.Default)
            {
            	BadlionMod.currentState=UIState.Edit;
            	OverlayEditor.open();
            }
        }
    }
}

