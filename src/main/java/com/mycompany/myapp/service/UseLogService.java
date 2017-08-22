package com.mycompany.myapp.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.UseLog;
import com.mycompany.myapp.repository.UseLogRepository;

@Service
@Transactional
public class UseLogService {

	private final Logger log = LoggerFactory.getLogger(UseLogService.class);
	
	private final UseLogRepository useLogRepository;
	
	public UseLogService(UseLogRepository useLogRepository) {
		this.useLogRepository = useLogRepository;
	}
	
	public void saveUseLog(Long id, String name, BigDecimal count, Integer type) {
		UseLog useLog = new UseLog();
		useLog.setNameId(id);
		useLog.setName(name);
		useLog.setCount(count);
		useLog.setType(type);
		useLogRepository.save(useLog);
	}
}
