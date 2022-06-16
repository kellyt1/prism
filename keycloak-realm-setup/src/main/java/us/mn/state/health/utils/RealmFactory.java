package us.mn.state.health.utils;

import org.keycloak.representations.idm.RealmRepresentation;

import java.util.HashMap;

/**
 * User: ochial1
 * Date: 6/29/2016
 * Time: 11:31 AM
 */
public class RealmFactory {

    public static RealmRepresentation createRealm(String realmId, String realmName, String sslRequired, PasswordPolicy passwordPolicy) {
        RealmRepresentation realm = new RealmRepresentation();

        realm.setId(realmId);
        realm.setRealm(realmId);
        realm.setEnabled(true);
        realm.setSslRequired(sslRequired);
        realm.setDisplayName(realmName);
        realm.setDisplayNameHtml(realmName);
        realm.setRegistrationEmailAsUsername(true);

        realm.setSmtpServer(new HashMap<String, String>());
        realm.getSmtpServer().put("from", "donotreply@state.mn.us");
        realm.getSmtpServer().put("host", "localhost");
        realm.getSmtpServer().put("port", "25");

        realm.setResetPasswordAllowed(true);
        realm.setBruteForceProtected(true);

        // max login failures
        realm.setFailureFactor(10);

        //wait increment
        realm.setWaitIncrementSeconds(60);

        //Quick Login Check Milli Seconds
        realm.setQuickLoginCheckMilliSeconds(1000L);

        //Minimum Quick Login Wait
        realm.setMinimumQuickLoginWaitSeconds(60);

        //Max Wait
        realm.setMaxFailureWaitSeconds(15 * 60);

        //Failure Reset Time
        realm.setMaxDeltaTimeSeconds(12 * 60 * 60);
        realm.setPasswordPolicy(passwordPolicy.getValue());
        realm.setEventsEnabled(true);

        //90 days
        realm.setEventsExpiration(7776000L);
        realm.setAdminEventsEnabled(true);
        realm.setAdminEventsDetailsEnabled(true);

        return realm;
    }
}
