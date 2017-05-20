package org.amoustakos.linker.io.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Server extends RealmObject{

    public static final String COL_ID = "server.id",
                               COL_PROTOCOL = "server.protocol",
                               COL_IP = "server.ip",
                               COL_PORT = "server.port",
                               COL_NAME = "server.name",
                               COL_DESCRIPTION = "server.description";


    @PrimaryKey
    public long id;

    public String protocol;
    public String ip;
    public int port;

    public String name;
    public String description;



}
