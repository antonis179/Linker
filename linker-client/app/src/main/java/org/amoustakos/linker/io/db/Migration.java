package org.amoustakos.linker.io.db;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
    }



    //Reference
    // Migrate from version 1 to version 2
//        if (oldVersion == 1) {
//        // Create a new class
//        RealmObjectSchema petSchema = schema.create("Server")
//                .addField("name", String.class, FieldAttribute.REQUIRED)
//                .addField("type", String.class, FieldAttribute.REQUIRED);
//
//        // Add a new field to an old class and populate it with initial data
//        schema.get("Person")
//                .addRealmListField("pets", petSchema)
//                .transform(new RealmObjectSchema.Function() {
//                    @Override
//                    public void apply(DynamicRealmObject obj) {
//                        if (obj.getString("fullName").equals("JP McDonald")) {
//                            DynamicRealmObject pet = realm.createObject("Pet");
//                            pet.setString("name", "Jimbo");
//                            pet.setString("type", "dog");
//                            obj.getList("pets").add(pet);
//                        }
//                    }
//                });
//        oldVersion++;
//    }
}
