package com.acme.afsvendor.models;

import java.util.List;

public class Result {
    private String _id;
    private String author;
    private String authorSlug;
    private String content;
    private String dateAdded;
    private String dateModified;
    private int length;
    private List<String> tags;

    public Result(String _id, String author, String authorSlug, String content,
                  String dateAdded, String dateModified, int length, List<String> tags) {
        this._id = _id;
        this.author = author;
        this.authorSlug = authorSlug;
        this.content = content;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.length = length;
        this.tags = tags;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorSlug() {
        return authorSlug;
    }

    public void setAuthorSlug(String authorSlug) {
        this.authorSlug = authorSlug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Optional: Override toString for a better representation if required.
    @Override
    public String toString() {
        return "Result{" +
                "_id='" + _id + '\'' +
                ", author='" + author + '\'' +
                ", authorSlug='" + authorSlug + '\'' +
                ", content='" + content + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", dateModified='" + dateModified + '\'' +
                ", length=" + length +
                ", tags=" + tags +
                '}';
    }
}
