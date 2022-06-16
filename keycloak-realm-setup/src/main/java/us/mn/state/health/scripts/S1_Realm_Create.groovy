package us.mn.state.health.scripts

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmsResource
import us.mn.state.health.configs.Constants

import us.mn.state.health.configs.KcEnvironment
import us.mn.state.health.utils.KeycloakConnection
import us.mn.state.health.utils.PasswordPolicy
import us.mn.state.health.utils.RealmFactory

/**
 * User: kiminn1
 * Date: 4/27/2017
 * Time: 5:12 PM
 */
KcEnvironment environment = KcEnvironment.valueOf(System.console().readLine("Deployment Environment (LOCAL/DEV/TEST/PROD): ").toUpperCase())
Keycloak keycloak = KeycloakConnection.connect(environment)
RealmsResource realms = keycloak.realms()
realms.create(RealmFactory.createRealm(Constants.realmId,Constants.realmDisplayName,environment.sslRequired,PasswordPolicy.valueOf(System.console().readLine("Password Policy (LOCAL/PROD): ").toUpperCase())))
keycloak.close()
