package us.mn.state.health.domain.service.util;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.util.Utilities;
import us.mn.state.health.common.exceptions.InfrastructureException;

import java.util.Collection;
import java.util.Iterator;


public class SecurityManagerAuthorizationImpl implements AuthenticationProvider
{
    private static Log log = LogFactory.getLog(SecurityManagerAuthorizationImpl.class);

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticated " + authentication.isAuthenticated());
        // Go to Security Manager and set the necessary stuff in the session and if you return yes,  set the token.
        // Go to database and get roles for this user.        
// Get Person and look through there group links and see if they are authenticated.
//         doAuthenticate((String)authentication.getPrincipal(),(String)authentication.getCredentials());
//                UsernamePasswordAuthenticationToken upa = null;
//            GrantedAuthority[] gr =
//                    new GrantedAuthority[]
//                            {new GrantedAuthorityImpl("ABC")
//                                    ,new GrantedAuthorityImpl("DEF")};


//          if (authentication.isAuthenticated()) {
//                upa = new UsernamePasswordAuthenticationToken(
//                authentication.getPrincipal(),
//                authentication.getCredentials(),
//                new GrantedAuthority[] { new GrantedAuthorityImpl("") });
//          }
        return  authentication;
    }

    public boolean supports(Class aClass) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


}