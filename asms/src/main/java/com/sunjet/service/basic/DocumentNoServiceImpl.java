package com.sunjet.service.basic;

import com.sunjet.model.basic.DocumentNoEntity;
import com.sunjet.repository.basic.DocumentNoRepository;
import com.sunjet.utils.common.CommonHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

/**
 * Created by lhj on 16/11/5.
 */
@Transactional
@Service("documentNoService")
public class DocumentNoServiceImpl implements DocumentNoService {
    @Autowired
    private DocumentNoRepository repository;

    @Override
    public String getDocumentNo(String key) {
        String docNo = StringUtils.EMPTY;
        synchronized (this) {
            String strToday = LocalDate.now().toString().replace("-", "");
            DocumentNoEntity entity = repository.findOneByKey(key);
            if (entity == null) {
                DocumentNoEntity documentNoEntity = new DocumentNoEntity();
                documentNoEntity.setDocKey(key);
                documentNoEntity.setDocCode(CommonHelper.mapDocumentNo.get(key).toString());
                documentNoEntity.setLastNoDate(strToday);
                documentNoEntity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(1, 4));
                repository.save(documentNoEntity);
                docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(1, 4);
            } else {
                if (StringUtils.equals(strToday, entity.getLastNoDate())) {
                    Integer nowNo = Integer.parseInt(entity.getLastNoSerialNumber()) + 1;
                    entity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(nowNo, 4));
                    repository.save(entity);
                    docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(nowNo, 4);
                } else {
                    entity.setLastNoDate(strToday);
                    entity.setLastNoSerialNumber(CommonHelper.genFixedStringBySn(1, 4));
                    repository.save(entity);
                    docNo = CommonHelper.mapDocumentNo.get(key).toString() + strToday + CommonHelper.genFixedStringBySn(1, 4);
                }
            }
        }
        return docNo;
    }
}
