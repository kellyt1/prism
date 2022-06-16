package us.mn.state.health.scripts

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import us.mn.state.health.configs.Constants
import us.mn.state.health.configs.KcEnvironment
import us.mn.state.health.utils.ApiUtil
import us.mn.state.health.utils.KeycloakConnection

/**
 * User: kiminn1
 * Date: 5/2/2017
 * Time: 11:53 AM
 */

Keycloak keycloak = KeycloakConnection.connect(KcEnvironment.valueOf(System.console().readLine("Deployment Environment (LOCAL/DEV/TEST/PROD): ").toUpperCase()))

RealmResource realm = keycloak.realms().realm(Constants.realmId)
ApiUtil.findClientByClientId(realm,Constants.directAccessClieant).remove()
keycloak.close()
