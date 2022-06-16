package us.mn.state.health.utils;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.ServerInfoResource;

import java.io.Closeable;

/**
 * User: ochial1
 * Date: 6/24/2016
 * Time: 1:31 PM
 */
public interface KeycloakClient extends Closeable {

    RealmsResource realms();

    RealmResource realm(String realmName);

    ServerInfoResource serverInfo();

}
