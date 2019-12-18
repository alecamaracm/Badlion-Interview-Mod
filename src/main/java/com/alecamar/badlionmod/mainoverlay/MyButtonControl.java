package com.alecamar.badlionmod.mainoverlay;

import java.util.ArrayList;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;

public class MyButtonControl extends AbstractGui {
	
		
	Minecraft mc;
	
	
	IndividualOverlay ui;
	
	boolean wasLastDown;
	long clicksLastInterval=0;
	long lastMillis=System.currentTimeMillis();
	
	ArrayList<Long> clicksThisInterval=new ArrayList<Long>(10);
	
	public ButtonSettings settings;
	
	public MyButtonControl(ButtonSettings _settings,Minecraft _mc,IndividualOverlay _ui) {
		settings=_settings;		
		ui=_ui;
		mc=_mc;
	}
	
	public void update()
	{
		if(settings.keyBinding.isKeyDown() && wasLastDown==false && settings.showCPS)
		{
			clicksThisInterval.add(System.currentTimeMillis());
		}		
		wasLastDown=settings.keyBinding.isKeyDown();
		
		
		if(System.currentTimeMillis()-lastMillis>100 && settings.showCPS)
		{
			
			//Removes old clicks
			lastMillis=System.currentTimeMillis();			
			for(int i=0;i<clicksThisInterval.size();i++)
			{
				if(lastMillis-clicksThisInterval.get(i)>1000)
				{
					clicksThisInterval.remove(i);
					i--;
				}
				
			}
			
			//Update the current CPS
			clicksLastInterval=clicksThisInterval.size();
		}
	}
	
	public void render(int baseX,int baseY, boolean isHovered)
	{
		if(settings.keyBinding==null) return;
		
		int xtemp=baseX+((ui.gridSize+ui.spacerSize)*settings.posX);
		int ytemp=baseY+((ui.gridSize+ui.spacerSize)*settings.posY);
		int widthtemp= settings.width*ui.gridSize;
		int heighttemp=settings.height*ui.gridSize;
		if(settings.width>1) widthtemp+=ui.spacerSize*(settings.width-1);
		if(settings.height>1) heighttemp+=ui.spacerSize*(settings.height-1);
		
		
		String keyName=getKeyNameFromDescription(settings.keyBinding.getKey().getTranslationKey());
		int backColor=0x55000000;
		int textColor=0xBBFFFFFF;
		
		if(isHovered) backColor=0x55FF0000;
		
		if(settings.keyBinding.isKeyDown())
		{
			backColor= 0xBB000000;
			textColor=0xFFFFFFFF;			
		}
		
		myFill(xtemp, ytemp, widthtemp,heighttemp, 0x88FFFFFF);
		myFill(xtemp+ui.borderSize, ytemp+ui.borderSize, widthtemp-ui.borderSize*2,heighttemp-ui.borderSize*2, backColor);
		
		if(keyName.contains("SPACE"))
		{
			myFill(xtemp+widthtemp/2-widthtemp/3, ytemp+heighttemp/2-1,(widthtemp/3)*2,2,textColor);
		}else {
			
			drawCenteredString(mc.fontRenderer, keyName,  xtemp+widthtemp/2, ytemp+heighttemp/2-3 + (settings.showCPS?-6:0), textColor);
		}
		
		if(settings.showCPS)
		{
			drawCenteredString(mc.fontRenderer, clicksLastInterval+" CPS",  xtemp+widthtemp/2, ytemp+heighttemp/2+3, textColor); 	
		}

	}	
	
	public static void myFill(int x,int y,int width,int height,int color)
	{
		fill(x+width, y+height, x,y, color);
	}
	
	public static String getKeyNameFromDescription(String description)
	{
		int count=description.split(Pattern.quote(".")).length;
		if(count==0)
		{			
			return description;	
		}else {
			 
			if(description.contains(".mouse."))
			{
				switch(description.split(Pattern.quote("."))[count-1].toUpperCase())
				{
				case "LEFT":
					return "LMB";
				case "RIGHT":
					return "RMB";
					default:
						return "MOUSE " + description.split(Pattern.quote("."))[count-1].toUpperCase();	
				}
			
			}else
			{
				return description.split(Pattern.quote("."))[count-1].toUpperCase();
			}
		
		}
		
	}

	public boolean isMouseIn( double mouseX,double mouseY) {
		int xtemp=ui.Xpos+((ui.gridSize+ui.spacerSize)*settings.posX);
		int ytemp=ui.Ypos+((ui.gridSize+ui.spacerSize)*settings.posY);
		int widthtemp= settings.width*ui.gridSize;
		int heighttemp=settings.height*ui.gridSize;
		
		return (mouseX>xtemp && mouseX<xtemp+widthtemp && mouseY>ytemp && mouseY<ytemp+heighttemp);		
	}
	
}
