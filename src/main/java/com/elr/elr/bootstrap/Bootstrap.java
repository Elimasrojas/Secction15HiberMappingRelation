package com.elr.elr.bootstrap;

import com.elr.elr.domain.OrderHeader;
import com.elr.elr.repositories.OrderHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Bootstrap implements CommandLineRunner {
    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    BootstrapOrderService bootstrapOrderService;

//    @Transactional
//    public void readOrderData(){
//        OrderHeader orderHeader= orderHeaderRepository.findById(1L).get();
//        orderHeader.getOrderLines().forEach(ol -> {
//            System.out.println(ol.getProduct().getDescription());
//            /*
//             * Caused by: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
//             *  com.elr.elr.domain.Product.categories: could not initialize proxy - no Session
//             * necesita @Transacional
//
//             * */
//            ol.getProduct().getCategories().forEach(cat ->{
//                System.out.println(cat.getDescription() );
//            });
//        });
//    }

    /*
    * SE EJECUTA E INICIA CON SPING CONTEXT
    * */


    @Override
    public void run(String... args) throws Exception {
        bootstrapOrderService.readOrderData();
        //System.out.println("I was called!");
//        OrderHeader orderHeader= orderHeaderRepository.findById(1L).get();
//        orderHeader.getOrderLines().forEach(ol -> {
//            System.out.println(ol.getProduct().getDescription());
//            /*
//            * Caused by: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
//            *  com.elr.elr.domain.Product.categories: could not initialize proxy - no Session
//            * necesita @Transacional
//
//            * */
//            ol.getProduct().getCategories().forEach(cat ->{
//                System.out.println(cat.getDescription() );
//            });
//        });
    }
}