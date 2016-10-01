package quadespellman.tonposeclasses;

/**
 * Created by Quade Spellman on 9/27/2016.
 */
public class Player extends Living{
    protected int score;
    protected int xp;
    protected int lvl;
    protected boolean logged;
    protected int userId;
    protected String  userName;
    protected Item equiped;

    public Player(String name, int id){
        userId = id;
        userName = name;
        score = 0;
        xp = 0;
        lvl = 1;
    }

    public void updateScore(int points){
        score += points;
    }
    public void equipItem(Item toEquip){

    }
    public void gainXp(int gain){
        xp += gain;
    }
    public void login(){
        logged  = true;
    }
    public void logout(){
        logged = false;
    }

}
