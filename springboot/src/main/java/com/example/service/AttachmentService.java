package com.example.service;

import com.example.entity.Attachment;
import com.example.mapper.AttachmentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AttachmentService {

    @Resource
    private AttachmentMapper attachmentMapper;

    public void save(Attachment attachment) {
        attachmentMapper.insert(attachment);
    }
}
