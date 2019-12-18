package com.alecamar.badlionmod.mainoverlay;

import net.minecraft.client.settings.KeyBinding;


public class ButtonSettings {
	public transient KeyBinding keyBinding;
	public String keyBindingDescription="";
	public int posX,posY;
	public int width,height;
	public boolean showCPS;
	
	public ButtonSettings(KeyBinding keyBinding, int posX, int posY, int width, int height,boolean showCPS) {	
		this.keyBinding = keyBinding;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.showCPS=showCPS;
		keyBindingDescription=keyBinding.getKeyDescription();
	}
	
	public ButtonSettings()
	{
		
	}
}
