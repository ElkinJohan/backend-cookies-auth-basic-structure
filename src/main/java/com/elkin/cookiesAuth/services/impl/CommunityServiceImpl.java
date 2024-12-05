package com.elkin.cookiesAuth.services.impl;

import com.elkin.cookiesAuth.dto.ImageDto;
import com.elkin.cookiesAuth.dto.MessageDto;
import com.elkin.cookiesAuth.dto.UserDto;
import com.elkin.cookiesAuth.services.CommunityService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Override
    public List<MessageDto> getCommunityMessages(UserDto user, int page) {
        return Arrays.asList(new MessageDto(1L, "Primer mensaje " + user.getFirstName()),
                new MessageDto(2L, "Segundo mensaje " + user.getFirstName()));
    }

    @Override
    public List<ImageDto> getCommunityImages(int page) {
        return Arrays.asList(new ImageDto(1L, "Primer titulo", null),
                new ImageDto(2L, "Segundo titulo", null));
    }

    @Override
    public MessageDto postMessage(MessageDto messageDto) {
        return new MessageDto(3L, "Nuevo mensaje");
    }

    @Override
    public ImageDto postImage(MultipartFile file, String title) {
        return new ImageDto(3L, "Nuevo titulo", null);
    }
}
