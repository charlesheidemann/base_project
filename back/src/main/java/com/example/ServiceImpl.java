package com.example;

import javax.ejb.Stateless;

@Stateless
public class ServiceImpl implements Service {
	public String servico() {
		return "serviço ejb";
	}

}
