package com.idea.ad.service;

import com.idea.ad.ExtraApplicationTests;
import com.idea.ad.exception.CustomException;
import com.idea.ad.service.impl.AnotherSpringTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExtraApplicationTests.class},
webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TransactionTest {
    @Autowired
    private ISpringTransaction springTransaction;
    @Autowired
    private AnotherSpringTransaction anotherSpringTransaction;
    @Test
    @Transactional
    public void testCatchExceptionCanNotRollback(){
        springTransaction.CatchExceptionCanNotRollback();
    }
    @Test
    @Transactional
    public void testNotRunTimeExceptionCanNotRollBack() throws CustomException {
        springTransaction.NotRunTimeExceptionCanNotRollBack();
    }
    @Test
    public void testRuntimeExceptionCanRollBack(){
        springTransaction.RuntimeExceptionCanRollBack();
    }
    @Test
    public void testAssignExceptionCanRollBack() throws CustomException {
        springTransaction.AssignExceptionCanRollBack();
    }
    @Test
    public void testOneSaveMethod() throws CustomException {
        springTransaction.RollbackOnlyCanRollBack();
    }
    @Test
    public void testNonTransactionCanNotRollBack() throws CustomException {
        springTransaction.NomTransactionCanNotRollBack();
    }
    @Test
    public void testAnotherSpringTransaction() throws CustomException {
        anotherSpringTransaction.TransactionalCanRollBack();
    }
}
