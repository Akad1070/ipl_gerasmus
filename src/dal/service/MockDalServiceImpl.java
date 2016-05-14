package dal.service;

import core.exceptions.BizzException;

import java.sql.PreparedStatement;


public class MockDalServiceImpl implements DalService, DalBackendService {

  @Override
  public boolean startTransaction() throws BizzException {
    return true;
  }

  @Override
  public boolean commit() throws BizzException {
    return true;
  }

  @Override
  public boolean rollback() throws BizzException {
    return true;
  }

  @Override
  public void closeConnection() {}

  @Override
  public PreparedStatement createPrepareStatement(String query, int flag) {
    return null;
  }

  @Override
  public PreparedStatement createPrepareStatement(String query) {
    return null;
  }


}
