package com.sparta.foodtruck.domain.user.sercurity;


import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.entity.User;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import com.sparta.foodtruck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AccountInfoRepository accountInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {
        User user = userRepository.findByAddress(address)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + address));
        AccountInfo accountInfo = accountInfoRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + address));
        return new UserDetailsImpl(user, accountInfo);
    }
}