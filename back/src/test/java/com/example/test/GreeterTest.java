package com.example.test;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.back.model.Pais;
import org.back.model.QPais;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.Greeter;
import com.example.Service;
import com.example.ServiceImpl;
import com.querydsl.jpa.impl.JPAQuery;

@RunWith(Arquillian.class)
public class GreeterTest {

	Logger log = Logger.getLogger(getClass().getName());
	
	@Inject
	Greeter greeter;
	
	EntityManager em;
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClass(GreeterTest.class)
				.addClass(Greeter.class)
				.addClass(Service.class)
				.addClass(ServiceImpl.class)
				.addClass(Pais.class)
				.addAsResource("META-INF/log4j.properties", "log4j.properties")
				.addAsResource("META-INF/log4j.properties")
				.addAsResource("META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
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
		paises.stream().forEach(p -> log.info(p.getNome()));
	}

}
