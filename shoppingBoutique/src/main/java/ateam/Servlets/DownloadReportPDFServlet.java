package ateam.Servlets;

import ateam.DTO.StorePerfomanceInSales;
import ateam.DTO.TopProductDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.jfree.chart.util.Rotation;

@WebServlet("/DownloadReportPDF")
public class DownloadReportPDFServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");

        // Fetch the necessary data from the session or database
        Map<String, BigDecimal> monthReport = (Map<String, BigDecimal>) request.getSession().getAttribute("reportForThisMonth");
        Map<String, StorePerfomanceInSales> topAchievingStores = (Map<String, StorePerfomanceInSales>) request.getSession().getAttribute("topAchievingStores");
        Map<String, BigDecimal> leastPerformingStores = (Map<String, BigDecimal>) request.getSession().getAttribute("leastPerformingStores");
        List<TopProductDTO> topProducts = (List<TopProductDTO>) request.getSession().getAttribute("top40SellingProducts");
        Map<String, BigDecimal> todaysSales = (Map<String, BigDecimal>) request.getSession().getAttribute("Today'sReport");

        // Create a new document
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Add content to the document
            document.add(new Paragraph("Sales Report"));

            // Add Monthly Sales Report as a Table
            if (monthReport != null && !monthReport.isEmpty()) {
                document.add(new Paragraph("Monthly Sales Report"));
                PdfPTable table = new PdfPTable(2); // 2 columns for Month and Amount
                table.addCell("Month");
                table.addCell("Amount");

                for (Map.Entry<String, BigDecimal> entry : monthReport.entrySet()) {
                    table.addCell(entry.getKey());
                    table.addCell(entry.getValue().toString());
                }

                document.add(table);
            }

            // Add other tables and content as needed
            // Top Achieving Stores Bar Graph
            // Inside the DownloadReportPDFServlet doGet method
// Top Achieving Stores Bar Graph
            if (topAchievingStores != null && !topAchievingStores.isEmpty()) {
                document.add(new Paragraph("Top Achieving Stores"));
                DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
                for (Map.Entry<String, StorePerfomanceInSales> entry : topAchievingStores.entrySet()) {
                    barDataset.setValue(entry.getValue().getTotalSold(), "Sales", entry.getKey());
                }

                JFreeChart barChart = ChartFactory.createBarChart(
                        "Top Achieving Stores",
                        "Store",
                        "Sales",
                        barDataset
                );

                ByteArrayOutputStream barChartOutputStream = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(barChartOutputStream, barChart, 600, 400);
                Image barChartImage = Image.getInstance(barChartOutputStream.toByteArray());
                document.add(barChartImage);
            }

            // Least Performing Stores Pie Chart
            if (leastPerformingStores != null && !leastPerformingStores.isEmpty()) {
                document.add(new Paragraph("Least Performing Stores"));
                DefaultPieDataset pieDataset = new DefaultPieDataset();
                for (Map.Entry<String, BigDecimal> entry : leastPerformingStores.entrySet()) {
                    pieDataset.setValue(entry.getKey(), entry.getValue());
                }

                JFreeChart pieChart = ChartFactory.createPieChart3D(
                        "Least Performing Stores",
                        pieDataset,
                        true,
                        true,
                        false
                );
                PiePlot3D plot = (PiePlot3D) pieChart.getPlot();
                plot.setStartAngle(290);
                plot.setDirection(Rotation.CLOCKWISE); // May need to import Rotation from the correct package
                plot.setForegroundAlpha(0.5f);

                ByteArrayOutputStream pieChartOutputStream = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(pieChartOutputStream, pieChart, 600, 400);
                Image pieChartImage = Image.getInstance(pieChartOutputStream.toByteArray());
                document.add(pieChartImage);
            }

            // Top Selling Products
            if (topProducts != null && !topProducts.isEmpty()) {
                document.add(new Paragraph("Top 40 Selling Products"));
                PdfPTable productTable = new PdfPTable(2); // 2 columns for Product and Quantity
                productTable.addCell("Product");
                productTable.addCell("Quantity Sold");

//                for (TopProductDTO product : topProducts) {
//                    productTable.addCell(product.getProductName());
//                    productTable.addCell(String.valueOf(product.getQuantitySold()));
//                }

                document.add(productTable);
            }

            // Today's Sales
            if (todaysSales != null && !todaysSales.isEmpty()) {
                document.add(new Paragraph("Today's Sales"));
                PdfPTable todaysSalesTable = new PdfPTable(2); // 2 columns for Store and Sales
                todaysSalesTable.addCell("Store");
                todaysSalesTable.addCell("Sales");

                for (Map.Entry<String, BigDecimal> entry : todaysSales.entrySet()) {
                    todaysSalesTable.addCell(entry.getKey());
                    todaysSalesTable.addCell(entry.getValue().toString());
                }

                document.add(todaysSalesTable);
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
