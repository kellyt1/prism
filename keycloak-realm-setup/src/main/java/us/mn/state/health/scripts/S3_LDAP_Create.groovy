package us.mn.state.health.scripts

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.representations.idm.ComponentRepresentation
import us.mn.state.health.configs.Constants
import us.mn.state.health.configs.KcEnvironment
import us.mn.state.health.configs.LdapEnvironment
import us.mn.state.health.utils.KeycloakConnection
import us.mn.state.health.utils.UserFederationProviderFactory

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Form
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/**
 * User: kiminn1
 * Date: 4/27/2017
 * Time: 5:08 PM
 */

KcEnvironment kcEnvironment = KcEnvironment.valueOf(System.console().readLine("Deployment Environment (LOCAL/DEV/TEST/PROD): ").toUpperCase())
createUserFederationProvider(kcEnvironment, LdapEnvironment.MDHAD)

///////////////////////////////////////////////////////////////////////////////////////////////////
/// --------------- Details :) ----------------------------------------------------------------////
///////////////////////////////////////////////////////////////////////////////////////////////////
private static void createUserFederationProvider(KcEnvironment kcEnvironment, LdapEnvironment ldapEnvironment) {

    Form tokenForm = new Form()
        .param("grant_type", "password")
        .param("client_id", Constants.adminClientId)
        .param("username", KeycloakConnection.adminUserPrompt())
        .param("password", KeycloakConnection.adminPasswordPrompt())
    Entity<Form> tokenEntity = Entity.form(tokenForm)

    ComponentRepresentation ldapUserFederationProvider = UserFederationProviderFactory.createUserFederationProvider(ldapEnvironment)
    ObjectMapper mapper = new ObjectMapper()
    String ldapConfigJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ldapUserFederationProvider)

    String tokenResponse = new ResteasyClientBuilder()
        .build()
        .target(UriBuilder.fromPath(kcEnvironment.tokenEndpoint))
        .request()
        .accept(MediaType.APPLICATION_JSON)
        .post(tokenEntity, String.class)

    JsonSlurper slurper = new JsonSlurper()
    String bearerToken = slurper.parseText(tokenResponse).getAt("access_token")

    Response createLdapResponse = new  ResteasyClientBuilder()
        .build()
        .target(UriBuilder.fromPath(kcEnvironment.adminComponentEndpoint))
        .request()
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + bearerToken)
        .post(Entity.json(ldapConfigJson))

    createLdapResponse
}
