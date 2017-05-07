package org.amoustakos.linker.io.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Server extends RealmObject{

    public static final String C_ID = "server.id",
                                C_PROTOCOL = "server.protocol",
                                C_IP = "server.ip",
                                C_PORT = "server.port";


    @PrimaryKey
    public long id;

    public String protocol;
    public String ip;
    public int port;



}
