
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
@Entity
public class ObsValuesOfReadings implements Serializable {
    @Id@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String readingSNo;
    private double discharge;
    private double head;
    private double eff;
    private double mcurrent;

    /**
     * @return the discharge
     */
    public double getDischarge() {
        return discharge;
    }

    /**
     * @param discharge the discharge to set
     */
    public void setDischarge(double discharge) {
        this.discharge = discharge;
    }

    /**
     * @return the head
     */
    public double getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(double head) {
        this.head = head;
    }

    /**
     * @return the eff
     */
    public double getEff() {
        return eff;
    }

    /**
     * @param eff the eff to set
     */
    public void setEff(double eff) {
        this.eff = eff;
    }

   

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the readingSNo
     */
    public String getReadingSNo() {
        return readingSNo;
    }

    /**
     * @param readingSNo the readingSNo to set
     */
    public void setReadingSNo(String readingSNo) {
        this.readingSNo = readingSNo;

    }

    /**
     * @return the mcurrent
     */
    public double getMcurrent() {
        return mcurrent;
    }

    /**
     * @param mcurrent the mcurrent to set
     */
    public void setMcurrent(double mcurrent) {
        this.mcurrent = mcurrent;
    }
}
