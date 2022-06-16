package us.mn.state.health.configs;

import java.util.Collections;
import java.util.List;

/**
 * User: kiminn1
 * Date: 5/2/2017
 * Time: 11:53 AM
 */
public enum KcEnvironment {

    LOCAL("http://localhost:7080/?refreshToken=true",
            "http://localhost:7080",
            Collections.singletonList("http://localhost:7080/*"),
            "http://localhost:8080/auth",
            "http://localhost:8080/auth/realms/" + Constants.keycloak_admin_realm + "/protocol/openid-connect/token",
            "http://localhost:8080/auth/admin/realms/" + Constants.realmId + "/components",
            "external"),

    TEST("https://prism.nonprod.health.state.mn.us/?refreshToken=true",
            "https://prism.nonprod.health.state.mn.us",
            Collections.singletonList("https://prism.nonprod.health.state.mn.us/*"),
            "https://authenticator4.nonprod.health.state.mn.us",
            "https://authenticator4.nonprod.health.state.mn.us/realms/" + Constants.keycloak_admin_realm + "/protocol/openid-connect/token",
            "https://authenticator4.nonprod.health.state.mn.us/admin/realms/" + Constants.realmId + "/components",
            "all"),

    PROD("https://prism.web.health.state.mn.us/?refreshToken=true",
            "https://prism.web.health.state.mn.us",
            Collections.singletonList("https://prism.web.health.state.mn.us/*"),
            "https://authenticator4.web.health.state.mn.us",
            "https://authenticator4.web.health.state.mn.us/realms/" + Constants.keycloak_admin_realm + "/protocol/openid-connect/token",
            "https://authenticator4.web.health.state.mn.us/admin/realms/" + Constants.realmId + "/components",
            "all");


    private String appBaseUrl;
    private String appAdminUrl;
    private List<String> appRedirectUri;
    private String url;
    private String tokenEndpoint;
    private String adminComponentEndpoint;
    private String sslRequired;

    KcEnvironment(String appBaseUrl, String appAdminUrl, List<String> appRedirectUri, String url, String tokenEndpoint, String adminComponentEndpoint, String sslRequired) {
        this.appBaseUrl = appBaseUrl;
        this.appAdminUrl = appAdminUrl;
        this.appRedirectUri = appRedirectUri;
        this.url = url;
        this.tokenEndpoint = tokenEndpoint;
        this.adminComponentEndpoint = adminComponentEndpoint;
        this.sslRequired = sslRequired;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public String getAppAdminUrl() {
        return appAdminUrl;
    }

    public List<String> getAppRedirectUri() {
        return appRedirectUri;
    }

    public String getUrl() {
        return url;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public String getAdminComponentEndpoint() {
        return adminComponentEndpoint;
    }

    public String getSslRequired() {
        return sslRequired;
    }

}
