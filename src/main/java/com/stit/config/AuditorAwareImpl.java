package com.stit.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author User
 */
public class AuditorAwareImpl implements AuditorAware<String> {
//bprivate List<String> userList;
//private Random random = new Random(13);

    public AuditorAwareImpl() {
//this.userList = List.of("dav", "mia", "mike", "jeff", "google", "amazon", "yahoo", "MS" );
    }

    @Override
    public Optional<String> getCurrentAuditor() {
//int index = this.random.nextInt(this.userList.size());
//return Optional.of(this.userList.get(index));

//String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetail
                = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Optional.ofNullable(userDetail.getUsername());
    }

}
