import static org.junit.jupiter.api.Assertions.*;

import com.darian.ecommerce.order.exception.OrderNotFoundException;
import com.darian.ecommerce.order.entity.Order;
import com.darian.ecommerce.order.enums.OrderStatus;
import com.darian.ecommerce.payment.enums.PaymentStatus;
import com.darian.ecommerce.order.OrderServiceImpl;
import com.darian.ecommerce.payment.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

public class PaymentServiceTest {
    private PaymentServiceImpl paymentService;
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        // Mock OrderRepository

        orderService = Mockito.mock(OrderServiceImpl.class);
//        paymentService = new PaymentServiceImpl(null, null, orderService, null);

    }

    @Test
    public void testValidatePaymentWhenOrderIsConfirmedAndUnpaid() throws OrderNotFoundException {
        // Arrange: Tạo một Order với trạng thái CONFIRMED và UNPAID
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.FAILED);

        // Mock repository trả về order này khi tìm theo orderIdx
        Mockito.when(orderService.findOrderById(1L)).thenReturn(Optional.of(order));

        // Act: Gọi hàm validatePayment
        Boolean result = paymentService.validatePayment(order);

        // Assert: Kiểm tra kết quả trả về là true
        assertTrue(result);
    }

    @Test
    public void testValidatePaymentWhenOrderIsPending() throws OrderNotFoundException {
        // Arrange: Tạo một Order với trạng thái PENDING và UNPAID
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.FAILED);

        // Mock repository trả về order này khi tìm theo orderId
        Mockito.when(orderService.findOrderById(2L)).thenReturn(Optional.of(order));

        // Act: Gọi hàm validatePayment
        Boolean result = paymentService.validatePayment(order);

        // Assert: Kiểm tra kết quả trả về là false
        assertFalse(result);
    }

    @Test
    public void testValidatePaymentWhenOrderIsPaid() throws OrderNotFoundException {
        // Arrange: Tạo một Order với trạng thái CONFIRMED và PAID
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.PAID);

        // Mock repository trả về order này khi tìm theo orderId
        Mockito.when(orderService.findOrderById(3L)).thenReturn(Optional.of(order));

        // Act: Gọi hàm validatePayment
        Boolean result = paymentService.validatePayment(order);

        // Assert: Kiểm tra kết quả trả về là false
        assertFalse(result);
    }

    @Test
    public void testValidatePaymentWhenOrderIsCancelled() throws OrderNotFoundException {
        // Arrange: Tạo một Order với trạng thái CANCELLED và UNPAID
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.FAILED);

        // Mock repository trả về order này khi tìm theo orderId
        Mockito.when(orderService.findOrderById(4L)).thenReturn(Optional.of(order));

        // Act: Gọi hàm validatePayment
        Boolean result = paymentService.validatePayment(order);

        // Assert: Kiểm tra kết quả trả về là false
        assertFalse(result);
    }

    @Test
    public void testValidatePaymentWhenOrderNotFound() throws OrderNotFoundException {
        // Arrange: Mock repository trả về Optional.empty()
        Mockito.when(orderService.findOrderById(5L)).thenReturn(Optional.empty());

        // Act: Gọi hàm validatePayment
//        Boolean result = paymentService.validatePayment();

        // Assert: Kiểm tra kết quả trả về là false vì không tìm thấy đơn hàng
//        assertFalse(result);
    }
}
