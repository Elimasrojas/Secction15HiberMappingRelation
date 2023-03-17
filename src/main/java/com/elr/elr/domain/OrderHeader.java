package com.elr.elr.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.yaml.snakeyaml.DumperOptions;

import java.util.HashSet;
import java.util.Set;


@Entity
@AttributeOverrides({
        @AttributeOverride(
                name = "shippingAddress.address",
                column = @Column(name = "shipping_address")
        ),
        @AttributeOverride(
                name = "shippingAddress.city",
                column = @Column(name = "shipping_city")
        ),
        @AttributeOverride(
                name = "shippingAddress.state",
                column = @Column(name = "shipping_state")
        ),
        @AttributeOverride(
                name = "shippingAddress.zipCode",
                column = @Column(name = "shipping_zip_code")
        ),
        @AttributeOverride(
                name = "billToAddress.address",
                column = @Column(name = "bill_to_address")
        ),
        @AttributeOverride(
                name = "billToAddress.city",
                column = @Column(name = "bill_to_city")
        ),
        @AttributeOverride(
                name = "billToAddress.state",
                column = @Column(name = "bill_to_state")
        ),
        @AttributeOverride(
                name = "billToAddress.zipCode",
                column = @Column(name = "bill_to_zip_code")
        ),
        @AttributeOverride(
                name = "createdDate",
                column = @Column(name = "createdDate")
        )
})
/*lo podemos traer de BaseEntity y personalizar el norbre de la columna
@AttributeOverride(
                name = "createdDate",
                column = @Column(name = "createdDate")  // si cambiamos e l nombre
                                                            aca lo cambiamos en el script flyway
        )

* Extiende de BaseEntity (MappedSuperclass) el cual es una clase abtracta
* toma todas sus propiedades.
*
* */
public class OrderHeader extends BaseEntity{

    /*lo Activamos para el bloque optimista*/
    @Version
    private Integer version;

    @ManyToOne (fetch = FetchType.LAZY) //por defecto es eager
    private Customer customer;

    /* shippingAddress y billToAddress son un ejemlpo de  Embedded y mediante AttributeOverrides declaramos
     sus propiedades que vienen de Address (@Embeddable) por ejemplo:
     @AttributeOverride(
                name = "shippingAddress.state",
                column = @Column(name = "shipping_state")
        )
     */
    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billToAddress;

    /*
    * ejmplo de una clase enumerada con sus get a setter con su anotacion @Enumerated(EnumType.STRING) para
    * simular el campo con cadenas de texto. NEW, IN_PROCESS, COMPLETE
    * */
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /*@OneToMany(mappedBy = "orderHeader") orderHeader=este nombre se debe por qye se encuentra en la tabla
    * OrderLine como atrubuto referenciado para relacion
    *
    * CascadeType.REMOVE con este podemos pasar el test en OrderHeaderRepositoryTest en @Test
      void testDeleteCascade()
      * eliminamos en cascada para no rompre reglas de integridad
      *
      * EAGER->
    * */
    @OneToMany(mappedBy = "orderHeader", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT) //controlamos la forma con hace la consulta eqger hay quer experimentar rediemtos
                                // de tiempo con grandes cantidades de datos
    private Set<OrderLine> orderLines;
    /*
    * Realacion uno a uno
    *  CascadeType.PERSIST=guardamos en la tabla OrderApproval
    *       si no  tenemos pesist sale este error -->object references an unsaved transient instance -
    *       save the transient instance before flushing
    *  orphanRemoval = true con esta orden JPA sabe que es una tabla huefana
    *                       y le decimos vaya y borre el dato huerfano
    * @OneToOne(cascade = CascadeType.PERSIST, orphanRemoval = true)
    *
    * CascadeType.REMOVE->al no tener esta cacada violamos llaves de integrida
    * alter table order_approval
        add column order_header_id bigint;

        alter table order_approval
        add constraint order_hdr_fk
        foreign key (order_header_id) references order_header (id);
     * */
    //@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE },mappedBy = "orderHeader")
    @Fetch(FetchMode.SELECT)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE })//se borra mapping para ganar velocidad
    private OrderApproval orderApproval;

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setOrderApproval(OrderApproval orderApproval) {
        this.orderApproval = orderApproval;
        //forzamos la relacion
        orderApproval.setOrderHeader(this);
    }

    public void addOrderLine(OrderLine orderLine) {
        if (orderLines == null) {
            orderLines = new HashSet<>();
        }

        orderLines.add(orderLine);
        orderLine.setOrderHeader(this);
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(Address billToAddress) {
        this.billToAddress = billToAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderHeader)) return false;
        if (!super.equals(o)) return false;

        OrderHeader that = (OrderHeader) o;

        if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) : that.getCustomer() != null)
            return false;
        if (getShippingAddress() != null ? !getShippingAddress().equals(that.getShippingAddress()) : that.getShippingAddress() != null)
            return false;
        if (getBillToAddress() != null ? !getBillToAddress().equals(that.getBillToAddress()) : that.getBillToAddress() != null)
            return false;
        if (getOrderStatus() != that.getOrderStatus()) return false;
        return getOrderLines() != null ? getOrderLines().equals(that.getOrderLines()) : that.getOrderLines() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
        result = 31 * result + (getShippingAddress() != null ? getShippingAddress().hashCode() : 0);
        result = 31 * result + (getBillToAddress() != null ? getBillToAddress().hashCode() : 0);
        result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
        result = 31 * result + (getOrderLines() != null ? getOrderLines().hashCode() : 0);
        return result;
    }
}