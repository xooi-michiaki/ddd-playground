package playground.order.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.architecture.onion.simplified.ApplicationRing;
import org.springframework.stereotype.Service;
import playground.customer.Customer;
import playground.customer.Customers;
import playground.order.*;
import playground.product.Products;

import javax.transaction.Transactional;

/**
 * @author KOGA, Yu
 */
@ApplicationRing
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final Products products;

    private final Customers customers;

    private final Orders orders;

    private final OrderNumberGenerator orderNumberGenerator;

    @Override
    public Order createOrder(CreateOrder command) {
        log.info("在庫チェック");
        for (OrderLine line: command.getLines()) {
            var product = products.findById(line.getProduct().getId());
            if (product.isPresent()) {
                // 商品が存在したら在庫数確認
                if (product.get().getStock() < line.getQuantity()) {
                    throw new RuntimeException("product out of stock");
                }
            } else {
                throw new RuntimeException("product unavailable");
            }
        }

        log.info("注文番号の採番");
        var orderNo = this.orderNumberGenerator.generate();

        log.info("注文登録");
        Customer customer = this.customers.findById(command.getCustomer()).orElseThrow();
        var order = new Order(
                new Customer.CustomerAssociation(customer),
                orderNo,
                command.getOrderDate(),
                command.getLines()
        );
        return this.orders.save(order);
    }
}