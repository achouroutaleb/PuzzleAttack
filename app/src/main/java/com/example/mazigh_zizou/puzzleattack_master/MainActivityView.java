package com.example.mazigh_zizou.puzzleattack_master;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mazigh-Zizou on 13/11/2017.
 */

public class MainActivityView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //déclaration des images utilisées


    private Bitmap panther;
    private Bitmap elephant;
    private Bitmap vide;
    private Bitmap panda;
    private Bitmap tigre;
    private Bitmap gagne;
    private Bitmap withe;
    private Bitmap background;
    public static final String Datee= "ScoreLevel3";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;



    boolean replay         = false;
    boolean DialogueDisp = true;
    mainActivity class1;
    mainActivity class2;
    boolean nextL = false;
    private Bitmap timer;

    private long beginTimer, endTimer;


    Paint text = new Paint();



    int xDown = 0;
    int yDown = 0;

    int l = 0;
    int   nbTouch = 0;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources   pRes;
    private Context 	pContext;
    private Context shContext;

    // déclaration de la matrice
    int [][] carte;


    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  //coordonnées en X du point d'ancrage de notre carte

    // initialisation de la taille


    static final int carteWidth    = 6;
    static final int carteHeight   = 7;
    static final int sizeCST = 53;

    // constante modelisant les differentes types de cases
    static final int    CST_vide      = 0;
    static final int    CST_brique1     = 1;//YELLOW
    static final int    CST_brique2     = 2;//ORANGE
    static final int    CST_brique3     = 3;//BLUE
    static final int CST_brique4 = 4;


    int[][][] ref = {
            {
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_brique2, CST_brique2, CST_vide, CST_brique2, CST_vide, CST_vide}
            }, {
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_brique4,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_brique4,CST_brique3,CST_vide,CST_vide,CST_vide},
            {CST_brique3,CST_brique3,CST_brique2,CST_brique4,CST_vide,CST_vide},
            {CST_brique1,CST_brique1,CST_brique2,CST_brique2,CST_brique1,CST_vide}
    }, {
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_brique3, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_brique2, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_brique2, CST_brique1, CST_vide, CST_vide},
            {CST_vide, CST_brique1, CST_brique1, CST_brique2, CST_brique1, CST_vide},
            {CST_vide, CST_brique3, CST_brique2, CST_brique3, CST_brique3, CST_vide}
    }};


    SurfaceHolder holder;
    private     Thread  cv_thread;
    private     boolean in      = true;





    public MainActivityView(Context context, AttributeSet attrs) {
        super(context,attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder= getHolder();
        holder.addCallback(this);

        // chargement des images
        pContext	    = context;
        pRes 		    = pContext.getResources();

        panther = BitmapFactory.decodeResource(pRes, R.drawable.panther);
        elephant = BitmapFactory.decodeResource(pRes, R.drawable.elephant);
        panda = BitmapFactory.decodeResource(pRes, R.drawable.panda);
        tigre = BitmapFactory.decodeResource(pRes, R.drawable.bricktiger);
        vide = BitmapFactory.decodeResource(pRes, R.drawable.empty);
        gagne = BitmapFactory.decodeResource(pRes, R.drawable.gagne);
        timer = BitmapFactory.decodeResource(pRes, R.drawable.nnn);
        withe  = BitmapFactory.decodeResource(pRes, R.drawable.vide);
        background   = BitmapFactory.decodeResource(pRes, R.drawable.background);


        class1 = new mainActivity();
        class2 = new mainActivity();

        // creation du thread
        cv_thread = new Thread(this);

        // prise de focus pour gestion des touches

        setFocusable(true);
    }


    public void starTimer(){
        beginTimer = System.currentTimeMillis();
    }
    public void stopTimer(){
        endTimer = System.currentTimeMillis();
    }
    public double getTimer() {
        return ((endTimer - beginTimer) / 1000);
    }



// chargement de l
    private void loadlevel()
    {
        for (int i = 0; i < carteWidth; i++)
        {
            for (int j = 0; j < carteHeight; j++)
            {
                carte[j][i] = ref[l][j][i];
            }
        }
        starTimer();
    }

    // initialisation du jeu
    public void initparameters() {

        Log.e("-FCT-", " initparameters()");

        carte =     new int[carteHeight][carteWidth];
        loadlevel();

        carteTopAnchor = getHeight()-371;
        carteLeftAnchor = (getWidth()) / carteWidth;
        //sizeCST= (getWidth())/6;
        //sizeCST = brickYellow.getWidth();

        if ((cv_thread != null) && (!cv_thread.isAlive()))
        {
            cv_thread.start();
            Log.e("-TEST-", "cv_thread.start()");
        }
    }

    // dessin du Timer

    private void paintTimer(Canvas canvas) {
        canvas.drawBitmap(timer, getWidth()-70, 50, null);
    }

    // dessin de l'image gagné
    private void paintWin(Canvas canvas) {
        canvas.drawBitmap(gagne, carteLeftAnchor + 30, carteTopAnchor + 20, null);
    }


    private void paintEmpty(Canvas canvas) {
        canvas.drawBitmap(withe, -10, -10, null);
    }


    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas)
    {
        for (int i = 0; i < carteHeight; i++)
        {
            for (int j = 0; j < carteWidth; j++)
            {
                switch (carte[i][j])
                {
                    case CST_brique2:
                        canvas.drawBitmap(elephant, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_vide:
                        canvas.drawBitmap(vide, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique3:
                        canvas.drawBitmap(panda, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique1:
                        canvas.drawBitmap(panther, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique4:
                        canvas.drawBitmap(tigre, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                }
            }
        }
    }

    //dessin du fond
    private void paintBackground(Canvas canvas) {
        canvas.drawBitmap(background, 1, 1, null);
    }

    private void paintMessage(Canvas canvas)
    {
        /*sharedpreferences = pContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       // SharedPreferences sharedPref = MainActivityView.this.pContext.get(Context.MODE_PRIVATE);
        SharedPreferences shareDate =this.pContext.getSharedPreferences("maz",Context.MODE_PRIVATE);

        final Float dateajout= shareDate.getFloat("madatee",0);*/



        int time = (int) getTimer();
        int time1= 0;
        int time2=0;
        text.setTextSize(19);
        text.setFakeBoldText(true);



        text.setStyle(Paint.Style.FILL_AND_STROKE);
        if (nbTouch == 2)
        {
            SharedPreferences shareLevel3 = this.pContext.getSharedPreferences("level3",MODE_PRIVATE);
            final String share3= shareLevel3.getString("ScoreLevel3"," ");
            SharedPreferences shareLevel2 = this.pContext.getSharedPreferences("level2",MODE_PRIVATE);
            final String share2= shareLevel2.getString("ScoreLevel2"," ");
            SharedPreferences shareLevel1 = this.pContext.getSharedPreferences("level4",MODE_PRIVATE);
            final String share1= shareLevel1.getString("ScoreLevel4"," ");

            String timee = new String();
            timee= Integer.toString(time);
            if (isWon())

            {

                if ( l==0) {
                   /* String sss = new String();
                    sss= Integer.toString(time);*/

                    SharedPreferences.Editor editorz = pContext.getSharedPreferences("level4", MODE_PRIVATE).edit();
                    editorz.putString("ScoreLevel4", (String) timee);
                    editorz.commit();
                    SharedPreferences.Editor editorR = pContext.getSharedPreferences("level4", MODE_PRIVATE).edit();
                    editorR.putString("ScoreLevel4", (String) timee);
                    editorR.commit();

                }



                if ( l==1){

                    SharedPreferences.Editor editor = pContext.getSharedPreferences("level2", MODE_PRIVATE).edit();
                    editor.putString("ScoreLevel2", (String) timee);
                    editor.commit();


                }

                if(l == 2)


                    {


                       SharedPreferences.Editor editorR = pContext.getSharedPreferences("level3", MODE_PRIVATE).edit();
                        editorR.putString("ScoreLevel3", (String) timee);
                        editorR.commit();

                        paintEmpty(canvas);
                        paintWin(canvas);










                    }






            }
            else
            {

                class2.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {

                        try{
                            Thread.sleep(100);
                        }catch(Exception e){}

                        if (DialogueDisp && !isWon()) {
                            AlertDialog();
                            DialogueDisp = false;
                        }
                    }
                });

                if (replay) {
                    nbTouch = 0;
                    loadlevel();
                    DialogueDisp = false;
                    replay = false;
                }
            }
        }


        if (nbTouch == 1)
        {
            if(!isWon())

            {
                DialogueDisp = true;
                text.setColor(Color.WHITE);
                canvas.drawText("1 déplacement autorisé ", 12, carteTopAnchor / 2, text);
                paintTimer(canvas);
                canvas.drawText(""+ time + "", getWidth()-50, 120, text);
              /*  String monScore = new String();
                monScore= String.valueOf(time);*/



            }
        }

        if (nbTouch == 0)
        { if (l==0){
            DialogueDisp = true;
            text.setColor(Color.WHITE);
            canvas.drawText("1 déplacement autorisé ", 12, carteTopAnchor / 2, text);
            paintTimer(canvas);
            canvas.drawText(""+ time + "", getWidth()-50, 120, text);}
        else
            {
            text.setColor(Color.WHITE);
            canvas.drawText("2 déplacement autorisé ", 12, carteTopAnchor / 2, text);
            paintTimer(canvas);
            canvas.drawText(""+ time + "", getWidth()-50, 120, text);

        }

    }}

    //Identifies whether the game is won
    public boolean isWon()
    {


        boolean won = true;
        int i = 0;
        int j = 0;

        while (won & i < carteHeight)
        {
            j = 0;
            while (won & j < carteWidth)
            {
                if (carte[i][j] != CST_vide)
                {
                    won = false;
                }
                j++;
            }
            i++;
        }
        return won;
    }

    private void nDraw(Canvas canvas) {
        paintBackground(canvas);
        paintMessage(canvas);

        if (isWon()) {
            class1.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (DialogueDisp) {
                        if(l != 2) {
                            AlertDialog();
                        }
                        DialogueDisp = false;
                    }
                }
            });

            if (nextL) {
                l++;
                loadlevel();
                paintcarte(canvas);

                nbTouch = 0;
                nextL = false;
            }
        }


        else {
            paintcarte(canvas);
            delet();
        }
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> TEST <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> TEST <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> TEST <-", "surfaceDestroyed");
    }


    public void run() {
        Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(300);
                stopTimer();

                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
            }
        }
    }




    public void delet()
    {
        boolean vert=true;
        boolean hor=true;
        do{
            hor = false;
            for (int i = 0; i < 7; i++) {
                boolean bool1 = true;
                for (int j = 0; j < 4; j++) {
                    if (carte[i][j] == carte[i][j + 1] && carte[i][j] == carte[i][j + 2] && carte[i][j] != CST_vide) {
                        hor = true;
                        for (int k = j + 3; k < 6; k++) {
                            while (bool1) {
                                bool1 = false;
                                if (carte[i][j] == carte[i][k]) {
                                    bool1 = true;
                                    carte[i][k] = CST_vide;
                                    int b = i;
                                    while (i > 0) {
                                        carte[i][k] = carte[i - 1][k];
                                        carte[i - 1][k] = CST_vide;
                                        i--;
                                    }
                                    i = b;
                                    k++;
                                }
                            }
                        }
                        carte[i][j] = CST_vide;
                        carte[i][j + 1] = CST_vide;
                        carte[i][j + 2] = CST_vide;


                        if (i > 0) {
                            int a = i;
                            while (i > 0) {
                                carte[i][j] = carte[i - 1][j];
                                carte[i - 1][j] = CST_vide;
                                i--;
                            }
                            i = a;
                            while (i > 0) {
                                carte[i][j + 1] = carte[i - 1][j + 1];
                                carte[i - 1][j + 1] = CST_vide;
                                i--;
                            }

                            i = a;
                            while (i > 0) {
                                carte[i][j + 2] = carte[i - 1][j + 2];
                                carte[i - 1][j + 2] = CST_vide;
                                i--;
                            }
                            i = a;
                        }

                    }
                }

            }



            vert = false;
            for (int i = 0; i < 7; i++) {
                boolean bool1 = true;
                for (int j = 0; j < 5; j++) {
                    if (carte[j][i] == carte[j + 1][i] && carte[j][i] == carte[j + 2][i] && carte[j][i] != CST_vide) {
                        vert = true;
                        int s = carte[j][i];
                        carte[j][i] = CST_vide;

                        int b = j;
                        while (j > 0) {
                            carte[j][i] = carte[j - 1][i];
                            carte[j - 1][i] = CST_vide;
                            j--;
                        }
                        carte[j + 1][i] = CST_vide;
                        j = b;
                        while (j > 0) {
                            carte[j + 1][i] = carte[j][i];
                            carte[j][i] = CST_vide;
                            j--;
                        }
                        carte[j + 2][i] = CST_vide;
                        j = b;
                        b = j;
                        while (j > 0) {
                            carte[j + 2][i] = carte[j + 1][i];
                            carte[j + 1][i] = CST_vide;
                            j--;
                        }
                        j = b;


                        for (int k = j + 3; k < 7; k++) {
                            while (bool1) {
                                bool1 = false;
                                if (s == carte[k][i]) {
                                    bool1 = true;
                                    carte[k][i] = CST_vide;
                                    int o = k;
                                    while (k > 0) {
                                        carte[k][i] = carte[k - 1][i];
                                        carte[k - 1][i] = CST_vide;
                                        k--;
                                    }
                                    k = o;
                                    k++;
                                }
                            }
                        }

                    }
                }
            }
        }while(hor || vert);
    }



    // la récuperation des évenement tactile
    public boolean onTouchEvent (MotionEvent event) {
        if (nbTouch < 2) {
            boolean empty = true;
            int temp = 0;
            int direction = 0;
            int action = event.getAction();
            int x = (int) (event.getX() / sizeCST);
            int y = (int) ((event.getY() - carteTopAnchor) / sizeCST);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    xDown = x;
                    yDown = y;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    return true;

                case MotionEvent.ACTION_UP:


                    if ((x - xDown) > 0) { direction = 1;  }  //Déplacement à droite
                    if ((x - xDown) < 0) { direction = -1; }  //Déplacement à gauche


                    if (yDown >= 0)
                    {
                        //si la case qu'on a touchée n'est pas vide
                        if (carte[yDown][xDown] != CST_vide)
                        {
                            if (direction != 0) { nbTouch++; }

                            //on verifie que la case n'est pas à l'extrémité de la carte X
                            if ((direction == 1 && xDown < carteWidth) || ( direction == -1 && xDown > 0))
                            {
                                //on fait notre permutation
                                temp = carte[yDown][xDown];
                                carte[yDown][xDown] = carte[yDown][xDown + (direction)];
                                carte[yDown][xDown + ( direction )] = temp;

                                //si notre case initiale a été remplacée par une case vide, on fait descendre
                                //ce qu'il y'a au dessus ( si ce n'est pas vide )
                                if (carte[yDown][xDown] == CST_vide)
                                {
                                    if (yDown > 0)
                                    {
                                        if (carte[yDown - 1][xDown] != CST_vide)
                                        {
                                            int a = yDown;
                                            while (a > 0)
                                            {
                                                carte[a][xDown] = carte[a - 1][xDown];
                                                carte[a - 1][xDown] = CST_vide;
                                                a--;
                                            }
                                        }
                                    }
                                }

                                //on verifie que la case n'est pas à l'extrémité de la carte Y
                                if (yDown < carteHeight - 1)
                                {
                                    //si après permutation notre case est suspendu sur un vide, on la fait descendre
                                    if (carte[yDown + 1][xDown + ( direction) ] == CST_vide)
                                    {
                                        int x1 = xDown + ( direction );
                                        int y1 = yDown + 1;
                                        do{
                                            empty = false;
                                            if (carte[y1][x1] == CST_vide)
                                            {
                                                empty=true;
                                                carte[y1][x1] = carte[y1 - 1][x1];
                                                carte[y1 - 1][x1] = CST_vide;
                                            }
                                            y1++;
                                        }while (empty & y1 < carteHeight);
                                    }
                                }
                            }
                        }
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }



    private void AlertDialog(){
        AlertDialog.Builder about = new AlertDialog.Builder(pContext);


        try{
            Thread.sleep(301);
        }catch(Exception e){}

        if(isWon()) {
            about.setTitle("Félicitations ");
        }
        else {
            about.setTitle("Perdue :/");
        }

        TextView l_viewabout = new TextView(pContext);
        l_viewabout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        l_viewabout.setPadding(10, 10, 10, 10);
        l_viewabout.setTextSize(20);

        if(isWon()) {
            l_viewabout.setText("Voulez vous continuer ?");
        }
        else {
            l_viewabout.setText("Voulez vous rejouer ?");
        }
        l_viewabout.setMovementMethod(LinkMovementMethod.getInstance());
        about.setView(l_viewabout);


        about.setPositiveButton("oui", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isWon()) {
                    nextL = true;}
                else        {replay = true;}
            }
        });
        about.setNegativeButton("non", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isWon()) {
                    nextL = false;
                }
                else        {
                    replay = false;
                }

                class1.finish();
            }
        });
        about.show();
    }
}
