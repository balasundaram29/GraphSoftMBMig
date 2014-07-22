
import java.awt.*;
import java.awt.FontMetrics;
import java.awt.geom.*;

/**
 * Write a description of class RangeAxis here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class RangeAxis extends Axis {

    private double x, yTop, yBottom, scale;
    public AxisPosition axisPosition;
    private ScaleSelectionMode scaleMode;
    private Dataset dataset;
    private String label;
    private Color DEFAULT_AXIS_LINE_PAINT = Color.black;
    private ScaleSelectionMode DEFAULT_SCALE_MODE = ScaleSelectionMode.CALCULATE_FROM_DATASET;
    private Paint axisLinePaint;
    private Font font;
    private Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 10);
    private ComponentToBePrintedType DEFAULT_PLOT_TYPE = ComponentToBePrintedType.GRAPH;
    private ComponentToBePrintedType plotType;
    private Stroke stroke;
    private Stroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(1.5f);
    private double maxAxisValue;

    public RangeAxis(String label, AxisPosition axisPosition) {
        this.label = label;
        this.axisPosition = axisPosition;
        this.axisLinePaint = DEFAULT_AXIS_LINE_PAINT;
        this.scaleMode = DEFAULT_SCALE_MODE;
        this.font = DEFAULT_FONT;
        this.stroke = DEFAULT_AXIS_LINE_STROKE;
        this.maxAxisValue = 0.0;
        this.plotType = DEFAULT_PLOT_TYPE;
    }

    public ScaleSelectionMode getScaleSelectionMode() {
        return scaleMode;
    }

    public void setScaleSelectionMode(ScaleSelectionMode scaleMode) {
        this.setScaleMode(scaleMode);
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        dataset.setRangeAxis(this);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale(Rectangle2D dataArea) {
        double scaleY;
        scaleMode = ScaleSelectionMode.MANUAL;
        if (this.maxAxisValue == 0.0) {
            scaleMode = ScaleSelectionMode.CALCULATE_FROM_DATASET;
        }
        if (scaleMode == ScaleSelectionMode.MANUAL) {
            scaleY = (yBottom - getyTop()) / maxAxisValue;
            // scaleY=scaleY*(-1.0);
        } else {
            scaleY = GraphUtilities.getScaleY(dataset, dataArea);
        }
        // scaleY=(scaleY*-1.0);
        return scaleY;
    }

    public void setX(double x) {
        this.x = x;

    }

    public void setYTop(double yTop) {
        this.setyTop(yTop);
    }

    public void setYBottom(double yBottom) {
        this.yBottom = yBottom;
    }

    public void drawAxis(Graphics2D g2D) {
        switch (plotType) {
            case GRAPH:
                drawAxisForBigGraph(g2D);
                break;
            case ECONOMY_REPORT:
                drawAxisForEconomyGraph(g2D);
                break;
        }
    }

    public void drawAxisForBigGraph(Graphics2D g2D) {
        g2D.setFont(font);
        g2D.setPaint(getAxisLinePaint());
        //  g2D.drawString("10,10",10,10);
        // g2D.drawRect(0, 0, 10, 10);
        g2D.draw(new Line2D.Double(x, yBottom, x, getyTop() - 60));

        Polygon poly = new Polygon(new int[]{(int) x - 3, (int) x + 3, (int) x}, new int[]{(int) getyTop() - 60, (int) getyTop() - 60, (int) getyTop() - 20 - 60}, 3);
        g2D.drawPolygon(poly);
        AffineTransform at = new AffineTransform();
        at.setToRotation(-Math.PI / 2);
        Font theFont = g2D.getFont();
        Font derivedFont = theFont.deriveFont(at);
        FontMetrics fm = g2D.getFontMetrics();
        int offset = fm.stringWidth(label);
        g2D.setFont(derivedFont);
        Ellipse2D smallCircle = new Ellipse2D.Double(-1, -1, 2, 2);

        switch (this.getDataset().getType()) {

            case DISCHARGE_VS_CURRENT:
                int offset1 = fm.stringWidth("Current,A");
                g2D.drawString("Current,A", (int) x, (int) getyTop() - 60 + offset1);
                Ellipse2D circle = new Ellipse2D.Double(-3.0, -3.0, 6, 6);
                g2D.draw(ShapeUtilities.createTranslatedShape(circle, (int) x - 15, (int) getyTop() - offset1 + 25));
                Shape CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 15, (int) getyTop() - offset1 + 25);
                g2D.fill(CircleAtPlace);
                break;

            case DISCHARGE_VS_EFFICIENCY:
                offset1 = fm.stringWidth("Overall");
                g2D.drawString("Overall", (int) x - 2, (int) getyTop() - 60 + offset1);
                offset1 = fm.stringWidth("Eff. ,%");
                g2D.drawString("Eff. ,%", (int) x + fm.getHeight(), (int) getyTop() - 60 + offset1);
                Rectangle2D rectangle = new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0);
                g2D.draw(ShapeUtilities.createTranslatedShape(rectangle, (int) x - 12, (int) getyTop() - 50 + offset1));
                CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 12, (int) getyTop() - 50 + offset1);
                g2D.fill(CircleAtPlace);

                break;
            case DISCHARGE_VS_HEAD:
                offset1 = fm.stringWidth("Total");
                g2D.drawString("Total", (int) x - 2, (int) getyTop() - 60 + offset1);
                offset1 = fm.stringWidth("Head,m");
                g2D.drawString("Head,mWC", (int) x + fm.getHeight(), (int) getyTop() - 50 + offset1);
                Shape triangle = ShapeUtilities.createUpTriangle(4.00f);
                g2D.draw(ShapeUtilities.createTranslatedShape(triangle, (int) x - 2 - 10, (int) getyTop() - 55 + offset1));
                CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 12, (int) getyTop() - 55 + 2 + offset1);
                g2D.fill(CircleAtPlace);

                break;
        }

        //g2D.drawString(label, (int) x - 5, (int) yTop + offset);
        g2D.setFont(theFont);

    }

    public void drawAxisForEconomyGraph(Graphics2D g2D) {
        g2D.setFont(new Font("SansSerif", Font.BOLD, 5));
        g2D.setPaint(getAxisLinePaint());
        g2D.draw(new Line2D.Double(x, yBottom, x, getyTop() - 60));
        Polygon poly = new Polygon(new int[]{(int) x - 3, (int) x + 3, (int) x}, new int[]{(int) getyTop() - 60, (int) getyTop() - 60, (int) getyTop() - 20 - 60}, 3);
        g2D.drawPolygon(poly);
        AffineTransform at = new AffineTransform();
        Font theFont = g2D.getFont();
        Font derivedFont = theFont.deriveFont(at);
        FontMetrics fm = g2D.getFontMetrics();
        int offset = fm.stringWidth(label);
        Ellipse2D smallCircle = new Ellipse2D.Double(-1, -1, 2, 2);
        int parOffset = 10;
        int shapeOffset = 15;
        int unitOffset = 25;
        switch (this.getDataset().getType()) {

            case DISCHARGE_VS_CURRENT:
                int offset1 = fm.stringWidth("I");
                g2D.drawString("I", (int) x - 6, (int) getyTop() + parOffset);
                Ellipse2D circle = new Ellipse2D.Double(-3.0, -3.0, 6, 6);
                g2D.draw(ShapeUtilities.createTranslatedShape(circle, (int) x - 6, (int) getyTop() + shapeOffset));
                Shape CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 6, (int) getyTop() + shapeOffset);
                g2D.fill(CircleAtPlace);
                g2D.drawString("A", (int) x - 6 - fm.stringWidth("A") / 2 + 1, (int) getyTop() + unitOffset);
                break;

            case DISCHARGE_VS_EFFICIENCY:
                offset1 = fm.stringWidth("OAE");
                g2D.drawString("OAE", (int) x - 13, (int) getyTop() + parOffset);
                Rectangle2D rectangle = new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0);
                g2D.draw(ShapeUtilities.createTranslatedShape(rectangle, (int) x - 7, (int) getyTop() + shapeOffset));
                CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 7, (int) getyTop() + shapeOffset);
                g2D.fill(CircleAtPlace);
                g2D.drawString("%", (int) x - 8, (int) getyTop() + unitOffset);

                break;
            case DISCHARGE_VS_HEAD:
                g2D.drawString("TH", (int) x - 11, (int) getyTop() + parOffset);
                Shape triangle = ShapeUtilities.createUpTriangle(4.00f);
                g2D.draw(ShapeUtilities.createTranslatedShape(triangle, (int) x - 9, (int) getyTop() + shapeOffset));
                CircleAtPlace = ShapeUtilities.createTranslatedShape(smallCircle, (int) x - 9, (int) getyTop() + shapeOffset);
                g2D.fill(CircleAtPlace);
                g2D.drawString("mWC", (int) x - 14, (int) getyTop() + unitOffset);
                break;
        }
        g2D.setFont(theFont);
    }

    public void drawTickMarks(Graphics2D g2, Rectangle2D dataArea) {
        switch (plotType) {
            case GRAPH:
                drawTickMarksForBigGraph(g2, dataArea);
                break;
            case ECONOMY_REPORT:
                drawTickMarksForEconomyGraph(g2, dataArea);
                break;
        }
    }

    public void drawTickMarksForBigGraph(Graphics2D g2, Rectangle2D dataArea) {
        double tScale;
        g2.setFont(font);
        g2.setPaint(axisLinePaint);
        FontMetrics fm = g2.getFontMetrics();
        int offset = fm.stringWidth("xxxx");
        tScale = this.getScale(dataArea);
        double yIter = getyTop();
        double xPos;
        if (axisPosition == axisPosition.LEFT) {
            xPos = x;//- 20.0;
        } else {
            xPos = x + 10.0;
        }
        double increment = (yBottom - getyTop()) / 10.0;
        float dbl = (float) ((yBottom - yIter) / tScale);
        
        String s = String.format("%,.1f", dbl);
        //Draw first  values belaow the tick mark;There is no space above tick mark
        g2.drawString(s, (int) xPos - fm.stringWidth(s) - 5, (int) yIter + fm.getHeight() );
        yIter = yIter + increment;
        while (yIter < yBottom) {
            dbl = (float) ((yBottom - yIter) / tScale);
            //String tickString=Integer.toString((int)((yBottom-yIter)/tScale));
            s = String.format("%,.1f", dbl);
            g2.drawString(s, (int) xPos - fm.stringWidth(s) - 5, (int) yIter + fm.getHeight() / 3);
            //tick mark length;
            double tml = 5;
            g2.draw(new Line2D.Double(xPos, yIter, xPos + tml, yIter));
            yIter = yIter + increment;
        }
    }

    public void drawTickMarksForEconomyGraph(Graphics2D g2, Rectangle2D dataArea) {
        double tScale;
        g2.setFont(new Font("SansSerif", Font.BOLD, 5));
        g2.setPaint(axisLinePaint);
        FontMetrics fm = g2.getFontMetrics();
        int offset = fm.stringWidth("xxxx");
        tScale = this.getScale(dataArea);
        double yIter = getyTop();
        double xPos;
        if (axisPosition == axisPosition.LEFT) {
            xPos = x;//- 20.0;
        } else {
            xPos = x + 10.0;
        }
        double increment = (yBottom - getyTop()) / 10.0;
        while (yIter < yBottom) {
            float dbl = (float) ((yBottom - yIter) / tScale);
            //String tickString=Integer.toString((int)((yBottom-yIter)/tScale));
            String s = String.format("%,.1f", dbl);
            g2.drawString(s, (int) xPos - fm.stringWidth(s) - 1, (int) yIter + fm.getHeight() / 2);
            //tick mark length;
            double tml = 5;
            g2.draw(new Line2D.Double(xPos, yIter, xPos + tml, yIter));
            yIter = yIter + increment;
        }
    }

    public void setAxisLinePaint(Paint paint) {
        this.axisLinePaint = paint;
    }

    /**
     * @return the axisLineColor
     */
    public Paint getAxisLinePaint() {
        return axisLinePaint;
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
     * @param stroke the stroke to set
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * @return the yTop
     */
    public double getyTop() {
        return yTop;
    }

    /**
     * @param yTop the yTop to set
     */
    public void setyTop(double yTop) {
        this.yTop = yTop;
    }

    /**
     * @param scaleMode the scaleMode to set
     */
    public void setScaleMode(ScaleSelectionMode scaleMode) {
        this.scaleMode = scaleMode;
    }

    /**
     * @param maxAxisValue the maxAxisValue to set
     */
    public void setMaxAxisValue(double maxAxisValue) {
        this.maxAxisValue = maxAxisValue;
    }

    public void setPlotType(ComponentToBePrintedType plotType) {
        this.plotType = plotType;
    }
}
