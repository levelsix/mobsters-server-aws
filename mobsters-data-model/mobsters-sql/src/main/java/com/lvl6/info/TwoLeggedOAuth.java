package com.lvl6.info;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class TwoLeggedOAuth extends DefaultApi10a {
    @Override
    public String getAccessTokenEndpoint ()       { return ""; };
    @Override
    public String getRequestTokenEndpoint()       { return ""; };
    @Override
    public String getAuthorizationUrl(Token arg0) { return ""; };

}
