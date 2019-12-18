package com.alecamar.badlionmod.mainoverlay;

import java.util.ArrayList;
import com.alecamar.badlionmod.BadlionMod;
import com.alecamar.badlionmod.BadlionMod.UIState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;


public class IndividualOverlay extends AbstractGui {
    
	
	
	public ArrayList<MyButtonControl> buttons=new ArrayList<MyButtonControl>();
	
	public int Xpos=50;
	public int Ypos=90;
	
	public int gridSize=16;
	public int spacerSize=2;
	public int borderSize=1;
	
	public ArrayList<Button> editButtons=new ArrayList<Button>();
	
	public static final int moveButtonSize=8;
	
	Minecraft mc;
	
	public boolean moveDown=false;
	public int lastXmove,lastYmove;
	
	public IndividualOverlay(Minecraft _mc)
	{
		mc=_mc;	
	}
	
	//Creates the default layout as describes in the assignment
	public void CreateDefaultUI()
	{
		buttons.add( new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindForward, 2,0, 2,2,false),mc,this));
		buttons.add( new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindBack, 2,2, 2,2,false),mc,this));
		buttons.add(new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindLeft, 0,2, 2,2,false),mc,this));
		buttons.add(new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindRight, 4,2, 2,2,false),mc,this));
		buttons.add(new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindJump, 0,6, 6,1,false),mc,this));
		buttons.add(new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindAttack, 0,4, 3,2,true),mc,this));
		buttons.add(new MyButtonControl(new ButtonSettings(mc.gameSettings.keyBindUseItem, 3,4, 3,2,true),mc,this));
	}
	
	
	public void render()
	{
		for(MyButtonControl control:buttons)
		{
			control.update();			
		}
		
		if(BadlionMod.currentState==UIState.Edit)
		{
			int maxWidth=calculateWidth();
			int maxHeight=calculateHeight();
			
			
			myFill(Xpos-6,Ypos-22,maxWidth*(gridSize+spacerSize)-spacerSize+12,maxHeight*(gridSize+spacerSize)-spacerSize+28,0xBBFFFFFF);	
			myFill(Xpos-5,Ypos-21,maxWidth*(gridSize+spacerSize)-spacerSize+10,maxHeight*(gridSize+spacerSize)-spacerSize+26,0xBB000000);
			
		}else {
			moveDown=false;
		}
		
		for(MyButtonControl control:buttons)
		{
			control.render(Xpos, Ypos,false);
		}
		
	}
	
	

	//Calculates the current maximum width of the grid
	public int calculateWidth()
	{
		int maxWidth=0;
		for(MyButtonControl control:buttons)
		{
			int maxX=control.settings.posX+control.settings.width;
			if(maxX>maxWidth) maxWidth=maxX;				
		}
		return maxWidth;
	}
	
	//Calculates the current maximum height of the grid
	public int calculateHeight()
	{
		int maxHeight=0;
		for(MyButtonControl control:buttons)
		{
			int maxY=control.settings.posY+control.settings.height;
			if(maxY>maxHeight) maxHeight=maxY;				
		}
		return maxHeight;
	}

	
	public static void myFill(int x,int y,int width,int height,int color)
	{
		fill(x+width, y+height, x,y, color);
	}


}
