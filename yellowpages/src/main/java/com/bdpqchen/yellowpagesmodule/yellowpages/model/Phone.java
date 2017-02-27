package com.bdpqchen.yellowpagesmodule.yellowpages.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chen on 17-2-26.
 */

@Entity
public class Phone  {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "phone")
    private String phone;

    @Property(nameInDb = "category")
    private String category;

    @Property(nameInDb = "department")
    private String department;

    @Property(nameInDb = "isCollected")
    private int isCollected;

    @Generated(hash = 151169786)
    public Phone(Long id, String name, String phone, String category,
            String department, int isCollected) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.department = department;
        this.isCollected = isCollected;
    }

    @Generated(hash = 429398894)
    public Phone() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }



}
