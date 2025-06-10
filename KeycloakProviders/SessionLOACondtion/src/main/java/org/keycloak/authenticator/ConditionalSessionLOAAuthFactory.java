package org.keycloak.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class ConditionalSessionLOAAuthFactory implements ConditionalAuthenticatorFactory {
    public static final String CONF_NOT = "not";
    public static final String CONF_CLEAR_USER = "clear-user";
    public static final String CONF_REQUIED_LOA = "required-loa";
    public static final String PROVIDER_ID = "conditional-s-loa-authenticator";

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalSessionLOAAuth.SINGLETON;
    }

    @Override
    public String getDisplayType() {
        return "Condition - Session LOA";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow is executed only if the session has the required LOA or higher";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty negateOutput = new ProviderConfigProperty();
        negateOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        negateOutput.setName(CONF_NOT);
        negateOutput.setLabel("Negate output");
        negateOutput.setHelpText("Apply a not to the check result");
        configProperties.add(negateOutput);

        ProviderConfigProperty loaValueConfig = new ProviderConfigProperty();
        loaValueConfig.setName(CONF_REQUIED_LOA);
        loaValueConfig.setLabel("Nivel de autenticación requerido");
        loaValueConfig.setType(ProviderConfigProperty.STRING_TYPE);
        loaValueConfig.setHelpText("Nivel de autenticación requerido para continuar el flow, debe ser un numero entero");
        loaValueConfig.setDefaultValue("2");
        configProperties.add(loaValueConfig);

        ProviderConfigProperty clearUser = new ProviderConfigProperty();
        clearUser.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        clearUser.setName(CONF_CLEAR_USER);
        clearUser.setLabel("Clear user");
        clearUser.setHelpText("Clear user for fresh authentication");
        clearUser.setDefaultValue("true");
        configProperties.add(clearUser);

        return configProperties;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
