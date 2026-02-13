package com.idea.ad.service.impl;

import com.idea.ad.dao.ExtraAdDao;
import com.idea.ad.entity.ExtraAd;
import com.idea.ad.exception.CustomException;
import com.idea.ad.service.ISpringTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class SpringTransactionImpl implements ISpringTransaction {
    private final ExtraAdDao extraAdDao;
    @Autowired
    public SpringTransactionImpl(ExtraAdDao extraAdDao) {
        this.extraAdDao = extraAdDao;
    }

    @Override
    public void CatchExceptionCanNotRollback() {
        try {
            extraAdDao.save(new ExtraAd("qinyi"));
            throw new RuntimeException();
        }catch (Exception ex){
            ex.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public void NotRunTimeExceptionCanNotRollBack() throws CustomException {
        try{
            extraAdDao.save(new ExtraAd("qinyi"));
            throw new RuntimeException();
        }catch (Exception ex){
            throw new CustomException(ex.getMessage());
        }

    }

    @Override
    @Transactional
    public void RuntimeExceptionCanRollBack() {
        extraAdDao.save(new ExtraAd("qinyi"));
        throw new RuntimeException();
    }

    @Override
    @Transactional(rollbackFor = {CustomException.class})
    public void AssignExceptionCanRollBack() throws CustomException {
        try {
            extraAdDao.save(new ExtraAd("qinyi"));
            throw new RuntimeException();
        }catch (Exception ex){
            throw new CustomException(ex.getMessage());
        }
    }
    @Transactional
    public void oneSaveMethod() {
        extraAdDao.save(new ExtraAd("qinyi"));
    }

    @Override
    @Transactional
    public void RollbackOnlyCanRollBack() throws CustomException {
        oneSaveMethod();
        try{
            extraAdDao.save(new ExtraAd());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Transactional
    public void anotherOneSaveMethod() {
        extraAdDao.save(new ExtraAd("qinyi"));
        throw new RuntimeException();
    }

    @Override
    public void NomTransactionCanNotRollBack() {
        anotherOneSaveMethod();
    }
}
