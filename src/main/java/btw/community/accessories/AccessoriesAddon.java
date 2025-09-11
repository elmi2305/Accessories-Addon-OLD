package btw.community.accessories;

import btw.AddonHandler;
import btw.BTWAddon;
import net.fabricmc.accessories.items.ACItems;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AccessoriesAddon extends BTWAddon {
    private static AccessoriesAddon instance;

    public AccessoriesAddon() {
        super();
    }

    public static AccessoriesAddon getInstance() {
        if (instance == null) {
            instance = new AccessoriesAddon();
        }
        instance.modID = "accessories";
        return instance;
    }


    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        ACItems.initItems();
        ACRecipes.initRecipes();
    }



    public KeyBinding accessoryKeyBind;
    public static String accessoryKey;

    @Override
    public void preInitialize() {

        this.registerProperty("AccessoryAbilityKey", "Y"); // unused key
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        accessoryKey = propertyValues.get("AccessoryAbilityKey"); // unused key
    }

    public void initKeybind(){
        accessoryKeyBind = new KeyBinding("Accessory Ability", Keyboard.getKeyIndex(accessoryKey));

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        KeyBinding[] keyBindings = settings.keyBindings;
        keyBindings = Arrays.copyOf(keyBindings, keyBindings.length + 1);
        keyBindings[keyBindings.length - 1] = accessoryKeyBind;
        settings.keyBindings = keyBindings;
    }
    public void modifyConfigProperty(String propertyName, String newValue) {
        String filename = "config/" + "accessories" + ".properties";
        File config = new File(filename);

        // Ensure the file exists before modifying
        if (!config.exists()) {
            System.out.println("Config file does not exist. Creating a new one.");
            try {
                config.getParentFile().mkdirs(); // Ensure config directory exists
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        List<String> lines = new ArrayList<>();
        boolean propertyFound = false;

        // Read the config file and modify the specified property
        try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(propertyName + "=")) {
                    lines.add(propertyName + "=" + newValue); // Modify specified property
                    propertyFound = true;
                } else {
                    lines.add(line); // Keep other settings unchanged
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // If the property wasn't found, add it at the end
        if (!propertyFound) {
            lines.add(propertyName + "=" + newValue);
        }

        // Write back the modified config file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(config, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}