package si.dbcomm.service;

import si.dbcomm.dao.LogChangeProcessBatchDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.LogChangeProcessBatchModel;

public class LogChangeProcessBatchService {
	
	private LogChangeProcessBatchDao logChangeProcessBatchDao;
	
	public LogChangeProcessBatchService(){
		logChangeProcessBatchDao = new LogChangeProcessBatchDao();
	}

	public LogChangeProcessBatchModel findByBatch(BatchModel batch){
		return logChangeProcessBatchDao.findByBatch(batch);
	}
	
	public LogChangeProcessBatchModel update(LogChangeProcessBatchModel entity) throws CrudDatabaseException {
		return logChangeProcessBatchDao.update(entity);
	}
	
}
