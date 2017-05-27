package org.amoustakos.linker.io.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Server extends RealmObject{

    public static final String COL_ID = "id",
                               COL_PROTOCOL = "protocol",
                               COL_IP = "ip",
                               COL_PORT = "port",
                               COL_NAME = "name",
                               COL_DESCRIPTION = "description";


    @PrimaryKey
    public long id;

    public String protocol;
    public String ip;
    public int port;

    public String name;
    public String description;



}
