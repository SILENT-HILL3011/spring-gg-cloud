package com.idea.ad.service;

import com.idea.ad.exception.CustomException;

public interface ISpringTransaction {
    void CatchExceptionCanNotRollback();
    void NotRunTimeExceptionCanNotRollBack()throws CustomException;
    // unchecked exception can rollback
    void RuntimeExceptionCanRollBack();
    // checked exception can rollback
    void AssignExceptionCanRollBack() throws CustomException;
    // rollback only can rollback
    void RollbackOnlyCanRollBack() throws CustomException;
    // 在同一个类中不标注事物的方法运行标注事物的方法，事物失效
    void NomTransactionCanNotRollBack();
}
