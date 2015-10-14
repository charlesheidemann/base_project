package com.example.test;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.activiti.cdi.impl.util.ProgrammaticBeanLookup;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.apache.log4j.Logger;
import org.back.model.Pais;
import org.back.model.QPais;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.Greeter;
import com.example.Service;
import com.example.ServiceImpl;
import com.example.VerificarTarefa;
import com.querydsl.jpa.impl.JPAQuery;

@RunWith(Arquillian.class)
public class GreeterTest {

	Logger log = Logger.getLogger(getClass().getName());
	
	@Inject
	Greeter greeter;
	
	@Inject
	ProcessEngine engine;
	
	EntityManager em;
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule(getBeanInstance(ProcessEngine.class));
	
	protected <T> T getBeanInstance(Class<T> clazz) {
		return ProgrammaticBeanLookup.lookup(clazz);
	}

	protected Object getBeanInstance(String name) {
		return ProgrammaticBeanLookup.lookup(name);
	}

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClass(GreeterTest.class)
				.addClass(Greeter.class)
				.addClass(Service.class)
				.addClass(ServiceImpl.class)
				.addClass(Pais.class)
				.addClass(VerificarTarefa.class)
				.addPackages(true, "org.activiti.cdi")
				.addPackages(true, "org.diagrama")
				.addAsResource("META-INF/log4j.properties", "log4j.properties")
				//.addAsResource("MyProcess.bpmn", "MyProcess.bpmn")
				.addAsManifestResource("META-INF/persistence.xml","persistence.xml")
				.addAsManifestResource("META-INF/beans.xml","beans.xml")
				.addAsManifestResource("META-INF/services/org.activiti.cdi.spi.ProcessEngineLookup","services/org.activiti.cdi.spi.ProcessEngineLookup");
		System.out.println(jar.toString(true));
		return jar;
	}
	
	@Before
	public void init(){
		createEM();
		load();
	}

	private void createEM() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		EntityManager em = emf.createEntityManager();
		Assert.assertNotNull(em);
		this.em = em;
	}

	private void load(){
		this.em.getTransaction().begin();
		Pais pais1 = new Pais();
		pais1.setNome("Brasil");
		Pais pais2 = new Pais();
		pais2.setNome("Paraguai");
		this.em.persist(pais1);
		this.em.persist(pais2);
		this.em.getTransaction().commit();	
	}

	private JPAQuery<Void> query() {
		return new JPAQuery<Void>(em);
	}
	
	@Test
	public void should_create_greeting() {
		QPais pais = QPais.pais;
		List<Pais> paises = query().from(pais).select(pais).fetch();
		//paises.stream().forEach(p -> log.info(p.getNome()));
	}
	
	@Test
	@org.activiti.engine.test.Deployment(resources = {"org/diagrama/MyProcess.bpmn"})
	public void testCDI() {
		log.info("Inicia Teste CDI com jbpm");
	    ProcessInstance piCatchSignal = engine.getRuntimeService().startProcessInstanceByKey("myProcess");
	}

}
