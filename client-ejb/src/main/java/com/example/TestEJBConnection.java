package com.example;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import com.example.remotes.ChangeServiceRemote;

public class TestEJBConnection {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote+http://localhost:9090");
        
        // Authentication properties
        props.put(Context.SECURITY_PRINCIPAL, "ejbuser");
        props.put(Context.SECURITY_CREDENTIALS, "ejbpass");

        Context ctx = new InitialContext(props);

        String jndiName = "change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote";
        ChangeServiceRemote service = (ChangeServiceRemote) ctx.lookup(jndiName);

        System.out.println("Nombre de devises: " + service.getAllDevises().size());
        ctx.close();
    }
}