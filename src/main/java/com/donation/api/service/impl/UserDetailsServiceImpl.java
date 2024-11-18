
/**
 * 
 */
package com.donation.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.donation.api.dao.UserRepository;
import com.donation.api.entity.UserEntity;

/**
 * @author TA Admin
 *
 * 
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	  @Autowired
	  UserRepository userRepository;


		@Override
		@Transactional
		public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
			UserEntity user = userRepository.findByUsername(credential).orElse(null);
			if (user == null) {
				user = userRepository.findByEmail(credential);
				if (user == null) {
					throw new UsernameNotFoundException("User Not Found with credential: " + credential);
				}
			}

			return UserDetailsImpl.build(user);
		}

}
