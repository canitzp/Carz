package de.canitzp.carz.integration;

/**
 * @author canitzp
 */
public class IntegrationWrapper {

    private String modid;
    private Class<? extends IInternalIntegration> integrationClass;

    public IntegrationWrapper(String modid, Class<? extends IInternalIntegration> integrationClass){
        this.modid = modid;
        this.integrationClass = integrationClass;
    }

    public String getModid() {
        return modid;
    }

    public Class<? extends IInternalIntegration> getIntegrationClass() {
        return integrationClass;
    }
}
