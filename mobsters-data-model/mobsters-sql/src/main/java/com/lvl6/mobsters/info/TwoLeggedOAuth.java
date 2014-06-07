package com.lvl6.mobsters.info;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class TwoLeggedOAuth extends BasePersistentObject {//extends DefaultApi10a {

    private static final long serialVersionUID = 2797144959094896876L;
/*    @Override
    public String getAccessTokenEndpoint ()       { return ""; };
    @Override
    public String getRequestTokenEndpoint()       { return ""; };
    @Override
    public String getAuthorizationUrl(Token arg0) { return ""; };*/

}
