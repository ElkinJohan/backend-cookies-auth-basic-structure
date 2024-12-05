package com.elkin.cookiesAuth.services;

import com.elkin.cookiesAuth.dto.ProfileDto;
import com.elkin.cookiesAuth.dto.SignUpDto;
import com.elkin.cookiesAuth.dto.UserDto;
import com.elkin.cookiesAuth.dto.UserSummaryDto;

import java.util.List;

public interface UserService {
    ProfileDto getProfile(Long userId);

    void addFriend(Long friendId);

    List<UserSummaryDto> searchUsers(String term);

    UserDto signUp(SignUpDto user);

    void signOut(UserDto user);
}
