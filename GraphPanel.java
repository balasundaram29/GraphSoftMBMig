
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;

/**
 * Write a description of class GraphPanel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public final class GraphPanel extends JPanel implements GraphDrawListener {

    private GraphDrawingPanel drawingPanel;
    private Graph graph;

    private ReportPanel reportPanel;
    private int w;
    private int h;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane tScroller;
    private MergableColumnsTable panel;
    Plot myPlot;
    private JPanel tableScrollerPanel;
    PumpValues declaredValues;
    public static int COMP_COL = 0;
    public static int TYPE_COL = 1;
    public static int DUTY_POINT_COL = 2;
    public static int DISCH_COL = 3;
    public static int TOTAL_HEAD_COL = 4;
    public static int EFF_COL = 5;
    public static int IMAX_COL = 6;

    /**
     * Constructor for objects of class GraphPanel
     */
    public GraphPanel(ReportPanel reportPanel) {
        // this.w = w;
        //this.h = h-35;
        this.reportPanel = reportPanel;
        makeDrawingPanel();
        makeTableScrollerPanel();
        setLayout(new MigLayout("", "[grow,right][]", "[grow][]"));
        setBackground(Color.white);
        add(drawingPanel, "grow,span,wrap");
        add(panel, "grow,span,width : :494,height : :90,gapright 0px,gapbottom 5px");
    }

    //to change the size of the already existing  GraphPanel following constructor is used
    public GraphPanel(GraphPanel gp) {
        this.reportPanel = gp.reportPanel;
        makeDrawingPanel();
        makeTableScrollerPanel();
        setBackground(Color.white);
        add(drawingPanel);
        add(panel);
        // add(tScroller);
    }

    void makeDrawingPanel() {
        Dataset headDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_HEAD);
        Dataset currDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_CURRENT);
        Dataset effDataset = reportPanel.getDataset(DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
        Graph myGraph = new Graph("Graph Software", "Discharge , lps", "Total Head , m", headDataset);
        graph = myGraph;
        myPlot = myGraph.getPlot();
        myPlot.addGraphDrawListener(this);
        DomainAxis xAxis = myPlot.getDomainAxis();
        xAxis.setDataset(headDataset);
        xAxis.setAxisLineColor(Color.blue);
        xAxis.setMaxAxisValue(reportPanel.getValuesForScale().getDischMax());
        RangeAxis theFirst = myPlot.getRangeAxis(0);
        theFirst.setAxisLinePaint(Color.magenta);
        theFirst.setDataset(headDataset);
        theFirst.setMaxAxisValue(reportPanel.getValuesForScale().getHeadMax());
        LoessSmoothRenderer renderer1 = new LoessSmoothRenderer();
        myPlot.setRenderer(0, renderer1);
        renderer1.setDataset(headDataset);
        RangeAxis axis2 = new RangeAxis("Overall Efficiency , %", AxisPosition.LEFT);
        axis2.setAxisLinePaint(Color.red);
        axis2.setDataset(effDataset);
        axis2.setMaxAxisValue(reportPanel.getValuesForScale().getEffMax());

        xAxis.setDataset(effDataset);
        RangeAxis axis3 = new RangeAxis("Current , A", AxisPosition.LEFT);
        axis3.setAxisLinePaint(Color.blue);
        axis3.setDataset(currDataset);
        axis3.setMaxAxisValue(reportPanel.getValuesForScale().getCurrMax());
        xAxis.setDataset(currDataset);

        myPlot.setRangeAxis(1, axis2);
        myPlot.setRangeAxis(2, axis3);

        LoessSmoothRenderer renderer2 = new LoessSmoothRenderer();
        renderer2.setDataset(effDataset);

        myPlot.setRenderer(1, renderer2);

        LoessSmoothRenderer renderer3 = new LoessSmoothRenderer();
        renderer3.setDataset(currDataset);
        myPlot.setRenderer(2, renderer3);
        declaredValues = this.reportPanel.getDeclaredValues();
        myPlot.setDeclaredValues(declaredValues);
        //setLayout(null);
        drawingPanel = new GraphDrawingPanel(myGraph);
        setCurveAndAxisPaintTheSame();

    }

    private void makeTableScrollerPanel() {
        // tableScrollerPanel = new JPanel();
//        tableScrollerPanel.setBackground(Color.white);
        model = new DefaultTableModel();

        //table = new JTable(model);
        panel = new MergableColumnsTable(model);
        panel.setAlignment(Alignment.Horizontal);
        table = panel.getjTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        model.addColumn(" "+AppConstants.COMPANY_NAME);
        model.addColumn("Type : " + reportPanel.getEntryPanel().getPumpTypeField().getText());
        model.addColumn("Duty Point");
        model.addColumn("Q(lps)");
        model.addColumn("TH(mtrs)");
        model.addColumn("OAE(%)");
        model.addColumn("I-Max(Amps)");
        ReadingEntryPanel entryPanel = reportPanel.getEntryPanel();
        Object[] rowData1 = {" IS " + AppConstants.IS_REF, "S.No : " + entryPanel.getSlNoField().getText(), "Guaranteed", declaredValues.getDischarge(),
            declaredValues.getHead(), declaredValues.getEfficiency(), declaredValues.getMaxCurrent()};

        Object[] rowData2 = {" Head Range : " + declaredValues.getHeadRangeMin() + "/" + declaredValues.getHeadRangeMax() + "  (m)",
            "Size :" + reportPanel.getPipeSize(), "Actual", " ", " ", " ", ""};
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(entryPanel.getDateChooser().getDate());
        Object[] rowData3 = {" Date : " + dateString, "Frequency : 50 Hz", "Result", " ", "", " ", " "};
        model.addRow(rowData1);
        model.addRow(rowData2);
        model.addRow(rowData3);
        setTableCellAlignment(JLabel.LEFT, table);

//        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        table.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 8));
        // for(int col=0;col<table.getColumnCount();col++){
        //table.getColumnModel().getColumn(col)
        //      .setHeaderRenderer(new MyHeaderRenderer(Color.black));
        // }
        table.setGridColor(Color.black);
        // table.setShowGrid(true);
        table.setShowVerticalLines(true);
        // table.setBorder(BorderFactory.createLineBorder(Color.black));
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 20));
        table.getTableHeader().setBackground(Color.WHITE);

        table.setRowHeight(17);
        table.setRowMargin(3);
        Font font = new Font("SansSerif", Font.PLAIN, 8);
        table.setFont(font);
        FontMetrics fm = table.getFontMetrics(font);
        TableColumn col = table.getColumnModel().getColumn(COMP_COL);
        col.setPreferredWidth(300);
        table.getColumnModel().getColumn(DUTY_POINT_COL).setMinWidth(fm.stringWidth(" Guaranteed "));
        table.getColumnModel().getColumn(TYPE_COL).setPreferredWidth(200);
        table.getColumnModel().getColumn(IMAX_COL).setMinWidth(fm.stringWidth("I-Max(Amps)"));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(false);
        table.setBorder(new CustomBorder(CustomBorder.ALL-CustomBorder.TOP, 1.3f));

         table.getTableHeader().setBorder(new CustomBorder(CustomBorder.ALL-CustomBorder.BOTTOM,1.30f));

        // tScroller = new JScrollPane(table);
        table.setPreferredSize(new Dimension(getWidth(), table.getRowCount() * table.getRowHeight()));
     //   tableScrollerPanel.add(tScroller,"gapright 120px");
        //panel.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    private void setTableCellAlignment(int alignment, JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.setDefaultRenderer(table.getColumnClass(i), renderer);

        }
        table.updateUI();
    }

    public void graphDrawn(GraphDrawEvent ev) {

        PumpValues obsValues = myPlot.getObsValues();
        String s = String.format("%,.2f", obsValues.getDischarge());
        table.setValueAt(s, 1, 3);
        s = String.format("%,.2f", obsValues.getHead());
        table.setValueAt(s, 1, 4);
        s = String.format("%,.2f", obsValues.getEfficiency());
        table.setValueAt(s, 1, 5);
        s = String.format("%,.2f", obsValues.getMaxCurrent());
        table.setValueAt(s, 1, 6);
        table.setValueAt(obsValues.getDischResult().toString(), 2, 3);
        table.setValueAt(obsValues.getHeadResult().toString(), 2, 4);
        table.setValueAt(obsValues.getEffResult().toString(), 2, 5);
        table.setValueAt(obsValues.getCurrResult().toString(), 2, 6);
        table.doLayout();
        table.validate();
    }

    public void setCurveAndAxisPaintTheSame() {
        for (Renderer renderer : myPlot.getRendererList()) {
            renderer.setCurvePaint(renderer.getDataset().getRangeAxis().getAxisLinePaint());
        }
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return the w
     */
    public int getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(int h) {
        this.h = h;
    }
}
