
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.IndexColumn;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@Entity
public class Readings implements Serializable{
    @Id
    @Column(name="PUMP_SL_NO")
    private String sNo;
    private String inPassNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date tdate;
    private double gaugeDistance;
    //IS(Indian Standard) reference
    private String isRef;
    private String remarks;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="DECL_ID", unique= true, nullable=true, insertable=true, updatable=true)
    private DeclValuesOfReadings declValues;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="OBS_ID")
    private ObsValuesOfReadings obsValues;
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name="PARENT_ID",referencedColumnName ="PUMP_SL_NO")
    @IndexColumn(name="ROW_IDX")
    private ReadingRow[] readingRows;
//for scaling purpose
    private double dischMax;
    private double headMax;
    private double effMax;
    private double currMax;

    public Readings() {
    }

    /**
     * @return the sNo
     */
    public String getsNo() {
        return sNo;
    }

    /**
     * @param sNo the sNo to set
     */
    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    /**
     * @return the inPassNo
     */
    public String getInPassNo() {
        return inPassNo;
    }

    /**
     * @param inPassNo the inPassNo to set
     */
    public void setInPassNo(String inPassNo) {
        this.inPassNo = inPassNo;
    }

    /**
     * @return the gaugeDistance
     */
    public double getGaugeDistance() {
        return gaugeDistance;
    }

    /**
     * @param gaugeDistance the gaugeDistance to set
     */
    public void setGaugeDistance(double gaugeDistance) {
        this.gaugeDistance = gaugeDistance;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the isRef
     */
    public String getIsRef() {
        return isRef;
    }

    /**
     * @param isRef the isRef to set
     */
    public void setIsRef(String isRef) {
        this.isRef = isRef;
    }

   

    /**
     * @return the declValues
     */
    public DeclValuesOfReadings getdeclValues() {
        return declValues;
    }

    /**
     * @param declValues the declValues to set
     */
    public void setdeclValues(DeclValuesOfReadings declValues) {
        this.declValues = declValues;
    }

    /**
     * @return the dischMax
     */
    public double getDischMax() {
        return dischMax;
    }

    /**
     * @param dischMax the dischMax to set
     */
    public void setDischMax(double dischMax) {
        this.dischMax = dischMax;
    }

    /**
     * @return the headMax
     */
    public double getHeadMax() {
        return headMax;
    }

    /**
     * @param headMax the headMax to set
     */
    public void setHeadMax(double headMax) {
        this.headMax = headMax;
    }

    /**
     * @return the effMax
     */
    public double getEffMax() {
        return effMax;
    }

    /**
     * @param effMax the effMax to set
     */
    public void setEffMax(double effMax) {
        this.effMax = effMax;
    }

    /**
     * @return the currMax
     */
    public double getCurrMax() {
        return currMax;
    }

    /**
     * @param currMax the currMax to set
     */
    public void setCurrMax(double currMax) {
        this.currMax = currMax;
    }

    /**
     * @return the obsValues
     */
    public ObsValuesOfReadings getObsValues() {
        return obsValues;
    }

    /**
     * @param obsValues the obsValues to set
     */
    public void setObsValues(ObsValuesOfReadings obsValues) {
        this.obsValues = obsValues;
       // obsValues.setReadingSNo(sNo);

    }

    /**
     * @return the readingRows
     */
    public ReadingRow[] getReadingRows() {
        return readingRows;
    }

    /**
     * @param readingRows the readingRows to set
     */
    public void setReadingRows(ReadingRow[] readingRows) {
        this.readingRows = readingRows;
    }

    /**
     * @return the tdate
     */
    public Date getTdate() {
        return tdate;
    }

    /**
     * @param tdate the tdate to set
     */
    public void setTdate(Date tdate) {
        this.tdate = tdate;
    }
}
