

        package com.example.mazigh_zizou.puzzleattack_master;


        import android.app.Activity;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.widget.TextView;

/**
 * Created by Mazigh-zizou on 08/12/2017.
 */

public class score extends Activity {

    TextView scoreLevel3;
    TextView scoreLevel2;
    TextView scoreLevel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        scoreLevel3 = (TextView) findViewById(R.id.afficheur_level3);
        scoreLevel2 = (TextView) findViewById(R.id.score_level2);
       // scoreLevel1= (TextView) findViewById(R.id.best_level2);


        SharedPreferences shareLevel3 = this.getSharedPreferences("level3", Context.MODE_PRIVATE);
        String mazighhh = shareLevel3.getString("ScoreLevel3", " ");
        scoreLevel3.setText(""+mazighhh);

        SharedPreferences shareLevel2 = this.getSharedPreferences("level2", Context.MODE_PRIVATE);
        String mazighh = shareLevel2.getString("ScoreLevel2", " ");


        int x=0;

        int bb=  Integer.parseInt(mazighh);
        int m =bb;


        if(x!=0){
            int aa=  Integer.parseInt(mazighh);
            if (aa<bb){

                bb=aa;
                String aff = Integer.toString(bb);

                scoreLevel2.setText(""+aff);
            }

        }

        x+=1;

        String aff = Integer.toString(bb);

        scoreLevel2.setText(""+aff);


       /* SharedPreferences shareLevel1 = this.getSharedPreferences("level1", Context.MODE_PRIVATE);
        String mazigho = shareLevel1.getString("ScoreLevel1", " ");
        scoreLevel1.setText(""+mazigho);*/


    }
}
