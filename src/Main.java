import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface IReport {
    String generate();
}

class SalesReport implements IReport {
    public String generate() {
        return "Отчет по продажам: [Данные по продажам...]";
    }
}

class UserReport implements IReport {
    public String generate() {
        return "Отчет по пользователям: [Данные по пользователям...]";
    }
}

abstract class ReportDecorator implements IReport {
    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    public String generate() {
        return report.generate();
    }
}

class DateFilterDecorator extends ReportDecorator {
    private String startDate;
    private String endDate;

    public DateFilterDecorator(IReport report, String startDate, String endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String generate() {
        return report.generate() + "\nФильтр по датам: с " + startDate + " по " + endDate;
    }
}

class SortingDecorator extends ReportDecorator {
    private String sortBy;

    public SortingDecorator(IReport report, String sortBy) {
        super(report);
        this.sortBy = sortBy;
    }

    public String generate() {
        return report.generate() + "\nСортировка по: " + sortBy;
    }
}

class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        return report.generate() + "\n[Экспорт в CSV]";
    }
}

class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        return report.generate() + "\n[Экспорт в PDF]";
    }
}

class AmountFilterDecorator extends ReportDecorator {
    private double minAmount;
    private double maxAmount;

    public AmountFilterDecorator(IReport report, double minAmount, double maxAmount) {
        super(report);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public String generate() {
        return report.generate() + "\nФильтр по сумме: от " + minAmount + " до " + maxAmount;
    }
}

public class Main {
    public static void main(String[] args) {
        IReport report = new SalesReport();

        report = new DateFilterDecorator(report, "01-01-2023", "31-12-2023");
        report = new SortingDecorator(report, "дате");
        report = new CsvExportDecorator(report);

        System.out.println(report.generate());

        System.out.println("\n--- Новый отчет с другими декораторами ---\n");

        IReport userReport = new UserReport();
        userReport = new AmountFilterDecorator(userReport, 1000.0, 5000.0);
        userReport = new PdfExportDecorator(userReport);

        System.out.println(userReport.generate());
    }
}
