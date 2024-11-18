package com.donation.api.entity;
/**
 * 
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.donation.api.utill.ERole;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TA Admin
 *
 * 
 */

@Setter
@Getter
@Entity
@Table(name = "roles")
public class RoleEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;

	  @Enumerated(EnumType.STRING)
	  @Column(length = 20)
	  private ERole name;

	  public RoleEntity() {

	  }

	  public RoleEntity(ERole name) {
	    this.name = name;
	  }

}
