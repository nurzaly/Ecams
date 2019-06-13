package my.gov.ilpsdk.apps.ecams.model;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Nurzaly on 23/3/2018.
 */

public class Locations implements Searchable, Serializable{
    private Integer id;
    private String location_code;
    private String level;
    private String short_name;
    private String block_name;
    private String location_name;
    private String pemeriksa_1;
    private String pemeriksa_2;
    private String block_code;



    public Locations(Integer id, String location_code, String level, String short_name, String block_name, String location_name) {
        this.id = id;
        this.location_code = location_code;
        this.level = level;
        this.short_name = short_name;
        this.block_name = block_name;
        this.location_name = location_name;
    }

    public Locations(String block_name, String pemeriksa_1, String pemeriksa_2, String block_code) {
        this.block_name = block_name;
        this.pemeriksa_1 = pemeriksa_1;
        this.pemeriksa_2 = pemeriksa_2;
        this.block_code = block_code;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public Integer getId() {

        return id;
    }

    public String getLocation_code() {
        return location_code;
    }

    public String getLevel() {
        return level;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getBlock_name() {
        return block_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getPemeriksa_1() {
        return pemeriksa_1;
    }

    public String getPemeriksa_2() {
        return pemeriksa_2;
    }

    public void setPemeriksa_1(String pemeriksa_1) {
        this.pemeriksa_1 = pemeriksa_1;
    }

    public void setPemeriksa_2(String pemeriksa_2) {
        this.pemeriksa_2 = pemeriksa_2;
    }

    public String getBlock_code() {
        return block_code;
    }

    public void setBlock_code(String block_code) {
        this.block_code = block_code;
    }

    @Override
    public String getTitle() {
        return getBlock_code() + getLevel() + " - " + get_short_name(this.short_name, this.block_name) + ", " + getLocation_name();
    }

    private String get_short_name(String short_name, String block_name){
        try{
            if(!short_name.equals(null)){
                return short_name;
            }
        }
        catch (Exception e) {
            return block_name;
        }
        return block_name;
    }
}
