package com.sunjet.service.admin;

import com.sunjet.model.admin.IconEntity;
import com.sunjet.repository.admin.IconRepository;
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
@Service("iconService")
public class IconServiceImpl implements IconService {

    @Autowired
    private IconRepository iconRepository;

    @Override
    public JpaSpecificationExecutor getJpaSpecificationExecutor() {
        return iconRepository;
    }

    @Override
    public JpaRepository getRepository() {
        return iconRepository;
    }

    /**
     * 获取所有的 icon 列表
     *
     * @return
     */
    @Override
    public List<IconEntity> findAll() {
        return iconRepository.findAll();
    }

    /**
     * 根据 icon 的 id,删除 Icon
     *
     * @param iconId
     */
    @Override
    public void deleteById(String iconId) {
        iconRepository.delete(iconId);
    }
}