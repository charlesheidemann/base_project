package com.example;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.cdi.BusinessProcess;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;

@Named
public class VerificarTarefa implements JavaDelegate {

	Logger log = Logger.getLogger(getClass().getName());

	@Inject
	private BusinessProcess businessProcess;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("Executa Tarefa:" + execution.getProcessBusinessKey());
	}

}
