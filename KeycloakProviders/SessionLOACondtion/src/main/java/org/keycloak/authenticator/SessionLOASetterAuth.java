package org.keycloak.authenticator;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import static org.keycloak.authenticator.SessionLOASetterAuthFactory.LEVEL_OF_AUTHENTICATION;

public class SessionLOASetterAuth implements Authenticator {

    Logger logger = Logger.getLogger(SessionLOASetterAuth.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        var loaValue = context.getAuthenticatorConfig().getConfig().get(LEVEL_OF_AUTHENTICATION);
        context.getAuthenticationSession().setUserSessionNote(LEVEL_OF_AUTHENTICATION, loaValue);
        logger.info("Value after setting user session note: " + context.getAuthenticationSession().getUserSessionNotes().get(LEVEL_OF_AUTHENTICATION));
        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }


}
