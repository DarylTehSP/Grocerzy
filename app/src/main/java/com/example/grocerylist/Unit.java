package com.example.grocerylist;

import android.support.v7.app.AppCompatActivity;

public class Unit extends ListOfUnits {
    String name;
    int hp;
    int atk;
    int def;
    int rec;

    public String getName() {
        return this.name;
    }
    public int getHp() {
        return this.hp;
    }
    public int getAtk() {
        return this.atk;
    }
    public int getDef() {
        return this.def;
    }
    public int getRec() {
        return this.rec;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setHp() {
        this.hp = hp;
    }
    public void setAtk(int atk) {
        this.atk = atk;
    }
    public void setDef(int def) {
        this.def = def;
    }
    public void setRec(int rec) {
        this.rec = rec;
    }
}
