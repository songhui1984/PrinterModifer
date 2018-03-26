package com.neusoft.graphene.basecomponent.printer;

import lombok.Data;

/**
 * Created by lerrain on 2017/9/17.
 */
@Data
public class Sign
{
    Long id;

    String keystore;
    String password;
    String scope;
    String reason;
    String location;

}
