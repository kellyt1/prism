package us.mn.state.health.configs

import org.keycloak.common.util.MultivaluedHashMap

/**
 * User: kiminn1
 * Date: 5/2/2017
 * Time: 11:53 AM
 */

enum LdapEnvironment {

    MDHAD("mdh-ad", "ldap", "org.keycloak.storage.UserStorageProvider", createMdhLdapConfig())

    LdapEnvironment(String name, String providerId, String providerType, MultivaluedHashMap ldapConfig){
        this.name = name
        this.providerId = providerId
        this.providerType = providerType
        this.ldapConfig = ldapConfig
    }

    String name, providerId, providerType
    MultivaluedHashMap<String, String> ldapConfig

   private static final MultivaluedHashMap<String, String> createMdhLdapConfig(){
       MultivaluedHashMap<String, String> config = new MultivaluedHashMap<>()
       config.put("priority", ["0"])
       config.put("fullSyncPeriod", ["-1"])
       config.put("changedSyncPeriod", ["-1"])
       config.put("cachePolicy", ["DEFAULT"])
       config.put("evictionDay", [])
       config.put("evictionHour", [])
       config.put("evictionMinute", [])
       config.put("maxLifespan", [])
       config.put("batchSizeForSync", ["1000"])
       config.put("editMode", ["READ_ONLY"])
       config.put("syncRegistrations", ["false"])
       config.put("vendor", ["ad"])
       config.put("usernameLDAPAttribute", ["cn"])
       config.put("rdnLDAPAttribute", ["cn"])
       config.put("uuidLDAPAttribute", ["objectGUID"])
       config.put("userObjectClasses", ["person, organizationalPerson, user"])
       config.put("connectionUrl", [connectionUrlPrompt()])
       config.put("usersDn", [usersDnPrompt()])
       config.put("authType", ["simple"])
       config.put("bindDn", [bindDnPrompt()])
       config.put("bindCredential", [bindCredentialPrompt()])
       config.put("customUserSearchFilter", ["(&(objectCategory=person)(objectClass=user)(!(cn=admin*)))(&(mail=*))"])
       config.put("searchScope", ["2"])
       config.put("useTruststoreSpi", ["ldapsOnly"])
       config.put("connectionPooling", ["true"])
       config.put("connectionTimeout", [])
       config.put("readTimeout", [])
       config.put("pagination", ["true"])
       config.put("allowKerberosAuthentication", ["false"])
       config.put("serverPrincipal", ["0"])
       config.put("keyTab", [])
       config.put("kerberosRealm", [])
       config.put("debug", ["false"])
       config.put("useKerberosForPasswordAuthentication", ["false"])
       return config
   }

    private static String connectionUrlPrompt(){
        return System.console().readLine("LDAP Connection URL: ")
    }

    private static String usersDnPrompt(){
        return System.console().readLine("LDAP Users DN: ")
    }

    private static String bindDnPrompt(){
        return System.console().readLine("LDAP Bind DN: ")
    }

    private static String bindCredentialPrompt(){
        return new String(System.console().readPassword("LDAP Bind Credential: "))
    }

}
