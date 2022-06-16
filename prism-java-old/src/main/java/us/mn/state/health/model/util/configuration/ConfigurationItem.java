package us.mn.state.health.model.util.configuration;

public class ConfigurationItem {
    private Long configurationItemId;
    private String name;
    private String code;
    private String value;
    private ApplicationConfig applicationConfig;
    private int version;

    public String getCode() {
        return code;
    }

    public ConfigurationItem() {
    }

    public ConfigurationItem(String name, String code, String value) {
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public Long getConfigurationItemId() {
        return configurationItemId;
    }

    public void setConfigurationItemId(Long configurationItemId) {
        this.configurationItemId = configurationItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        String result = "";
        result+="Code: " + code;
        result+="\n"+"Name: " + name;
        result+="\n"+"Value: " + value;
        return result;
    }
}

