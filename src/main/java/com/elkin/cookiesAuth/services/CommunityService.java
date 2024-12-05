package com.elkin.cookiesAuth.services;

import com.elkin.cookiesAuth.dto.ImageDto;
import com.elkin.cookiesAuth.dto.MessageDto;
import com.elkin.cookiesAuth.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommunityService {
    List<MessageDto> getCommunityMessages(UserDto user, int page);

    List<ImageDto> getCommunityImages(int page);

    MessageDto postMessage(MessageDto messageDto);

    ImageDto postImage(MultipartFile file, String title);
}
