package org.amoustakos.linker.io.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject {

    public static final String COLUMN_ID = "id",
                                COLUMN_TITLE = "title",
                                COLUMN_DESCRIPTION = "description",
                                COLUMN_AUTHOR = "author",
                                COLUMN_IMAGE_URL = "imageUrl";

    @PrimaryKey
    private long id;
    private String title;
    private String description;
    private String author;
    private String imageUrl;


    /*
     * Getters - Setters
     */
    public long getId() {
        return id;
    }

    public synchronized void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public synchronized void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public synchronized void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public synchronized void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

