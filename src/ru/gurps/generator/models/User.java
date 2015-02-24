package ru.gurps.generator.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import ru.gurps.generator.config.Model;

public class User extends Model {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty currentPoints;
    private SimpleStringProperty maxPoints;
    private SimpleIntegerProperty st;
    private SimpleIntegerProperty dx;
    private SimpleIntegerProperty iq;
    private SimpleIntegerProperty ht;
    private SimpleIntegerProperty hp;
    private SimpleIntegerProperty will;
    private SimpleIntegerProperty per;
    private SimpleIntegerProperty fp;
    private SimpleDoubleProperty bs;
    private SimpleIntegerProperty move;
    private SimpleIntegerProperty sm;
    private SimpleBooleanProperty noFineManipulators;
    
    public User(){
    }

    public User(int id, String name, String currentPoints, String maxPoints, int st, int dx, int iq, int ht, int hp, int will,
                int per, int fp, double bs, int move, int sm, boolean noFineManipulators) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.currentPoints = new SimpleStringProperty(currentPoints);
        this.maxPoints = new SimpleStringProperty(maxPoints);
        this.st = new SimpleIntegerProperty(st);
        this.dx = new SimpleIntegerProperty(dx);
        this.iq = new SimpleIntegerProperty(iq);
        this.ht = new SimpleIntegerProperty(ht);
        this.hp = new SimpleIntegerProperty(hp);
        this.will = new SimpleIntegerProperty(will);
        this.per = new SimpleIntegerProperty(per);
        this.fp = new SimpleIntegerProperty(fp);
        this.bs = new SimpleDoubleProperty(bs);
        this.move = new SimpleIntegerProperty(move);
        this.sm = new SimpleIntegerProperty(sm);
        this.noFineManipulators = new SimpleBooleanProperty(noFineManipulators);
    }

    public int getId() { return id.get(); }

    public void setId(int sId) { id.set(sId); }

    public String getName() { return name.get(); }

    public void setName(String sName) { name.set(sName); }

    public String getCurrentPoints() { return currentPoints.get(); }

    public void setCurrentPoints(String sCurrentPoints) { currentPoints.set(sCurrentPoints); }

    public String getMaxPoints() { return maxPoints.get(); }

    public void setMaxPoints(String sMaxPoints) { maxPoints.set(sMaxPoints); }

    public int getSt() { return st.get(); }

    public void setSt(int sSt) { st.set(sSt); }

    public int getDx() { return dx.get(); }

    public void setDx(int sDx) { dx.set(sDx); }

    public int getIq() { return iq.get(); }

    public void setIq(int sIq) { iq.set(sIq); }

    public int getHt() { return ht.get(); }

    public void setHt(int sHt) {ht.set(sHt); }

    public int getHp() { return hp.get(); }

    public void setHp(int sHp) { hp.set(sHp); }

    public int getWill() { return will.get(); }

    public void setWill(int sWill) { will.set(sWill); }

    public int getPer() { return per.get(); }

    public void setPer(int sPer) { per.set(sPer); }

    public int getFp() { return fp.get(); }

    public void setFp(int sFp) { fp.set(sFp); }

    public double getBs() { return bs.get(); }

    public void setBs(double sBs) { bs.set(sBs); }

    public int getMove() { return move.get(); }

    public void setMove(int sMove) { move.set(sMove); }

    public int getSm() { return sm.get(); }

    public void setSm(int sSm) { sm.set(sSm); }

    public boolean getNoFineManipulators() { return noFineManipulators.get(); }

    public void setNoFineManipulators(boolean sNoFineManipulators) { noFineManipulators.set(sNoFineManipulators); }
}
