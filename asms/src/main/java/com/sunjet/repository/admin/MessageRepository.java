package com.sunjet.repository.admin;

import com.sunjet.model.admin.LogEntity;
import com.sunjet.model.admin.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.sound.midi.SysexMessage;

/**
 * ConfigRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface MessageRepository extends JpaRepository<MessageEntity, String>, JpaSpecificationExecutor<MessageEntity> {


}
