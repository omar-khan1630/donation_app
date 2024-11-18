package com.donation.api.request;


import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;

//import jax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class LoginRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
    private String username;

	@NotBlank
	private String password;
	
	private Set<String> role;
}
