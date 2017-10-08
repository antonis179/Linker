package org.amoustakos.linker.io.db;

import org.amoustakos.linker.io.models.Server;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Class to handle all Realm IO. <br>
 * Specified as {@link Singleton} to only hold one instance of the {@link Realm} object.
 */
@Singleton
public class RealmManager {
    private final Realm realm;

    @Inject
    public RealmManager() {
        realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
    }

    public void setAutoRefresh(boolean autoRefresh) {
        realm.setAutoRefresh(autoRefresh);
    }

    /*
     * GET
     */
    public RealmResults getByModel(Class model) {
        return realm.where(model).findAll();
    }
    public RealmModel getByID(Class model, String column, String id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, int id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, double id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public RealmModel getByID(Class model, String column, float id) {
        return realm.where(model).equalTo(column, id).findFirst();
    }
    public boolean has(Class model) {
        return realm.where(model).count() > 0;
    }
    public Long getCount(Class model) {
        return realm.where(model).count();
    }

    /*
     * MANAGE
     */

    /**
     * Add a {@link RealmObject} to the database.<br>
     * If copy parameter is specified as true the copied {@link RealmObject}<br>
     * will be returned.
     */
    public RealmObject add(RealmObject object, boolean copy){
        realm.beginTransaction();
        RealmObject returned = null;
        if(copy)
            returned = realm.copyToRealm(object);
        else
            realm.insert(object);
        realm.commitTransaction();
        return returned;
    }

    public void addOrUpdate(RealmObject object){
        realm.beginTransaction();
        realm.insertOrUpdate(object);
        realm.commitTransaction();
    }

    public void remove(RealmObject object){
        realm.beginTransaction();
        object.deleteFromRealm();
        realm.commitTransaction();
    }

    public void clearAll(Class<? extends RealmObject> model) {
        realm.beginTransaction();
        realm.delete(model);
        realm.commitTransaction();
    }


    /*
     * Server specific
     */
    public boolean serverExists(Server srv){
        Server savedSrv = realm.where(Server.class)
                                 .equalTo(Server.COL_IP, srv.ip)
                                 .equalTo(Server.COL_PORT, srv.port)
//                                 .equalTo(Server.COL_PROTOCOL, srv.protocol)
//                                 .equalTo(Server.COL_NAME, srv.name)
                                 .findFirst();
        return savedSrv != null;
    }

    @SuppressWarnings("unchecked")
    public RealmResults<Server> getServers(){
        return (RealmResults<Server>) getByModel(Server.class);
    }


    @Override
    protected void finalize() throws Throwable {
        realm.close();
        super.finalize();
    }
}
