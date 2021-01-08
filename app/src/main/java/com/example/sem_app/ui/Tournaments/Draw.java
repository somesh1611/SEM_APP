package com.example.sem_app.ui.Tournaments;

public class Draw {

    String player1Name;
    String player2Name;
    String score1;
    String score2;
    String winner;
    String drawId;

    public Draw(String id,String p1,String p2)
    {
        this.drawId=id;
        this.player1Name=p1;
        this.player2Name=p2;

    }

    public Draw(String id,String p1,String p2,String s1,String s2,String w)
    {
        this.drawId=id;
        this.player1Name=p1;
        this.player2Name=p2;
        this.score1=s1;
        this.score2=s2;
        this.winner=w;

    }

    public String getDrawId() { return drawId; }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public String getScore1() {
        return score1;
    }

    public String getScore2() {
        return score2;
    }

    public String getWinner() {
        return winner;
    }

}
