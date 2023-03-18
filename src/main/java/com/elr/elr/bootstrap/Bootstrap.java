package com.elr.elr.bootstrap;

import com.elr.elr.domain.Customer;
import com.elr.elr.domain.OrderHeader;
import com.elr.elr.domain.Product;
import com.elr.elr.domain.ProductStatus;
import com.elr.elr.repositories.CustomerRepository;
import com.elr.elr.repositories.OrderHeaderRepository;
import com.elr.elr.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Bootstrap implements CommandLineRunner {
    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

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

    @Autowired
    ProductService productService;
    private void updateProduct(){
        System.out.println("Metodo updateProduct");
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productService.saveProduct(product);

        Product savedProduct2 = productService.updateQOH(savedProduct.getId(), 25);

        System.out.println("Updated Qty: " + savedProduct2.getQuantityOnHand());
        System.out.println("Fin metodo updateProduct");
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicia metodo run");
        //bootstrapOrderService.readOrderData();
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

        /*
        *   @Version
        *   private Integer version;
        *   es necesario que clase customer contenga esta confoguracion
        *   156 optimista locking
        *   estas lineas evitan el bloqueo
        *   Customer savedCustomer = customerRepository.save(customer);
        *   Customer savedCustomer2 = customerRepository.save(savedCustomer);
        *   Customer savedCustomer3 = customerRepository.save(savedCustomer2);
        *
        * */

        updateProduct();
        //Error ->Cannot execute statement in a READ ONLY transaction.
        bootstrapOrderService.readOrderData();

        Customer customer = new Customer();
        customer.setCustomerName("Testing Version");
        Customer savedCustomer = customerRepository.save(customer);
        System.out.println("Version is: " + savedCustomer.getVersion());

        savedCustomer.setCustomerName("Testing Version 2");
        Customer savedCustomer2 = customerRepository.save(savedCustomer);
        System.out.println("Version is: " + savedCustomer2.getVersion());

        savedCustomer2.setCustomerName("Testing Version 3");
        Customer savedCustomer3 = customerRepository.save(savedCustomer2);
        System.out.println("Version is: " + savedCustomer3.getVersion());

        customerRepository.delete(savedCustomer3);

        System.out.println("Fin metodo run");
    }
}