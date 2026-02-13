package com.idea.ad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotherSpringTransaction {
    private final SpringTransactionImpl springTransaction;
    @Autowired
    public AnotherSpringTransaction(SpringTransactionImpl springTransaction) {
        this.springTransaction = springTransaction;
    }
    public void TransactionalCanRollBack(){
        springTransaction.anotherOneSaveMethod();
    }
}
