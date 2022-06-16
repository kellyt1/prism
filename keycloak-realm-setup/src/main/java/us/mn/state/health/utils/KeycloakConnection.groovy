package us.mn.state.health.utils

import org.keycloak.admin.client.Keycloak
import us.mn.state.health.configs.Constants
import us.mn.state.health.configs.KcEnvironment

class KeycloakConnection {

    static Console console = System.console()

    static Keycloak connect(KcEnvironment environment) {
        return getConnection(
                environment.url,
                Constants.keycloak_admin_realm,
                adminUserPrompt(),
                adminPasswordPrompt(),
                Constants.adminClientId)
    }


    private static Keycloak getConnection(String keycloakUrl,
                                          String adminRealm,
                                          String keycloakAdminUser,
                                          String keycloakAdminPassword,
                                          String clientId) {
        return Keycloak.getInstance(keycloakUrl, adminRealm, keycloakAdminUser, keycloakAdminPassword, clientId)
    }


    static String adminUserPrompt(){
        return console.readLine("Keycloak Admin User: ")
    }

    static String adminPasswordPrompt(){
        return new String(console.readPassword("Keycloak Admin Password: "))
    }
}
