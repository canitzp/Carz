package de.canitzp.carz.asm;

import de.canitzp.carz.Carz;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author canitzp
 */
@IFMLLoadingPlugin.Name("Carz Core Plugin - Hacking entity Light")
public class CarzCorePlugin implements IFMLLoadingPlugin{

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{CarzEntityLightTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
