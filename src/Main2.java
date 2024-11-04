import java.util.HashMap;
import java.util.Map;

interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
    double calculateDeliveryCost(String orderId);
}

class InternalDeliveryService implements IInternalDeliveryService {
    private Map<String, String> orders = new HashMap<>();

    public void deliverOrder(String orderId) {
        orders.put(orderId, "Доставляется");
        System.out.println("Внутренняя доставка заказа " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return orders.getOrDefault(orderId, "Заказ не найден");
    }

    public double calculateDeliveryCost(String orderId) {
        return 50.0;
    }
}

class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Отправка товара " + itemId + " через ExternalLogisticsServiceA");
    }

    public String trackShipment(int shipmentId) {
        return "ExternalLogisticsServiceA: Отслеживание отправки " + shipmentId;
    }

    public double getShippingCost(int itemId) {
        return 100.0;
    }
}

class ExternalLogisticsServiceB {
    public void sendPackage(String packageInfo) {
        System.out.println("Отправка пакета: " + packageInfo + " через ExternalLogisticsServiceB");
    }

    public String checkPackageStatus(String trackingCode) {
        return "ExternalLogisticsServiceB: Статус пакета " + trackingCode;
    }

    public double estimateCost(String packageInfo) {
        return 75.0;
    }
}

class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA externalService;
    private int shipmentId;

    public LogisticsAdapterA(ExternalLogisticsServiceA service, int shipmentId) {
        this.externalService = service;
        this.shipmentId = shipmentId;
    }

    public void deliverOrder(String orderId) {
        externalService.shipItem(shipmentId);
        System.out.println("Логгирование: доставка через LogisticsAdapterA для заказа " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return externalService.trackShipment(shipmentId);
    }

    public double calculateDeliveryCost(String orderId) {
        return externalService.getShippingCost(shipmentId);
    }
}

class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB externalService;
    private String trackingCode;

    public LogisticsAdapterB(ExternalLogisticsServiceB service, String trackingCode) {
        this.externalService = service;
        this.trackingCode = trackingCode;
    }

    public void deliverOrder(String orderId) {
        externalService.sendPackage(orderId);
        System.out.println("Логгирование: доставка через LogisticsAdapterB для заказа " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return externalService.checkPackageStatus(trackingCode);
    }

    public double calculateDeliveryCost(String orderId) {
        return externalService.estimateCost(orderId);
    }
}

class DeliveryServiceFactory {
    public static IInternalDeliveryService getDeliveryService(String serviceType, String orderId) {
        if (serviceType.equalsIgnoreCase("internal")) {
            return new InternalDeliveryService();
        } else if (serviceType.equalsIgnoreCase("externalA")) {
            return new LogisticsAdapterA(new ExternalLogisticsServiceA(), Integer.parseInt(orderId));
        } else if (serviceType.equalsIgnoreCase("externalB")) {
            return new LogisticsAdapterB(new ExternalLogisticsServiceB(), orderId);
        } else {
            throw new IllegalArgumentException("Неизвестный тип службы доставки: " + serviceType);
        }
    }
}

public class Main2 {
    public static void main(String[] args) {
        IInternalDeliveryService internalService = DeliveryServiceFactory.getDeliveryService("internal", "123");
        internalService.deliverOrder("123");
        System.out.println("Статус: " + internalService.getDeliveryStatus("123"));
        System.out.println("Стоимость доставки: " + internalService.calculateDeliveryCost("123") + " руб.\n");

        IInternalDeliveryService externalServiceA = DeliveryServiceFactory.getDeliveryService("externalA", "456");
        externalServiceA.deliverOrder("456");
        System.out.println("Статус: " + externalServiceA.getDeliveryStatus("456"));
        System.out.println("Стоимость доставки: " + externalServiceA.calculateDeliveryCost("456") + " руб.\n");

        IInternalDeliveryService externalServiceB = DeliveryServiceFactory.getDeliveryService("externalB", "789");
        externalServiceB.deliverOrder("789");
        System.out.println("Статус: " + externalServiceB.getDeliveryStatus("789"));
        System.out.println("Стоимость доставки: " + externalServiceB.calculateDeliveryCost("789") + " руб.");
    }
}
