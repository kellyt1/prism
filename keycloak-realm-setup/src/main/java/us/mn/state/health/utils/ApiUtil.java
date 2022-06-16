package us.mn.state.health.utils;

import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;

import javax.ws.rs.core.Response;
import java.net.URI;

public class ApiUtil {

    /**
     * Returns the uuid of the uri path (ex:/realms/realm/groups/91466ae3-1880-4a0b-9ed3-5b775f5ad9fb)
     * @param response the response
     * @return the uuid in the uri path
     */
    public static String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     *
     * @param realm the realm resource
     * @param clientId the client ID(the name)
     * @return a client resource
     */
    public static ClientResource findClientByClientId(RealmResource realm, String clientId) {
        for (ClientRepresentation c : realm.clients().findAll()) {
            if (c.getClientId().equals(clientId)) {
                return realm.clients().get(c.getId());
            }
        }
        return null;
    }

}
