package com.sunjet.service.admin;

import com.sunjet.model.admin.LogEntity;
import com.sunjet.repository.admin.LogRepository;
import com.sunjet.repository.admin.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 */
@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return messageRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return messageRepository;
    }
}