
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import jxl.format.Border;
import net.miginfocom.swing.MigLayout;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public class ModifiedPrintUtility implements Printable {

    Component componentToBePrinted;
    ReadingEntryPanel entryPanel;
    ComponentToBePrintedType type;
    GraphPanel gPanel;
    int originalW, originalH;
    Rectangle rect;
    Container frame;

    public ModifiedPrintUtility(ReadingEntryPanel entryPanel, ComponentToBePrintedType type) {
        this.entryPanel = entryPanel;
        this.type = type;

    }

    public ModifiedPrintUtility(GraphPanel gPanel, ComponentToBePrintedType type) {
        this.gPanel = gPanel;
        frame = (Container) gPanel.getParent();
        this.type = type;
        originalW = gPanel.getW();
        originalH = gPanel.getH();
        rect = gPanel.getBounds();
        // gPanel.set
    }

    public ModifiedPrintUtility(ReadingEntryPanel entryPanel, GraphPanel gp, ComponentToBePrintedType type) {
        this.entryPanel = entryPanel;
        this.gPanel = gp;
        this.type = type;

    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PrinterJob pj = PrinterJob.getPrinterJob();
        File file = new File("resources//myPFValues.pf");
        PageFormat pf = pj.defaultPage();
        try {

            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));

            PageFormatValues pfv = (PageFormatValues) is.readObject();
            if (pfv.getOrient() == GSPageOrientation.LANDSCAPE) {
                pf.setOrientation(PageFormat.LANDSCAPE);
            } else {
                pf.setOrientation(PageFormat.PORTRAIT);

            }
            Paper paper = new Paper();

            paper.setSize(pfv.getW(), pfv.getH());
            paper.setImageableArea(pfv.getImX(), pfv.getImY(), pfv.getImW(), pfv.getImH());
            pf.setPaper(paper);

        } catch (Exception ex) {
            //   System.out.println("Could not read file");
            // ex.printStackTrace();
        }

        PageFormat pageFormat = pj.pageDialog(pf);

        // PageFormat pageFormat = pj.pageDialog(pj.defaultPage());
        int paperWidth = (int) pageFormat.getWidth();
        int paperHeight = (int) pageFormat.getHeight();
        int prnW = (int) pageFormat.getImageableWidth();
        int prnH = (int) pageFormat.getImageableHeight();
        int imX = (int) pageFormat.getImageableX();
        int imY = (int) pageFormat.getImageableY();
        GSPageOrientation orient;
        if (pageFormat.getOrientation() == pageFormat.LANDSCAPE) {
            orient = GSPageOrientation.LANDSCAPE;
        } else {
            orient = GSPageOrientation.PORTRAIT;
        }
        PageFormatValues values = new PageFormatValues(orient, pageFormat.getPaper().getWidth(), pageFormat.getPaper().getHeight(),
                pageFormat.getPaper().getImageableX(), pageFormat.getPaper().getImageableY(),
                pageFormat.getPaper().getImageableWidth(), pageFormat.getPaper().getImageableHeight());
        try {

            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(values);
        } catch (Exception ex) {
            // System.out.println("Could not write file");
        }

        JFrame f = new JFrame("Print Preview");
        f.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        f.setSize(paperWidth, paperHeight + 8);
        f.setLayout(null);
        // f.setLayout(new MigLayout("insets 0 0 0 0","",""));
        f.setBackground(Color.white);
        if (type == ComponentToBePrintedType.GRAPH) {
            // GraphPanel gPanel = new GraphPanel(new ReportPanel(entryPanel, prnW, prnH), prnW, prnH);
            //Change the size of given GraphPanel
            // GraphPanel gPanel = graphPanel;//new GraphPanel(graphPanel, prnW, prnH);
            //GridLinesType gridType = entryPanel.getGridLinesType();
            //gPanel.getGraph().getPlot().setGridLinesType(gridType);

            //change size for printing
            gPanel.setSize(prnW, prnH);
            // gPanel.setWAndH(prnW, prnH);
            for (Renderer renderer : gPanel.getGraph().getPlot().getRendererList()) {
                // ((LoessSmoothRenderer) renderer).getLoessFunction().setBandwidth(entryPanel.getDesiredSmoothnessPercentage());
                renderer.setCurvePaint(Color.black);
            }
            for (RangeAxis axis : gPanel.getGraph().getPlot().getRangeAxesList()) {
                axis.setAxisLinePaint(Color.black);

            }
            gPanel.getGraph().getPlot().getDomainAxis().setAxisLineColor(Color.black);

            gPanel.setBounds(imX, imY, prnW, prnH);

            gPanel.setBackground(Color.white);

            f.getContentPane().setBackground(Color.white);
            f.getContentPane().add(gPanel);
            f.setVisible(true);
            f.getContentPane().getGraphics().clipRect(imX + 100, imY + 50, prnW, prnH);
            componentToBePrinted = gPanel;
        } else if (type == ComponentToBePrintedType.ECONOMY_REPORT) {
            ReportPanel rPanel = new ReportPanel(entryPanel, true);
            if (gPanel != null) {
                GridLinesType grid = gPanel.getGraph().getPlot().getGridLinesType();
                //      rPanel.getGp().getGraph().getPlot().setDataAreaOffset(15);
                rPanel.getGp().getGraph().getPlot().setGridLinesType(grid);
                rPanel.getGp().getGraph().getPlot().setPlotType(ComponentToBePrintedType.ECONOMY_REPORT);

                ArrayList<Renderer> rendererList = rPanel.getGp().getGraph().getPlot().getRendererList();
                double smooth = ((LoessSmoothRenderer) gPanel.getGraph().getPlot().getRenderer(0)).getLoessFunction().getBandwidth();
                for (Renderer renderer : rendererList) {
                    ((LoessSmoothRenderer) renderer).getLoessFunction().setBandwidth(smooth);

                }
            }
            rPanel.getGp().setCurveAndAxisPaintTheSame();
            rPanel.setBounds(imX, imY, prnW, prnH);
           // rPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            rPanel.setBackground(Color.white);
            rPanel.setFontNow(ReportPanel.PRINTABLE_LABEL_FONT);
            rPanel.getReportTable().setRowHeight(14);
             rPanel.getReportTable().setPreferredSize(new Dimension(rPanel.getWidth(),rPanel.getReportTable().getRowCount()
                     *rPanel.getReportTable().getRowHeight()));
             rPanel.getReportTable().getTableHeader().setPreferredSize(new Dimension(rPanel.getWidth(),80));
            //f.getContentPane().setBackground(Color.white);
            f.getContentPane().add(rPanel);
            //f.setContentPane(rPanel);
            f.setVisible(true);
            componentToBePrinted = rPanel;
        } else {
            ReportPanel rPanel = new ReportPanel(entryPanel);
            //  GridLinesType grid=gPanel.getGraph().getPlot().getGridLinesType();
            //rPanel.getGp().getGraph().getPlot().setGridLinesType(grid);
            rPanel.setBounds(imX, imY, prnW, prnH);
            rPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            rPanel.setBackground(Color.white);
           
           // rPanel.setFont(ReportPanel.PRINTABLE_LABEL_FONT);
           // rPanel.getReportTable().setFont(ReportPanel.PRINTABLE_LABEL_FONT);
            //rPanel.getRportTableHeader().setFont(ReportPanel.PRINTABLE_LABEL_FONT);
            rPanel.setFont(ReportPanel.PRINTABLE_LABEL_FONT);
            //f.getContentPane().setBackground(Color.white);
            f.getContentPane().add(rPanel);
            //f.setContentPane(rPanel);
            f.setVisible(true);
            componentToBePrinted = rPanel;
        }
        printJob.setPrintable(this, pageFormat);
        if (printJob.printDialog()) {
            try {
                printJob.print();

            } catch (PrinterException pe) {
                //   System.out.println("Error printing: " + pe);
            }
        }
        f.dispose();
        //restore graphPanel to original size
        //    gPanel.setWAndH(originalW, originalH);
        gPanel.setSize(originalW, originalH);
        gPanel.setBounds(rect);
        //the graph pnel has been removed from original OpeningFrame object when attaching to printable Frame.
        //Now we can restore it 
        if (type == ComponentToBePrintedType.GRAPH || type == ComponentToBePrintedType.ECONOMY_REPORT) {
            frame.add(gPanel);

            gPanel.repaint();
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {

        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            // g2d.drawLine(0, 0, 400,500);
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            //  componentToBePrinted.setSize((int) pageFormat.getPaper().getImageableWidth(), (int) pageFormat.getPaper().getImageableHeight());
            disableDoubleBuffering(componentToBePrinted);

           // g2d.setClip(1,1,(int)pageFormat.getImageableWidth()-1,(int)pageFormat.getImageableHeight()-1);
                    componentToBePrinted.paint(g2d);
                    g2d.setStroke(new BasicStroke(1.5f));
  g2d.drawRect(1,1,(int)pageFormat.getImageableWidth()-1,(int)pageFormat.getImageableHeight()-1);
  g2d.setClip(null);
  enableDoubleBuffering(componentToBePrinted);
            return (PAGE_EXISTS);
        }
    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
