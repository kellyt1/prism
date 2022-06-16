package us.mn.state.health.model.util.configuration;

import java.util.HashSet;
import java.util.Set;

public class ApplicationConfig {
    private int version;
    private Long applicationConfigId;
    private String applicationName;
    private String applicationCode;
    private Set configItems = new HashSet();

    public ApplicationConfig() {
    }

    public ApplicationConfig(String applicationName, String applicationCode) {
        this.applicationName = applicationName;
        this.applicationCode = applicationCode;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set getConfigItems() {
        return configItems;
    }

    public void setConfigItems(Set configItems) {
        this.configItems = configItems;
    }

    public Long getApplicationConfigId() {
        return applicationConfigId;
    }

    public void setApplicationConfigId(Long applicationConfigId) {
        this.applicationConfigId = applicationConfigId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }
}
