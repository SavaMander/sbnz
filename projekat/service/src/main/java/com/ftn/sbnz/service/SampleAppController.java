package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.CustomerAccount;
import com.ftn.sbnz.model.models.Order;
import com.ftn.sbnz.model.models.OrderStatus;
import com.ftn.sbnz.model.util.SecurityUtil;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ftn.sbnz.model.events.Item;

import java.util.ArrayList;


@RestController
public class SampleAppController {
	private static Logger log = LoggerFactory.getLogger(SampleAppController.class);


	private final KieContainer kieContainer;

	@Autowired
	public SampleAppController( KieContainer kieContainer) {

		this.kieContainer = kieContainer;
	}

	@PostMapping(value = "/activate-rules")
	public ResponseEntity<String> activateRules() {

		CustomerAccount account = new CustomerAccount("Korisnik1","korisnik1@maildrop.com","korisnik1","adresa","123-456-789","12345");
		account.getActivePromoCodes().add("promocode1");
		account.getActivePromoCodes().add("promocode2");

		Order order = new Order(account,3350.0);
		order.setStatus(OrderStatus.CANCELLED);

		KieSession kieSession = kieContainer.newKieSession("k-session");
		kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
		try {
			SecurityUtil util = new SecurityUtil();
			kieSession.setGlobal("securityUtil", util);

			kieSession.insert(account);
			kieSession.insert(order);
			kieSession.fireAllRules();

			System.out.println("Novi nivo sumnjivosti: " + account.getSuspicionLevel());

		} finally {
			kieSession.dispose();
		}
		return ResponseEntity.ok("Proba");
	}

	
	
	
}
