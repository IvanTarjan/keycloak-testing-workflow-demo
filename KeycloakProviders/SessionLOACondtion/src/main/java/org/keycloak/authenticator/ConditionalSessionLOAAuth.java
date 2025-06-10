package org.keycloak.authenticator;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Map;

import static org.keycloak.authenticator.SessionLOASetterAuthFactory.LEVEL_OF_AUTHENTICATION;

public class ConditionalSessionLOAAuth  implements ConditionalAuthenticator {

    static final ConditionalSessionLOAAuth SINGLETON = new ConditionalSessionLOAAuth();
    private static final Logger logger = Logger.getLogger(ConditionalSessionLOAAuth.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        if (context == null){
            throw new AuthenticationFlowException("context is null", AuthenticationFlowError.INTERNAL_ERROR);
        }
        boolean negateOutput = false;
        boolean clearUser = false;
        int loaValue = 0;

        var authenticatorConfig = context.getAuthenticatorConfig();
        if (authenticatorConfig != null){
            Map<String, String> config = authenticatorConfig.getConfig();
            if (config != null) {
                negateOutput = Boolean.parseBoolean(config.get(ConditionalSessionLOAAuthFactory.CONF_NOT));
                clearUser = Boolean.parseBoolean(config.get(ConditionalSessionLOAAuthFactory.CONF_CLEAR_USER));
                loaValue = Integer.parseInt(config.get(ConditionalSessionLOAAuthFactory.CONF_REQUIED_LOA));

            }
        }

        final Integer[] highestLoa = {0};
        context.getSession().sessions().getUserSessionsStream(context.getRealm(), context.getUser()).forEach(session -> {
            if (session.getNote(LEVEL_OF_AUTHENTICATION) != null) {
                var loa = Integer.parseInt(session.getNote(LEVEL_OF_AUTHENTICATION));
                if (loa > highestLoa[0]) {
                    highestLoa[0] = loa;
                }
            }
        });

        logger.info("Reading highest LOA: " + highestLoa[0]);
        logger.info("Expected LOA: " + loaValue);
        var result = highestLoa[0] < loaValue;

        var fullResult = negateOutput != result;
        logger.info("Condition result: " + fullResult);
        if (fullResult && clearUser) context.clearUser();
        return fullResult;
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        //Not used
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        //Not used
    }

    @Override
    public void close() {
        //Does nothing
    }
}
