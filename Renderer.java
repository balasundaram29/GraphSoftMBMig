import java.awt.*;
import java.awt.geom.*;

public abstract class Renderer
{
    Dataset dataset;
    private Plot plot;
    private Paint curvePaint;
    public abstract void drawCurve(Graphics2D  g2D,Rectangle2D dataArea)throws Exception;
       
    

    void setPlot(Plot plot) {
        this.plot= plot;

    }
    public abstract Dataset getDataset() ;
        
    public abstract Drawable getFunction();

    /**
     * @return the curvePaint
     */
    public Paint getCurvePaint() {
        return curvePaint;
    }

    /**
     * @param curvePaint the curvePaint to set
     */
    public void setCurvePaint(Paint curvePaint) {
        this.curvePaint = curvePaint;
    }

    public void setStroke(Stroke stroke) {
      // this.stroke = stroke; //throw new UnsupportedOperationException("Not yet implemented");
        
    }
}
