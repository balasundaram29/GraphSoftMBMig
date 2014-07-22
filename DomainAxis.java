
import java.awt.*;
import java.awt.geom.*;

/**
 * Write a description of class DomainAxis here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DomainAxis extends Axis {

    private double y, xRight, xLeft, scale;
    String label;
    private ScaleSelectionMode scaleMode;
    private AxisPosition axisPosition;
    private Dataset dataset;
    private Color DEFAULT_AXIS_LINE_COLOR = Color.black;
    private Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 10);
    private ComponentToBePrintedType DEFAULT_PLOT_TYPE = ComponentToBePrintedType.GRAPH;
    private ComponentToBePrintedType plotType;
    private Font font;
    private ScaleSelectionMode DEFAULT_SCALE_MODE = ScaleSelectionMode.CALCULATE_FROM_DATASET;
    Color axisLineColor;
    private Stroke stroke;
    private Stroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(1.5f);
    private double maxAxisValue;

    public DomainAxis(String label) {
        this.label = label;
        this.axisLineColor = this.DEFAULT_AXIS_LINE_COLOR;
        this.scaleMode = this.DEFAULT_SCALE_MODE;
        this.maxAxisValue = 0.0;
        this.font = this.DEFAULT_FONT;
        this.stroke = this.DEFAULT_AXIS_LINE_STROKE;
        this.plotType = this.DEFAULT_PLOT_TYPE;
    }

    public ScaleSelectionMode getScaleSelectionMode() {
        return scaleMode;
    }

    public void setScaleSelectionMode(ScaleSelectionMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        dataset.setDomainAxis(this);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale(Rectangle2D dataArea) {

        double scaleX;
        scaleMode = ScaleSelectionMode.MANUAL;
        if (this.maxAxisValue == 0.0) {
            scaleMode = ScaleSelectionMode.CALCULATE_FROM_DATASET;
        }
        if (scaleMode == ScaleSelectionMode.MANUAL) {
            scaleX = (xRight - xLeft) / maxAxisValue;
        } else {
            scaleX = GraphUtilities.getScaleX(dataset, dataArea);
        }
        // scale=(scaleY*-1.0);
        return scaleX;

    }

    public void setAxisLineColor(Color color) {
        this.axisLineColor = color;
    }

    /**
     * Set the vertical position of the axis
     *
     * @param y The Y value of the axis
     */
    public void setY(double y) {
        this.y = y;

    }

    /**
     * Set the rightmost point of the axis.
     *
     * @param xRight the rightmost point of the axis.
     */
    public void setXRight(double xRight) {
        this.xRight = xRight;
    }

    /**
     * Set the leftmost point of the axis.
     *
     * @param xLeft the leftmost point of the axis.
     */
    public void setXLeft(double xLeft) {
        this.xLeft = xLeft;
    }

    public void drawTickMarks(Graphics2D g2, Rectangle2D dataArea) {
        //double tScale;
        double tScale, xIter, yPos, increment;
        //if (scaleMode == ScaleSelectionMode.CALCULATE_FROM_DATASET) tScale = GraphUtilities.getScaleX(dataset,dataArea);
        // else tScale = scale;
        switch (plotType) {
            case GRAPH:
                tScale = this.getScale(dataArea);
                xIter = dataArea.getMaxX();//xRight;
                yPos = y + 10.0;
                increment = dataArea.getWidth() / 10.0;//(xRight - xLeft) / 10.0;
                g2.setPaint(axisLineColor);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                synchronized (this) {
                    while (xIter > dataArea.getMinX()) {
                        float val = (float) ((xIter - dataArea.getMinX()) / tScale);//(float) ((xIter - xLeft) / tScale);
                        String s = String.format("%,.2f", val);
                        g2.drawString(s, (int) xIter - fm.stringWidth(s) / 2, (int) yPos);
                        double tml = 5;
                        g2.draw(new Line2D.Double(xIter, y, xIter, y - tml));
                        xIter -= increment;
                    }

                    g2.drawString("Discharge , lps", (int) xRight
                            - 2 * fm.stringWidth("Discharge , lps") - 5, (int) y + 2 * fm.getHeight());
                }
                break;
            case ECONOMY_REPORT:

                tScale = this.getScale(dataArea);
                xIter = dataArea.getMaxX();//xRight;

                yPos = y + 8.0;
                increment = dataArea.getWidth() / 20.0;//(xRight - xLeft) / 10.0;
                g2.setPaint(axisLineColor);
                g2.setFont(new Font("SansSerif", Font.BOLD, 5));
                fm = g2.getFontMetrics();
                synchronized (this) {
                    float val = (float) ((xIter - dataArea.getMinX()) / tScale);
                    String s = String.format("%,.2f", val);
                    g2.drawString(s, (int) xIter - 10, (int) yPos);
                    xIter -= increment;
                    while (xIter > dataArea.getMinX()) {
                        val = (float) ((xIter - dataArea.getMinX()) / tScale);
                        s = String.format("%,.2f", val);
                        g2.drawString(s, (int) xIter - fm.stringWidth(s)/2, (int) yPos);
                        double tml = 5;
                        g2.draw(new Line2D.Double(xIter, y, xIter, y - tml));
                        xIter -= increment;
                    }
                    fm = g2.getFontMetrics();
                    g2.drawString("Discharge , lps", (int) xRight
                            - 2 * fm.stringWidth("Discharge , lps") - 5, (int) y + 2 * fm.getHeight());
                }
                break;
        }
    }

    public void drawAxis(Graphics2D gc) {
        gc.setColor(axisLineColor);
        gc.draw(new Line2D.Double(xLeft, y, xRight, y));
        if (plotType == ComponentToBePrintedType.GRAPH) {
            gc.drawPolygon(new Polygon(new int[]{(int) xRight - 20, (int) xRight, (int) xRight - 20}, new int[]{(int) y - 3, (int) y, (int) y + 3}, 3));
        }
    }

    /**
     * @param maxAxisValue the maxAxisValue to set
     */
    public void setMaxAxisValue(double maxAxisValue) {
        this.maxAxisValue = maxAxisValue;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @return the stroke
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * @param stroke the stroke to set
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public void setPlotType(ComponentToBePrintedType plotType) {
        this.plotType = plotType;
    }
}
