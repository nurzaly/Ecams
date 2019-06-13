package my.gov.ilpsdk.apps.ecams.model;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Staff implements Searchable{
    private int id;
    private String nama;
    private String email;
    private String ext;
    private String avatar;
    private String bahagian;

    public Staff(int id, String nama, String email, String ext, String avatar, String bahagian) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.ext = ext;
        this.avatar = avatar;
        this.bahagian = bahagian;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getExt() {
        return ext;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBahagian() {
        return bahagian;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setBahagian(String bahagian) {
        this.bahagian = bahagian;
    }

    @Override
    public String getTitle() {
        return getName();
    }
}
