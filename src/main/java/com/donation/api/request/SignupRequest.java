package com.donation.api.request;



import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


/**
 * @author TA Admin
 *
 * 
 */

@Setter
@Getter
public class SignupRequest  implements Serializable {
	
 private static final long serialVersionUID = 1L;

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;


}
