package com.elr.elr.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
public class Customer extends BaseEntity {

    /* Validation esta configurado en el pom
    * ConstraintViolationImpl{interpolatedMessage='la longitud debe estar entre 0 y 50'*/
    @Size(max = 50)
    private String customerName;

    @Valid
    @Embedded
    private Address address;
    @Size(max = 20)
    private String phone;
    @Size(max = 255)
    @Email
    private String email;
    /**
     * La anotación @Version se utiliza en Java para implementar la concurrencia optimista en una
     * aplicación que utiliza una base de datos relacional.
     *
     * La concurrencia optimista es una técnica que permite que varias transacciones puedan acceder
     * y modificar el mismo registro de una base de datos de forma simultánea. En lugar de bloquear
     * el registro, la técnica de concurrencia optimista utiliza una columna de versión en la tabla
     * para evitar conflictos en las actualizaciones concurrentes.
     *
     * La anotación @Version se utiliza para indicar que una propiedad de una entidad Java representa
     * la columna de versión en la tabla correspondiente. La propiedad anotada con @Version debe ser un
     * tipo numérico y debe ser actualizada automáticamente por el proveedor de persistencia en cada
     * actualización de la entidad en la base de datos.
     *
     * Cuando se realiza una actualización en una entidad que utiliza la concurrencia optimista,
     * el proveedor de persistencia verifica que la versión de la entidad que se está actualizando
     * coincide con la versión almacenada en la base de datos. Si las versiones no coinciden,
     * se lanza una excepción de OptimisticLockException, indicando que otra transacción ha
     * modificado el registro desde la última vez que se leyó.
     *
     * En resumen, la anotación @Version se utiliza para implementar la concurrencia optimista
     * en una aplicación que utiliza una base de datos relacional. Permite que varias
     * transacciones puedan acceder y modificar el mismo registro de forma simultánea sin bloquearlo,
     * utilizando una columna de versión en la tabla correspondiente.
     */

    @Version
    private Integer version;

    @OneToMany(mappedBy = "customer")
    private Set<OrderHeader> orders = new LinkedHashSet<>();

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<OrderHeader> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderHeader> orders) {
        this.orders = orders;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}