package com.example;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;
import com.example.remotes.CompteCourantServiceRemote;

public class TestEJBConnection {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        
        // Authentication properties
        props.put(Context.SECURITY_PRINCIPAL, "ejbuser");
        props.put(Context.SECURITY_CREDENTIALS, "ejbpass");

        Context ctx = new InitialContext(props);

        // String jndiName = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote";
        String jndiName = "server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote";

        CompteCourantServiceRemote service = (CompteCourantServiceRemote) ctx.lookup(jndiName);

        // System.out.println("Nombre de devises: " + service.test());
        ctx.close();
    }
}