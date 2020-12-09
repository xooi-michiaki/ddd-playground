package playground.order;

import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestConstructor;
import playground.customer.Customer;
import playground.product.Product;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author KIYOTA, Takeshi
 */
@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class OrderTests {

    final TestEntityManager em;

    @Test
    void mapping() {
//        var product1 = this.em.persistAndFlush(new Product("P-12345", "チョコモナカジャンボ"));
//        var product2 = this.em.persistAndFlush(new Product("P-12346", "バニラモナカジャンボ"));
//        var product3 = this.em.persistAndFlush(new Product("P-12347", "小豆バー"));
        var product1 = this.em.find(Product.class, new Product.ProductId(UUID.fromString("2b305c7b-5809-4103-ba56-e7aa313aa88b")));
        var product2 = this.em.find(Product.class, new Product.ProductId(UUID.fromString("586b78a4-d999-48b7-938f-38691346b759")));
        var customer = this.em.persistAndFlush(new Customer("空条承太郎"));
        var order = this.em.persistAndFlush(new Order
                ("O-12345",
                        LocalDate.of(2020, 11, 28),
                        customer,
                        new OrderLine(new Product.ProductAssociation(product1),"チョコモナカジャンボ", Money.of(140,"JPY"),1),
                        new OrderLine(new Product.ProductAssociation(product2),"バニラモナカジャンボ", Money.of(140,"JPY"),1)
                        ));
        assertThat(order).isInstanceOf(AggregateRoot.class);
        assertThat(order.getLines().get(0)).isInstanceOf(Entity.class);
        assertThat(order.getId()).isNotNull();
        assertThat(order.getOrderNo()).isEqualTo("O-12345");
        assertThat(order.getOrderDate()).isEqualTo(LocalDate.of(2020, 11, 28));
        assertThat(order.getCustomer().getName()).isEqualTo("空条承太郎");
        assertThat(order.getLines()).hasSize(2);
    }
}
