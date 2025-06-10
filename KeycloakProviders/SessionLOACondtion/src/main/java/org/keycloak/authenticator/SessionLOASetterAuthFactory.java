package org.keycloak.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class SessionLOASetterAuthFactory implements AuthenticatorFactory {


    public static final String ID = "s-loa-setter-authenticator";
    public static final String LEVEL_OF_AUTHENTICATION ="level-of-authentication";
    public static final SessionLOASetterAuth AUTHENTICATOR_INSTANCE = new SessionLOASetterAuth();

    @Override
    public String getDisplayType() {
        return "Setter de Session LOA";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

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
        return "Setea el nivel de autenticación de la session";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty loaValueConfig = new ProviderConfigProperty();
        loaValueConfig.setName(LEVEL_OF_AUTHENTICATION);
        loaValueConfig.setLabel("Nivel de autenticación");
        loaValueConfig.setType(ProviderConfigProperty.STRING_TYPE);
        loaValueConfig.setHelpText("Nivel de autenticación que se le setea a la session, debe ser un numero entero");
        loaValueConfig.setDefaultValue("2");

        configProperties.add(loaValueConfig);

        return configProperties;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return AUTHENTICATOR_INSTANCE;
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
        return ID;
    }

}
