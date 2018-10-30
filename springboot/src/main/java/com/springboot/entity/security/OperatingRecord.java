package com.springboot.entity.security;

import com.springboot.basic.entity.DefaultModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SYS_OPERATERECORD")
public class OperatingRecord extends DefaultModel {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(name="ROLE_ID", nullable = false)
    @Getter
    @Setter
    private Role role;

    @Column(name="RESOURCE_ID", nullable = false)
    @Getter
    @Setter
    private Resource resource;

    public OperatingRecord(){

    }

    public OperatingRecord(User user, Role role, Resource resource){
        this.user = user;
        this.role = role;
        this.resource = resource;
    }

    @Override
    public String toString(){
        return "[user:" + user + ", role:" + role + ", resource:" + resource + "]";
    }

}
