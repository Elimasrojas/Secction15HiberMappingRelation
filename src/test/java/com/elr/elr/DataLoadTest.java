package com.elr.elr;

import com.elr.elr.domain.*;
import com.elr.elr.repositories.CustomerRepository;
import com.elr.elr.repositories.OrderHeaderRepository;
import com.elr.elr.repositories.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;


@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataLoadTest {
    final String PRODUCT_D1 = "Product 1";
    final String PRODUCT_D2 = "Product 2";
    final String PRODUCT_D3 = "Product 3";

    final String TEST_CUSTOMER = "TEST CUSTOMER";

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    /**
     * From MySQL Workbench (or other client, run the following SQL statment, then test below.) Once
     * you commit, the test will complete. If test completes immediately, check autocommit settings in client.
     * Desde MySQL Workbench (u otro cliente, ejecute la siguiente instrucción SQL y luego pruebe a continuación). Una vez
     *       * te comprometes, la prueba se completará. Si la prueba se completa de inmediato,
     *       verifique la configuración de confirmación automática en el cliente.
     *
     *  {@code SELECT * FROM orderservice.order_header where id = 1 for update; }
     */
    @Rollback(value = false)
    @Test
    void testDBLock() {
        Long id = 1l;

        OrderHeader orderHeader = orderHeaderRepository.findById(id).get();

        Address billTo = new Address();
        billTo.setAddress("Bill me ");
        orderHeader.setBillToAddress(billTo);
        orderHeaderRepository.saveAndFlush(orderHeader);

        System.out.println("I updated the order");
    }
    /*
    * Ralacionado con OrderHeader
    @ManyToOne (fetch = FetchType.LAZY) //por defecto es eager
    private Customer customer
    * la idea es cordar el tiempo de las consultas
    * */
    @Test
    void testN_PlusOneProblem() {

        Customer customer = customerRepository.findCustomerByCustomerNameIgnoreCase(TEST_CUSTOMER).get();

        IntSummaryStatistics totalOrdered = orderHeaderRepository.findAllByCustomer(customer).stream()
                .flatMap(orderHeader -> orderHeader.getOrderLines().stream())
                .collect(Collectors.summarizingInt(ol -> ol.getQuantityOrdered()));

        System.out.println("total ordered: " + totalOrdered.getSum());
    }

    /*
    *
    * eager -> trae el join de todas las tablas
    * lazy-> trae consultas individuales */
    @Test
    void testLazyVsEager() {
        OrderHeader orderHeader = orderHeaderRepository.getReferenceById(5l);

        System.out.println("Order Id is: " + orderHeader.getId());

        System.out.println("Customer Name is: " + orderHeader.getCustomer().getCustomerName());

    }

    //@Disabled
    //@Rollback(value = false).
    @Disabled
    @Rollback(value = false)
    @Test
    void testDataLoader() {
        List<Product> products = loadProducts();
        Customer customer = loadCustomers();

        int ordersToCreate = 100;

        for (int i = 0; i < ordersToCreate; i++){
            System.out.println("Creating order #: " + i);
            this.saveOrder(customer, products);
        }

        orderHeaderRepository.flush();
    }

    private OrderHeader saveOrder(Customer customer, List<Product> products){
        Random random = new Random();

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer(customer);

        products.forEach(product -> {
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantityOrdered(random.nextInt(20));
            orderHeader.addOrderLine(orderLine);
            //orderHeader.getOrderLines().add(orderLine);
        });

        return orderHeaderRepository.save(orderHeader);
    }

    private Customer loadCustomers() {
        return getOrSaveCustomer(TEST_CUSTOMER);
    }

    private Customer getOrSaveCustomer(String customerName) {
        System.out.println("_____findCustomerByCustomerNameIgnoreCase");
        return customerRepository.findCustomerByCustomerNameIgnoreCase(customerName)
                .orElseGet(() -> {
                    Customer c1 = new Customer();
                    c1.setCustomerName(customerName);
                    c1.setEmail("test@example.com");
                    Address address = new Address();
                    address.setAddress("123 Main");
                    address.setCity("New Orleans");
                    address.setState("LA");
                    c1.setAddress(address);
                    return customerRepository.save(c1);
                });
    }
    private List<Product> loadProducts(){
        List<Product> products = new ArrayList<>();

        products.add(getOrSaveProduct(PRODUCT_D1));
        products.add(getOrSaveProduct(PRODUCT_D2));
        products.add(getOrSaveProduct(PRODUCT_D3));

        return products;
    }
    private Product getOrSaveProduct(String description) {
        return productRepository.findByDescription(description)
                .orElseGet(() -> {
                    Product p1 = new Product();
                    p1.setDescription(description);
                    p1.setProductStatus(ProductStatus.NEW);
                    return productRepository.save(p1);
                });
    }

}