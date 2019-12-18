package com.alecamar.badlionmod.mainoverlay;

import org.lwjgl.glfw.GLFW;

import com.alecamar.badlionmod.BadlionMod;
import com.alecamar.badlionmod.BadlionMod.UIState;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class OverlayEditor extends Screen {
	
	public OverlayEditor() {
		super(new StringTextComponent("Ovelay editor"));
	}
	
	public Button currentlyMovingMove;
	public Button currentlyMovingRemove;
	public String currentlyMovingUIName="";
	
	public TextFieldWidget newUITextField;
	
	public double lastMouseX,lastMouseY;
	public double realX,realY;
	public double currentRealOffsetX,currentRealOffsetY;
	
	public boolean wasCursorDown=true;

	private ResourceLocation MoveIcon=new ResourceLocation("badlionmod","textures/gui/move.png");
	public static final int moveButtonSize=16;
	
	@Override
	protected void init()
	{
		this.addButton(new Button(5,5,60,20,"Add new",(button) -> {
			IndividualOverlay indiv=new IndividualOverlay(Minecraft.getInstance());
			indiv.CreateDefaultUI();
			BadlionMod.guis.put(newUITextField.getText(),indiv );		
			OverlayEditor.open(); //Reopen the editor to update the changes
		}));
		
		newUITextField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,70,5,100,20,"New UI "+(BadlionMod.guis.size()+1));
		newUITextField.setText("New UI "+(BadlionMod.guis.size()+1));
		buttons.add(newUITextField);
		this.children.add(newUITextField);
			
		//Add all the buttons for each overlay
		for(String key: BadlionMod.guis.keySet())
		{
			IndividualOverlay indiv=BadlionMod.guis.get(key);
			
			indiv.editButtons.clear();			
			Button removeButton=new Button(BadlionMod.guis.get(key).Xpos+14,BadlionMod.guis.get(key).Ypos-44,50,20,"Remove",(button) -> {
				BadlionMod.guis.remove(key);
				OverlayEditor.open(); //Reopen the editor to update the changes
			});
			this.addButton(removeButton);
			indiv.editButtons.add(removeButton);
			
			Button newTileButton=new Button(BadlionMod.guis.get(key).Xpos+65,BadlionMod.guis.get(key).Ypos-44,48,20,"New tile",(button) -> {
				ButtonSettings settings=new ButtonSettings();
				settings.height=2;
				settings.width=2;
				settings.posX=indiv.calculateWidth();
				settings.posY=0;
				settings.keyBinding=minecraft.gameSettings.keyBindForward;
				settings.showCPS=false;
				indiv.buttons.add(new MyButtonControl(settings, minecraft, indiv));
			});
			this.addButton(newTileButton);
			indiv.editButtons.add(newTileButton);
			
			Button moveButton=new Button(BadlionMod.guis.get(key).Xpos-7,BadlionMod.guis.get(key).Ypos-44,20,20,"M",(button) -> {
				if(currentlyMovingMove==null)
				{ //Start moving					
					currentlyMovingMove=button;
					currentlyMovingRemove=removeButton;
					currentlyMovingUIName=key;
					lastMouseX=Minecraft.getInstance().mouseHelper.getMouseX();
					realX=BadlionMod.guis.get(key).Xpos;
					lastMouseY=Minecraft.getInstance().mouseHelper.getMouseY();
					realY=BadlionMod.guis.get(key).Ypos;
					System.out.println("Clicked "+key);
				}else if(currentlyMovingUIName==key) { //Finish movement
					currentlyMovingMove=null;
					currentlyMovingUIName="";
					currentlyMovingRemove=null;
				}				
			});
			this.addButton(moveButton);
			indiv.editButtons.add(moveButton);	
		}
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	public void render(int mouseX,int mouseY, float partialTicks)
	{		
		renderBackground(0);
		
				
			
		for(String key: BadlionMod.guis.keySet())
		{
			IndividualOverlay indiv=BadlionMod.guis.get(key);	
			indiv.render(); //Render each UI here so that they are displayed on TOP of the background

			
			for(MyButtonControl button:indiv.buttons)
			{
				if(button.isMouseIn(mouseX, mouseY)) //Check if the mouse is inside the KeyBind
				{
					if(GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), 0)==1 && wasCursorDown==false)
					{
						KeyBindEditor.open(button);
						BadlionMod.currentState=UIState.Default;
						wasCursorDown=true;
						break;
					}else {
										
						button.render(indiv.Xpos, indiv.Ypos,true); //Re-render the keyBind on top of our hover fill
					}
				}
			}
			
		}
		
		if(GLFW.glfwGetMouseButton(Minecraft.getInstance().mainWindow.getHandle(), 0)==0) wasCursorDown=false;
				
				
		
		
		
		
		
		//Manage UI movement (If moving)
		if(currentlyMovingMove!=null)
		{
			double deltaX=(Minecraft.getInstance().mouseHelper.getMouseX()-lastMouseX)*(double)this.minecraft.mainWindow.getScaledWidth() / (double)this.minecraft.mainWindow.getWidth();
			realX+=deltaX;
			lastMouseX=Minecraft.getInstance().mouseHelper.getMouseX();
			double deltaY=(Minecraft.getInstance().mouseHelper.getMouseY()-lastMouseY)*(double)this.minecraft.mainWindow.getScaledWidth() / (double)this.minecraft.mainWindow.getWidth();
			realY+=deltaY;			
			lastMouseY=Minecraft.getInstance().mouseHelper.getMouseY();
			
			currentRealOffsetX+=deltaX;
			currentRealOffsetY+=deltaY;
			
			IndividualOverlay indiv=BadlionMod.guis.get(currentlyMovingUIName);
			
			if(indiv!=null && indiv.editButtons!=null)
			{
				//We do it this way to avoid accumulative floating point error
				while(currentRealOffsetX>1)
				{
					for(Widget w :indiv.editButtons)
					{				
						w.x+=1;					
					}
					BadlionMod.guis.get(currentlyMovingUIName).Xpos++;
					currentRealOffsetX--;
				}
				while(currentRealOffsetY>1)
				{
					for(Widget w :indiv.editButtons)
					{				
						w.y+=1;					
					}
					BadlionMod.guis.get(currentlyMovingUIName).Ypos++;
					currentRealOffsetY--;
				}
				
				while(currentRealOffsetX<-1)
				{
					for(Widget w :indiv.editButtons)
					{				
						w.x-=1;					
					}
					BadlionMod.guis.get(currentlyMovingUIName).Xpos--;
					currentRealOffsetX++;
				}
				while(currentRealOffsetY<-1)
				{
					for(Widget w :indiv.editButtons)
					{				
						w.y-=1;					
					}
					BadlionMod.guis.get(currentlyMovingUIName).Ypos--;
					currentRealOffsetY++;
				}
						
			}
						
		}
		
		for(String key: BadlionMod.guis.keySet())
		{
			IndividualOverlay indiv=BadlionMod.guis.get(key);
			
			if(key.equals(currentlyMovingUIName))
			{
				GlStateManager.color4f(0f, 0.85f, 1f, 1f);
			}else {
				GlStateManager.color4f(1f, 1f, 1f, 1f);
			}
			
			Minecraft.getInstance().getTextureManager().bindTexture(MoveIcon);	
			blit(indiv.Xpos-5, indiv.Ypos-42, 10, 0F, 0F, moveButtonSize, moveButtonSize, moveButtonSize, moveButtonSize);
			drawString(Minecraft.getInstance().fontRenderer,key,BadlionMod.guis.get(key).Xpos+1, BadlionMod.guis.get(key).Ypos-14,0xFFFFFF);
					
		}
		
		
		super.render(mouseX, mouseY, partialTicks);
	}

	
	
	public static void myFill(int x,int y,int width,int height,int color)
	{
		fill(x+width, y+height, x,y, color);
	}
	
	public static void open()
	{
		Minecraft.getInstance().displayGuiScreen(new OverlayEditor());
	}
	
	@Override
	public void onClose()
	{
		BadlionMod.currentState=UIState.Default;
		BadlionMod.SaveData();
		super.onClose();
	}
}
