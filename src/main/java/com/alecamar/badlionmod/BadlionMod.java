package com.alecamar.badlionmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alecamar.badlionmod.mainoverlay.ButtonSettings;
import com.alecamar.badlionmod.mainoverlay.IndividualOverlay;
import com.alecamar.badlionmod.mainoverlay.MyButtonControl;
import com.alecamar.badlionmod.mainoverlay.OverlayEditor;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("badlionmod")
public class BadlionMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static HashMap<String,IndividualOverlay> guis=new HashMap<String,IndividualOverlay>();
    public static UIState currentState=UIState.Default;
    
    public static Gson gson = new Gson();

    
    public static OverlayEditor editScreen=new OverlayEditor();

    public BadlionMod() {
    	KeyBindings.register();
    	LoadSavedData(); 	
    }

	public enum UIState{
		Hidden,
		Default,
		Edit,
	}
	
	public void LoadSavedData()
	{
		try {	
			UISaveData[] UIToLoad=new UISaveData[0];
			UIToLoad=gson.fromJson(readFile(FMLPaths.CONFIGDIR.get().resolve("BadlionModUIs.json").toString()), UIToLoad.getClass());
		
			for(UISaveData miniData: UIToLoad)
			{
				IndividualOverlay currentUI=new IndividualOverlay(Minecraft.getInstance());
				currentUI.Xpos=miniData.Xpos;
				currentUI.Ypos=miniData.Ypos;
				
				for(ButtonSettings setting:miniData.buttonSettings)
				{
					//Repair keyBinding using the stored info
					for(int i=0;i<Minecraft.getInstance().gameSettings.keyBindings.length;i++)
					{
						KeyBinding binding=Minecraft.getInstance().gameSettings.keyBindings[i];
						if(binding.getKeyDescription().equals(setting.keyBindingDescription))
						{
							setting.keyBinding=binding;
							break;
						}
					}
					
					MyButtonControl control=new MyButtonControl(setting,Minecraft.getInstance(),currentUI);
					currentUI.buttons.add(control);
				}
				
				guis.put(miniData.name, currentUI);
			}
		}
		catch (Exception ex){
			LOGGER.error("Failed to load UI data! "+ex.toString());
		}
	}
	
	public static String readFile(String path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static void SaveData()
	{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FMLPaths.CONFIGDIR.get().resolve("BadlionModUIs.json").toString(),false));
		    
			ArrayList<UISaveData> UIToSave=new ArrayList<UISaveData>();
			for(String key: guis.keySet())
			{
				IndividualOverlay indiv=guis.get(key);
				UISaveData miniData=new UISaveData();
				miniData.name=key;
				miniData.Xpos=indiv.Xpos;
				miniData.Ypos=indiv.Ypos;
				
				ArrayList<ButtonSettings> tempButtons=new ArrayList<ButtonSettings>();
				for(MyButtonControl control: indiv.buttons)
				{
					control.settings.keyBindingDescription=control.settings.keyBinding.getKeyDescription();
					tempButtons.add(control.settings);
				}
				miniData.buttonSettings=(ButtonSettings[])tempButtons.toArray(new ButtonSettings[tempButtons.size()]);
				UIToSave.add(miniData);
			}
			
			writer.append(gson.toJson(UIToSave)); 		     
		    writer.close();
		}catch (Exception ex){
			LOGGER.error("Failed to save UI data! "+ex.toString());
		}
		
	}
}

 class UISaveData{
	 public ButtonSettings[] buttonSettings;
	 public String name;
	 public int Xpos;
	 public int Ypos;
}
