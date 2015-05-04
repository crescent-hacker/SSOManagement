package com.jetsun.bean.biz;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/18.
 */
public class KeyBean {
    private int id=-1;
    private String key_no=null;
    private String certificate_no=null;
    private String model=null;
    private String location=null;
    private String responsible_person=null;
    private String responsible_telephone=null;
    private String sign=null;
    private String inStorage_person=null;
    private String outStorage_person=null;
    private String loss_person=null;
    private String inStorage_date=null;
    private String outStorage_date=null;
    private String loss_date=null;
    private String lastModified_date=null;
    private String notes=null;
    private int clzt=-1;

/*    public void jsonToBean(String jsonStr){
        Object object = null;
        JSONObject jsonMap = JSONObject.fromObject(jsonStr);

    }*/

    public void setBean(String name,String value){
        if(name.equals("id")) setId(Integer.parseInt(value));
        if(name.equals("key_no")) setKey_no(value);
        if(name.equals("certificate_no")) setCertificate_no(value);
        if(name.equals("model")) setModel(value);
        if(name.equals("location")) setLocation(value);
        if(name.equals("responsible_person")) setResponsible_person(value);
        if(name.equals("responsible_telephone")) setResponsible_telephone(value);
        if(name.equals("sign")) setSign(value);
        if(name.equals("inStorage_person")) setInStorage_person(value);
        if(name.equals("outStorage_person")) setOutStorage_person(value);
        if(name.equals("loss_person")) setLoss_person(value);
        if(name.equals("inStorage_date")) setInStorage_date(value);
        if(name.equals("outStorage_date")) setOutStorage_date(value);
        if(name.equals("loss_date")) setLoss_date(value);
        if(name.equals("lastModified_date")) setLastModified_date(value);
        if(name.equals("notes")) setNotes(value);
        if(name.equals("clzt")) setClzt(Integer.parseInt(value));
    }

    public void mapToBean(Map<String,Object> map){
        this.id=Integer.parseInt(map.get("id").toString());
        this.key_no=map.get("key_no").toString();
        this.certificate_no=map.get("certificate_no").toString();
        this.model=map.get("model").toString();
        this.location=map.get("location").toString();
        this.responsible_person=map.get("responsible_person").toString();
        this.responsible_telephone=map.get("responsible_telephone").toString();
        this.sign=map.get("sign").toString();
        this.inStorage_person=map.get("inStorage_person").toString();
        this.outStorage_person=map.get("outStorage_person").toString();
        this.loss_person=map.get("loss_person").toString();
        this.inStorage_date=map.get("inStorage_date").toString();
        this.outStorage_date=map.get("outStorage_date").toString();
        this.loss_date=map.get("loss_date").toString();
        this.lastModified_date=map.get("lastModified_date").toString();
        this.notes=map.get("notes").toString();
        this.clzt=Integer.parseInt(map.get("clzt").toString());
    }
//getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey_no() {
        return key_no;
    }

    public void setKey_no(String key_no) {
        this.key_no = key_no;
    }

    public String getCertificate_no() {
        return certificate_no;
    }

    public void setCertificate_no(String certificate_no) {
        this.certificate_no = certificate_no;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResponsible_person() {
        return responsible_person;
    }

    public void setResponsible_person(String responsible_person) {
        this.responsible_person = responsible_person;
    }

    public String getResponsible_telephone() {
        return responsible_telephone;
    }

    public void setResponsible_telephone(String responsible_telephone) {
        this.responsible_telephone = responsible_telephone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getInStorage_person() {
        return inStorage_person;
    }

    public void setInStorage_person(String inStorage_person) {
        this.inStorage_person = inStorage_person;
    }

    public String getOutStorage_person() {
        return outStorage_person;
    }

    public void setOutStorage_person(String outStorage_person) {
        this.outStorage_person = outStorage_person;
    }

    public String getLoss_person() {
        return loss_person;
    }

    public void setLoss_person(String loss_person) {
        this.loss_person = loss_person;
    }

    public String getInStorage_date() {
        return inStorage_date;
    }

    public void setInStorage_date(String inStorage_date) {
        this.inStorage_date = inStorage_date;
    }

    public String getOutStorage_date() {
        return outStorage_date;
    }

    public void setOutStorage_date(String outStorage_date) {
        this.outStorage_date = outStorage_date;
    }

    public String getLoss_date() {
        return loss_date;
    }

    public void setLoss_date(String loss_date) {
        this.loss_date = loss_date;
    }

    public String getLastModified_date() {
        return lastModified_date;
    }

    public void setLastModified_date(String lastModified_date) {
        this.lastModified_date = lastModified_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getClzt() {
        return clzt;
    }

    public void setClzt(int clzt) {
        this.clzt = clzt;
    }


}
