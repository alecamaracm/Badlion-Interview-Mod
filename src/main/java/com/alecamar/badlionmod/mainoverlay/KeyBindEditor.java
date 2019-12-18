package com.alecamar.badlionmod.mainoverlay;

import com.alecamar.badlionmod.BadlionMod;
import com.alecamar.badlionmod.BadlionMod.UIState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;

public class KeyBindEditor extends Screen {
	
	MyButtonControl control;
	public KeyBindEditor(MyButtonControl _control) {
		super(new StringTextComponent("KeyEditor editor"));
		
		control=_control;		
	}
	
	TextFieldWidget keyBindTextField;
	
	TextFieldWidget showCPSField;
	
	TextFieldWidget xField;
	TextFieldWidget yField;
	
	TextFieldWidget widthField;	
	TextFieldWidget heightField;
	
	String lastSearch="";
	
	@Override
	protected void init()
	{
		keyBindTextField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,this.width/2-10, this.height/2-44,120,15,"");
		keyBindTextField.setText(net.minecraft.client.resources.I18n.format(control.settings.keyBinding.getKeyDescription()));		
		buttons.add(keyBindTextField);
		this.children.add(keyBindTextField);
		
		showCPSField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,this.width/2-56, this.height/2-13,30,15,"");
		showCPSField.setText(control.settings.showCPS?"YES":"NO");		
		buttons.add(showCPSField);
		this.children.add(showCPSField);		
		
		xField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,this.width/2-75, this.height/2+17,30,15,"");
		xField.setText(control.settings.posX+"");		
		buttons.add(xField);
		this.children.add(xField);
		
		yField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,this.width/2+29,this.height/2+17, 30,15,"");
		yField.setText(control.settings.posY+"");		
		buttons.add(yField);
		this.children.add(yField);
		
		widthField=new TextFieldWidget(Minecraft.getInstance().fontRenderer, this.width/2-58,this.height/2+37,30,15,"");
		widthField.setText(control.settings.width+"");		
		buttons.add(widthField);
		this.children.add(widthField);
		
		heightField=new TextFieldWidget(Minecraft.getInstance().fontRenderer,this.width/2+53,this.height/2+37, 30,15,"");
		heightField.setText(control.settings.height+"");		
		buttons.add(heightField);
		this.children.add(heightField);

		this.addButton(new Button(this.width/2-25,this.height/2+70,50,20,"Close",(button) -> {			
			this.onClose();
		}));
		
		this.addButton(new Button(this.width/2,this.height/2-91,70,20,"Remove tile",(button) -> {			
			control.ui.buttons.remove(control);
			this.onClose();
		}));
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	public void render(int mouseX,int mouseY, float partialTicks)
	{		
		renderBackground(0);
		
		//Redraw the KeyBind we are currently editing so the it is on top of the background
		control.render(control.ui.Xpos, control.ui.Ypos, false); 
	
		
		myFill(this.width/2-126,this.height/2-101,252,202,0xBBFFFFFF);
		myFill(this.width/2-125,this.height/2-100,250,200,0xCC000000);
		
		
		drawCenteredString(getMinecraft().fontRenderer, "Tile editor", this.width/2-50, this.height/2-85, 0xFFFFBB00);
		
		drawCenteredString(getMinecraft().fontRenderer, "Selected KeyBind:", this.width/2-65, this.height/2-58, 0xFFFFFFFF);
		drawString(getMinecraft().fontRenderer, net.minecraft.client.resources.I18n.format(control.settings.keyBinding.getKeyDescription()), this.width/2-15, this.height/2-58, 0xFFAAAAFF);
		drawString(getMinecraft().fontRenderer, "Search KeyBind: ", this.width/2-95, this.height/2-41, 0xFFFFFFFF);
		if(lastSearch.equals(keyBindTextField.getText())==false) //Try to guess the KeyBind if the user typed something new
		{
			lastSearch=keyBindTextField.getText();
			setBestGuess(net.minecraft.client.resources.I18n.format(keyBindTextField.getText()));
		}		
		
		drawCenteredString(getMinecraft().fontRenderer, "Show CPS:", this.width/2-85, this.height/2-10, 0xFFFFFFFF);
		control.settings.showCPS=showCPSField.getText().equals("YES");
		
		drawCenteredString(getMinecraft().fontRenderer, "Tile X:", this.width/2-95, this.height/2+20, 0xFFFFFFFF);
		control.settings.posX=myParseInt(xField.getText(),control.settings.posX);
		
		drawCenteredString(getMinecraft().fontRenderer, "Tile Y:", this.width/2+10, this.height/2+20, 0xFFFFFFFF);		
		control.settings.posY=myParseInt(yField.getText(),control.settings.posY);
		
		drawCenteredString(getMinecraft().fontRenderer, "Tile width:", this.width/2-86, this.height/2+40, 0xFFFFFFFF);		
		control.settings.width=myParseInt(widthField.getText(),control.settings.width);
		
		drawCenteredString(getMinecraft().fontRenderer, "Tile height:", this.width/2+22, this.height/2+40, 0xFFFFFFFF);		
		control.settings.height=myParseInt(heightField.getText(),control.settings.height);
		
		super.render(mouseX, mouseY, partialTicks);
	}

	//Tries to parse the number, and if it can't, it returns the original value
	int myParseInt(String string,int initialData)
	{
		try {
			return Integer.parseInt(string);
		}catch (Exception ex){
			return initialData;
		}
	}
	
	public static void myFill(int x,int y,int width,int height,int color)
	{
		fill(x+width, y+height, x,y, color);
	}
	
	public static void open(MyButtonControl control)
	{
		Minecraft.getInstance().displayGuiScreen(new KeyBindEditor(control));
	}
	
	@Override
	public void onClose()
	{
		OverlayEditor.open();
		BadlionMod.currentState=UIState.Edit;
	}
	
	//Checks which keybind best fits the guess
	public void setBestGuess(String guess)
	{
		for(int i=0;i<getMinecraft().gameSettings.keyBindings.length;i++)
		{
			KeyBinding binding=getMinecraft().gameSettings.keyBindings[i];
			String localizedName=net.minecraft.client.resources.I18n.format(binding.getKeyDescription());
			if(guess.toLowerCase().equals(localizedName.toLowerCase()))
			{
				control.settings.keyBinding=binding;
			}
		}
	}
	
	
}
