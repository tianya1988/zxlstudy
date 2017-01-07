package es.bean;

import java.util.Date;

/**
 * Created by jason on 15-9-20.
 */
public class EsStrToBean {
    private int beanId;
    private String beanName;
    private Date beanCreateTime;

    public int getBeanId() {
        return beanId;
    }

    public void setBeanId(int beanId) {
        this.beanId = beanId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Date getBeanCreateTime() {
        return beanCreateTime;
    }

    public void setBeanCreateTime(Date beanCreateTime) {
        this.beanCreateTime = beanCreateTime;
    }
}


