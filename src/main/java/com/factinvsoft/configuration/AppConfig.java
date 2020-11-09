package com.factinvsoft.configuration;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({"USER",})
@ApplicationPath("/api")
public class AppConfig extends Application {
    
}
