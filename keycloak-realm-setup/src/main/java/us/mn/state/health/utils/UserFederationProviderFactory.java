package us.mn.state.health.utils;

import org.keycloak.representations.idm.ComponentRepresentation;
import us.mn.state.health.configs.Constants;
import us.mn.state.health.configs.LdapEnvironment;

/**
 * User: kiminn1
 * Date: 5/2/2017
 * Time: 1:21 PM
 */

public class UserFederationProviderFactory {

    public static ComponentRepresentation createUserFederationProvider(LdapEnvironment ldapEnvironment){
        ComponentRepresentation userFederationProvider = new ComponentRepresentation();

        userFederationProvider.setName(ldapEnvironment.getName());
        userFederationProvider.setProviderId(ldapEnvironment.getProviderId());
        userFederationProvider.setProviderType(ldapEnvironment.getProviderType());
        userFederationProvider.setParentId(Constants.realmId);
        userFederationProvider.setConfig(ldapEnvironment.getLdapConfig());

        return userFederationProvider;
    }
}
